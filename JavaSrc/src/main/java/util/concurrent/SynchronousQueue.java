/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea, Bill Scherer, and Michael Scott with
 * assistance from members of JCP JSR-166 Expert Group and released to
 * the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * A {@linkplain BlockingQueue blocking queue} in which each insert
 * operation must wait for a corresponding remove operation by another
 * thread, and vice versa.  A synchronous queue does not have any
 * internal capacity, not even a capacity of one.  You cannot
 * {@code peek} at a synchronous queue because an element is only
 * present when you try to remove it; you cannot insert an element
 * (using any method) unless another thread is trying to remove it;
 * you cannot iterate as there is nothing to iterate.  The
 * <em>head</em> of the queue is the element that the first queued
 * inserting thread is trying to add to the queue; if there is no such
 * queued thread then no element is available for removal and
 * {@code poll()} will return {@code null}.  For purposes of other
 * {@code Collection} methods (for example {@code contains}), a
 * {@code SynchronousQueue} acts as an empty collection.  This queue
 * does not permit {@code null} elements.
 *
 * <p>Synchronous queues are similar to rendezvous channels used in
 * CSP and Ada. They are well suited for handoff designs, in which an
 * object running in one thread must sync up with an object running
 * in another thread in order to hand it some information, event, or
 * task.
 *
 * <p>This class supports an optional fairness policy for ordering
 * waiting producer and consumer threads.  By default, this ordering
 * is not guaranteed. However, a queue constructed with fairness set
 * to {@code true} grants threads access in FIFO order.
 *
 * <p>This class and its iterator implement all of the
 * <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <E> the type of elements held in this collection
 * @author Doug Lea and Bill Scherer and Michael Scott
 * @since 1.5
 */
public class SynchronousQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {
    private static final long serialVersionUID = -3223113410248163686L;

    /*
     * This class implements extensions of the dual stack and dual
     * queue algorithms described in "Nonblocking Concurrent Objects
     * with Condition Synchronization", by W. N. Scherer III and
     * M. L. Scott.  18th Annual Conf. on Distributed Computing,
     * Oct. 2004 (see also
     * http://www.cs.rochester.edu/u/scott/synchronization/pseudocode/duals.html).
     * The (Lifo) stack is used for non-fair mode, and the (Fifo)
     * queue for fair mode. The performance of the two is generally
     * similar. Fifo usually supports higher throughput under
     * contention but Lifo maintains higher thread locality in common
     * applications.
     *
     * A dual queue (and similarly stack) is one that at any given
     * time either holds "data" -- items provided by put operations,
     * or "requests" -- slots representing take operations, or is
     * empty. A call to "fulfill" (i.e., a call requesting an item
     * from a queue holding data or vice versa) dequeues a
     * complementary node.  The most interesting feature of these
     * queues is that any operation can figure out which mode the
     * queue is in, and act accordingly without needing locks.
     *
     * Both the queue and stack extend abstract class Transferer
     * defining the single method transfer that does a put or a
     * take. These are unified into a single method because in dual
     * data structures, the put and take operations are symmetrical,
     * so nearly all code can be combined. The resulting transfer
     * methods are on the long side, but are easier to follow than
     * they would be if broken up into nearly-duplicated parts.
     *
     * The queue and stack data structures share many conceptual
     * similarities but very few concrete details. For simplicity,
     * they are kept distinct so that they can later evolve
     * separately.
     *
     * The algorithms here differ from the versions in the above paper
     * in extending them for use in synchronous queues, as well as
     * dealing with cancellation. The main differences include:
     *
     *  1. The original algorithms used bit-marked pointers, but
     *     the ones here use mode bits in nodes, leading to a number
     *     of further adaptations.
     *  2. SynchronousQueues must block threads waiting to become
     *     fulfilled.
     *  3. Support for cancellation via timeout and interrupts,
     *     including cleaning out cancelled nodes/threads
     *     from lists to avoid garbage retention and memory depletion.
     *
     * Blocking is mainly accomplished using LockSupport park/unpark,
     * except that nodes that appear to be the next ones to become
     * fulfilled first spin a bit (on multiprocessors only). On very
     * busy synchronous queues, spinning can dramatically improve
     * throughput. And on less busy ones, the amount of spinning is
     * small enough not to be noticeable.
     *
     * Cleaning is done in different ways in queues vs stacks.  For
     * queues, we can almost always remove a node immediately in O(1)
     * time (modulo retries for consistency checks) when it is
     * cancelled. But if it may be pinned as the current tail, it must
     * wait until some subsequent cancellation. For stacks, we need a
     * potentially O(n) traversal to be sure that we can remove the
     * node, but this can run concurrently with other threads
     * accessing the stack.
     *
     * While garbage collection takes care of most node reclamation
     * issues that otherwise complicate nonblocking algorithms, care
     * is taken to "forget" references to data, other nodes, and
     * threads that might be held on to long-term by blocked
     * threads. In cases where setting to null would otherwise
     * conflict with main algorithms, this is done by changing a
     * node's link to now point to the node itself. This doesn't arise
     * much for Stack nodes (because blocked threads do not hang on to
     * old head pointers), but references in Queue nodes must be
     * aggressively forgotten to avoid reachability of everything any
     * node has ever referred to since arrival.
     */

    /**
     * Shared internal API for dual stacks and queues.
     */
    abstract static class Transferer<E> {
        /**
         * Performs a put or take.
         *
         * @param e     if non-null, the item to be handed to a consumer;
         *              if null, requests that transfer return an item
         *              offered by producer.
         * @param timed if this operation should timeout
         * @param nanos the timeout, in nanoseconds
         * @param e     可以为null，null时表示这个请求是一个 REQUEST 类型的请求   (消费)
         *              如果不是null，说明这个请求是一个 DATA 类型的请求。（入队）
         * @param timed 如果为true 表示指定了超时时间 ,如果为false 表示不支持超时，表示当前请求一直等待到匹配为止，或者被中断。
         * @param nanos 超时时间限制 单位 纳秒
         * @return E 如果当前请求是一个 REQUEST类型的请求，返回值如果不为 null 表示 匹配成功；
         * 如果返回null，表示REQUEST类型的请求超时 或 被中断。
         * 如果当前请求是一个 DATA 类型的请求，返回值如果不为 null 表示 匹配成功，返回当前线程put的数据。
         * 如果返回值为null 表示，DATA类型的请求超时 或者 被中断..都会返回Null
         */
        abstract E transfer(E e, boolean timed, long nanos);
    }


    //为什么需要自旋这个操作？
    //因为线程 挂起 唤醒站在cpu角度去看的话，是非常耗费资源的，涉及到用户态和内核态的切换...
    //自旋的好处，自旋期间线程会一直检查自己的状态是否被匹配到，如果自旋期间被匹配到，那么直接就返回了
    //如果自旋期间未被匹配到，自旋次数达到某个指标后，还是会将当前线程挂起的...
    //NCPUS：当一个平台只有一个CPU时，你觉得还需要自旋么？
    //答：肯定不需要自旋了，因为一个cpu同一时刻只能执行一个线程，自旋没有意义了...而且你还站着cpu 其它线程没办法执行..这个
    //栈的状态更不会改变了.. 当只有一个cpu时 会直接选择 LockSupport.park() 挂起等待者线程。

    /**
     * The number of CPUs, for spin control
     */
    static final int NCPUS = Runtime.getRuntime().availableProcessors();

    /**
     * The number of times to spin before blocking in timed waits.
     * The value is empirically derived -- it works well across a
     * variety of processors and OSes. Empirically, the best value
     * seems not to vary with number of CPUs (beyond 2) so is just
     * a constant.
     * //表示指定超时时间的话，当前线程最大自旋次数。
     * //只有一个cpu 自旋次数为0
     * //当cpu大于1时，说明当前平台是多核平台，那么指定超时时间的请求的最大自旋次数是 32 次。
     * //32是一个经验值。
     */
    static final int maxTimedSpins = (NCPUS < 2) ? 0 : 32;

    /**
     * The number of times to spin before blocking in untimed waits.
     * This is greater than timed value because untimed waits spin
     * faster since they don't need to check times on each spin.
     * //表示未指定超时限制的话，线程等待匹配时，自旋次数。
     * //是指定超时限制的请求的自旋次数的16倍.
     */
    static final int maxUntimedSpins = maxTimedSpins * 16;

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices.
     * //如果请求是指定超时限制的话，如果超时nanos参数是< 1000 纳秒时，
     * //禁止挂起。挂起再唤醒的成本太高了..还不如选择自旋空转呢...
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * Dual stack
     * 非公平模式实现的同步队列，内部数据结构是 “栈”
     */
    static final class TransferStack<E> extends Transferer<E> {
        /*
         * This extends Scherer-Scott dual stack algorithm, differing,
         * among other ways, by using "covering" nodes rather than
         * bit-marked pointers: Fulfilling operations push on marker
         * nodes (with FULFILLING bit set in mode) to reserve a spot
         * to match a waiting node.
         */

        /* Modes for SNodes, ORed together in node fields */
        /**
         * Node represents an unfulfilled consumer
         * 表示Node类型为 请求类型
         */
        static final int REQUEST = 0;
        /**
         * Node represents an unfulfilled producer
         * 表示Node类型为 数据类型
         */
        static final int DATA = 1;
        /**
         * Node is fulfilling another unfulfilled DATA or REQUEST
         * 表示Node类型为 匹配中类型
         * 假设栈顶元素为 REQUEST-NODE，当前请求类型为 DATA的话，入栈会修改类型为 FULFILLING 【栈顶 & 栈顶之下的一个node】
         * 假设栈顶元素为 DATA-NODE，当前请求类型为 REQUEST的话，入栈会修改类型为 FULFILLING 【栈顶 & 栈顶之下的一个node】
         */
        static final int FULFILLING = 2;

        /**
         * Returns true if m has fulfilling bit set.
         * 判断当前模式是否为 匹配中状态。
         */
        static boolean isFulfilling(int m) {
            return (m & FULFILLING) != 0;
        }

        /**
         * Node class for TransferStacks.
         */
        static final class SNode {
            //指向下一个栈帧
            volatile SNode next;        // next node in stack
            //与当前node匹配的节点
            volatile SNode match;       // the node matched to this
            //假设当前node对应的线程 自旋期间未被匹配成功，那么node对应的线程需要挂起，挂起前 waiter 保存对应的线程引用，
            //方便 匹配成功后，被唤醒。
            volatile Thread waiter;     // to control park/unpark
            //数据域，data不为空 表示当前Node对应的请求类型为 DATA类型。 反之则表示Node为 REQUEST类型。
            Object item;                // data; or null for REQUESTs
            //表示当前Node的模式 【DATA/REQUEST/FULFILLING】
            int mode;
            // Note: item and mode fields don't need to be volatile
            // since they are always written before, and read after,
            // other volatile/atomic operations.

            SNode(Object item) {
                this.item = item;
            }

            //CAS方式设置Node对象的next字段。
            boolean casNext(SNode cmp, SNode val) {
                //优化：cmp == next  为什么要判断？
                //因为cas指令 在平台执行时，同一时刻只能有一个cas指令被执行。（重点）
                //有了java层面的这一次判断，可以提升一部分性能。 cmp == next 不相等，就没必要走 cas指令。
                return cmp == next && UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
            }

            /**
             * Tries to match node s to this node, if so, waking up thread.
             * Fulfillers call tryMatch to identify their waiters.
             * Waiters block until they have been matched.
             *
             * @param s the node to match
             * @return ture 匹配成功。 否则匹配失败..
             */
            boolean tryMatch(SNode s) {
                //条件一：match == null 成立，说明当前Node尚未与任何节点发生过匹配...
                //条件二 成立：使用CAS方式 设置match字段，表示当前Node已经被匹配了
                if (match == null && UNSAFE.compareAndSwapObject(this, matchOffset, null, s)) {
                    //当前Node如果自旋结束，那么会使用LockSupport.park 方法挂起，挂起之前会将Node对应的Thread 保留到 waiter字段。
                    Thread w = waiter;
                    //条件成立：说明Node对应的Thread已经挂起了...
                    if (w != null) {    // waiters need at most one unpark
                        waiter = null;
                        //使用unpark方式唤醒。
                        LockSupport.unpark(w);
                    }
                    return true;
                }
                return match == s;
            }

            /**
             * Tries to cancel a wait by matching node to itself.
             * 尝试取消..
             */
            void tryCancel() {
                //match字段 保留当前Node对象本身，表示这个Node是取消状态，取消状态的Node，最终会被 强制 移除出栈。
                UNSAFE.compareAndSwapObject(this, matchOffset, null, this);
            }

            //如果match保留的是当前Node本身，那表示当前Node是取消状态，反之 则 非取消状态。
            boolean isCancelled() {
                return match == this;
            }

            // Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
            private static final long matchOffset;
            private static final long nextOffset;

            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> k = SNode.class;
                    matchOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("match"));
                    nextOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("next"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }

        /**
         * The head (top) of the stack
         * //表示栈顶指针
         */
        volatile SNode head;

        //设置栈顶元素
        //h 旧头节点
        boolean casHead(SNode h, SNode nh) {
            return h == head && UNSAFE.compareAndSwapObject(this, headOffset, h, nh);
        }

        /**
         * Creates or resets fields of a node. Called only from transfer
         * where the node to push on stack is lazily created and
         * reused when possible to help reduce intervals between reads
         * and CASes of head and to avoid surges of garbage when CASes
         * to push nodes fail due to contention.
         *
         * @param s    SNode引用，当这个引用指向空时，snode 方法会创建一个SNode对象 并且赋值给这个引用
         * @param e    SNode对象的item字段
         * @param next 指向当前栈帧的下一个栈帧
         * @param mode REQUEST/DATA/FULFILLING
         */
        static SNode snode(SNode s, Object e, SNode next, int mode) {
            if (s == null) s = new SNode(e);
            s.mode = mode;
            s.next = next;
            return s;
        }

        /**
         * Puts or takes an item.
         */
        @SuppressWarnings("unchecked")
        E transfer(E e, boolean timed, long nanos) {
            /*
             * Basic algorithm is to loop trying one of three actions:
             *
             * 1. If apparently empty or already containing nodes of same
             *    mode, try to push node on stack and wait for a match,
             *    returning it, or null if cancelled.
             *
             * 2. If apparently containing node of complementary mode,
             *    try to push a fulfilling node on to stack, match
             *    with corresponding waiting node, pop both from
             *    stack, and return matched item. The matching or
             *    unlinking might not actually be necessary because of
             *    other threads performing action 3:
             *
             * 3. If top of stack already holds another fulfilling node,
             *    help it out by doing its match and/or pop
             *    operations, and then continue. The code for helping
             *    is essentially the same as for fulfilling, except
             *    that it doesn't return the item.
             */

            //包装当前线程的Node
            SNode s = null; // constructed/reused as needed
            //e == null 条件成立：当前线程是一个 REQUEST 线程。
            //否则 e!=null 说明 当前线程是一个 DATA 线程，提交数据的线程。
            int mode = (e == null) ? REQUEST : DATA;

            //自旋
            for (; ; ) {
                // h 表示栈顶指针
                SNode h = head;

                //CASE1：当前栈内为空 或者 栈顶Node模式与当前请求模式一致，都是需要做入栈操作。
                if (h == null || h.mode == mode) {  // empty or same-mode
                    //条件一：成立，说明当前请求是 指定了 超时限制的
                    //条件二：nanos <= 0 , nanos == 0. 表示这个请求 不支持 “阻塞等待”。比如： queue.offer();
                    if (timed && nanos <= 0) {      // can't wait
                        //条件成立：栈顶不为空，说明栈顶已经取消状态了，协助栈顶出栈。
                        if (h != null && h.isCancelled())
                            //更新栈顶next指针  指向 下一个元素
                            casHead(h, h.next);     // pop cancelled node
                        else
                            //大部分情况从这里返回。
                            return null;
                    }
                    //什么时候执行else if 呢？
                    //当前栈顶为空 或者 模式与当前请求一致，且当前请求允许 阻塞等待
                    //casHead(h, s = snode(s, e, h, mode))  入栈操作
                    else if (casHead(h, s = snode(s, e, h, mode))) {
                        //执行到这里，说明 当前请求入栈成功。
                        //入栈成功之后要做什么呢？
                        //在栈内等待一个好消息，等待被匹配！

                        //awaitFulfill  在栈内等待被匹配的逻辑...
                        //1.正常情况：返回匹配的节点
                        //2.取消情况：返回当前节点  s节点进去，返回s节点...
                        SNode m = awaitFulfill(s, timed, nanos);
                        //条件成立：说明当前Node状态是 取消状态...
                        if (m == s) {               // wait was cancelled
                            //将取消状态的节点 出栈...
                            clean(s);
                            //取消状态 最终返回null
                            return null;
                        }

                        //执行到这里 说明当前Node已经被匹配了...

                        //条件一：成立，说明栈顶是有Node
                        //条件二：成立，说明 Fulfill 和 当前Node 还未出栈，需要协助出栈。
                        if ((h = head) != null && h.next == s)
                            //将fulfill 和 当前Node 结对 出栈
                            casHead(h, s.next);     // help s's fulfiller

                        // 当前NODE模式为REQUEST类型：返回匹配节点的m.item 数据域
                        //当前NODE模式为DATA类型：返回Node.item 数据域，当前请求提交的 数据e
                        return (E) ((mode == REQUEST) ? m.item : s.item);
                    }
                }

                //什么时候来到这？？
                //栈顶Node的模式与当前请求的模式不一致，会执行else if 的条件。
                //栈顶是 (DATA  Reqeust)    (Request   DATA)   (FULFILLING  REQUEST/DATA)
                //CASE2：当前栈顶元素模式与请求元素模式不一致，且栈顶不是FULFILLING
                else if (!isFulfilling(h.mode)) { // try to fulfill
                    if (h.isCancelled())            // already cancelled
                        casHead(h, h.next);         // pop and retry

                        //条件成立：说明压栈节点成功，入栈一个 FULFILLING | mode  NODE
                    else if (casHead(h, s = snode(s, e, h, FULFILLING | mode))) {
                        //当前请求入栈成功

                        //自旋，fulfill 节点 和 fulfill.next 节点进行匹配工作...
                        for (; ; ) { // loop until matched or waiters disappear
                            // m 与当前s 匹配节点。
                            SNode m = s.next;       // m is s's match
                            //m == null 什么时候可能成立呢？
                            //当s.next节点 超时或者被外部线程中断唤醒后，会执行 clean 操作 将 自己清理出栈，此时
                            //站在匹配者线程 来看，真有可能拿到一个null。
                            if (m == null) {        // all waiters are gone
                                //将整个栈清空。
                                casHead(s, null);   // pop fulfill node
                                s = null;           // use new node next time
                                //回到外层大的 自旋中，再重新选择路径执行，此时有可能 插入一个节点。
                                break;                     // restart main loop
                            }
                            //什么时候会执行到这里呢？
                            //fulfilling 匹配节点不为null，进行真正的匹配工作。

                            //获取 匹配节点的 下一个节点。
                            SNode mn = m.next;
                            //尝试匹配，匹配成功，则将fulfilling 和 m 一起出栈。
                            if (m.tryMatch(s)) {
                                //结对出栈
                                casHead(s, mn);     // pop both s and m

                                //当前NODE模式为REQUEST类型：返回匹配节点的m.item 数据域
                                //当前NODE模式为DATA类型：返回Node.item 数据域，当前请求提交的 数据e
                                return (E) ((mode == REQUEST) ? m.item : s.item);
                            } else                  // lost match
                                //强制出栈
                                s.casNext(m, mn);   // help unlink
                        }
                    }
                }

                //CASE3：什么时候会执行？
                //栈顶模式为 FULFILLING模式，表示栈顶和栈顶下面的栈帧正在发生匹配...
                //当前请求需要做 协助 工作。
                else {                            // help a fulfiller
                    //h 表示的是 fulfilling节点,m fulfilling匹配的节点。
                    SNode m = h.next;               // m is h's match
                    //m == null 什么时候可能成立呢？
                    //当s.next节点 超时或者被外部线程中断唤醒后，会执行 clean 操作 将 自己清理出栈，此时
                    //站在匹配者线程 来看，真有可能拿到一个null。
                    if (m == null)                  // waiter is gone
                        //清空栈
                        casHead(h, null);           // pop fulfilling node

                        //大部分情况：走else分支。
                    else {
                        //获取栈顶匹配节点的 下一个节点
                        SNode mn = m.next;
                        //条件成立：说明 m 和 栈顶 匹配成功
                        if (m.tryMatch(h))          // help match
                            //双双出栈，让栈顶指针指向 匹配节点的下一个节点。
                            casHead(h, mn);         // pop both h and m
                        else                        // lost match
                            //强制出栈
                            h.casNext(m, mn);       // help unlink
                    }
                }
            }
        }

        /**
         * Spins/blocks until node s is matched by a fulfill operation.
         *
         * @param s     the waiting node
         * @param timed true if timed wait
         * @param nanos timeout value
         * @return matched node, or s if cancelled
         * * @param s 当前请求Node
         * * @param timed 当前请求是否支持 超时限制
         * * @param nanos 如果请求支持超时限制，nanos 表示超时等待时长。
         */
        SNode awaitFulfill(SNode s, boolean timed, long nanos) {

            //等待的截止时间。  timed == true  =>  System.nanoTime() + nanos
            final long deadline = timed ? System.nanoTime() + nanos : 0L;

            //获取当前请求线程..
            Thread w = Thread.currentThread();

            //spins 表示当前请求线程 在 下面的 for(;;) 自旋检查中，自旋次数。 如果达到spins自旋次数时，当前线程对应的Node 仍然未被匹配成功，
            //那么再选择 挂起 当前请求线程
            int spins = (shouldSpin(s) ?
                    //timed == true 指定了超时限制的，这个时候采用 maxTimedSpins == 32 ,否则采用 32 * 16
                    (timed ? maxTimedSpins : maxUntimedSpins) : 0);

            //自旋检查逻辑：1.是否匹配  2.是否超时  3.是否被中断..
            for (; ; ) {

                //条件成立：说明当前线程收到中断信号，需要设置Node状态为 取消状态。
                if (w.isInterrupted())
                    //Node对象的 match 指向 当前Node 说明该Node状态就是 取消状态。
                    s.tryCancel();

                //m 表示与当前Node匹配的节点。
                //1.正常情况：有一个请求 与 当前Node 匹配成功，这个时候 s.match 指向 匹配节点。
                //2.取消情况：当前match 指向 当前Node...
                SNode m = s.match;

                if (m != null)
                    //可能正常 也可能是 取消...
                    return m;

                //条件成立：说明指定了超时限制..
                if (timed) {
                    //nanos 表示距离超时 还有多少纳秒..
                    nanos = deadline - System.nanoTime();
                    //条件成立：说明已经超时了...
                    if (nanos <= 0L) {
                        // 设置当前Node状态为 取消状态.. match-->当前Node
                        s.tryCancel();
                        continue;
                    }
                }

                //条件成立：说明当前线程还可以进行自旋检查...
                if (spins > 0)
                    //自旋次数 累积 递减
                    spins = shouldSpin(s) ? (spins - 1) : 0;

                    //spins == 0 ，已经不允许再进行自旋检查了
                else if (s.waiter == null)
                    //把当前Node对应的Thread 保存到 Node.waiter字段中..
                    s.waiter = w; // establish waiter so can park next iter

                    //条件成立：说明当前Node对应的请求  未指定超时限制。
                else if (!timed)
                    //使用不指定超时限制的park方法 挂起当前线程，直到 当前线程被外部线程 使用unpark唤醒。
                    LockSupport.park(this);

                    //什么时候执行到这里？ timed == true 设置了 超时限制..
                    //条件成立：nanos > 1000 纳秒的值，只有这种情况下，才允许挂起当前线程..
                    // 否则 说明 超时给的太少了...挂起和唤醒的成本 远大于 空转自旋...
                else if (nanos > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanos);
            }
        }

        /**
         * Returns true if node s is at head or there is an active
         * fulfiller.
         */
        boolean shouldSpin(SNode s) {
            //获取栈顶
            SNode h = head;
            //条件一 h == s ：条件成立 说明当前 s 就是栈顶，允许自旋检查...
            //条件二 h == null : 什么时候成立？ 当前 s节点 自旋检查期间，又来了一个 与当前 s 节点匹配的请求，双双出栈了...条件会成立。
            //条件三 isFulfilling(h.mode) ： 前提 当前 s 不是 栈顶元素；并且当前栈顶正在匹配中，这种状态 栈顶下面的元素，都允许自旋检查。
            return (h == s || h == null || isFulfilling(h.mode));
        }

        /**
         * Unlinks s from the stack.
         */
        void clean(SNode s) {
            s.item = null;   // forget item
            s.waiter = null; // forget thread

            /*
             * At worst we may need to traverse entire stack to unlink
             * s. If there are multiple concurrent calls to clean, we
             * might not see s if another thread has already removed
             * it. But we can stop when we see any node known to
             * follow s. We use s.next unless it too is cancelled, in
             * which case we try the node one past. We don't check any
             * further because we don't want to doubly traverse just to
             * find sentinel.
             */

            SNode past = s.next;
            if (past != null && past.isCancelled())
                past = past.next;

            // Absorb cancelled nodes at head
            SNode p;
            while ((p = head) != null && p != past && p.isCancelled())
                casHead(p, p.next);

            // Unsplice embedded nodes
            while (p != null && p != past) {
                SNode n = p.next;
                if (n != null && n.isCancelled())
                    p.casNext(n, n.next);
                else
                    p = n;
            }
        }

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long headOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = TransferStack.class;
                headOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("head"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * Dual Queue
     */
    static final class TransferQueue<E> extends Transferer<E> {
        /*
         * This extends Scherer-Scott dual queue algorithm, differing,
         * among other ways, by using modes within nodes rather than
         * marked pointers. The algorithm is a little simpler than
         * that for stacks because fulfillers do not need explicit
         * nodes, and matching is done by CAS'ing QNode.item field
         * from non-null to null (for put) or vice versa (for take).
         */

        /**
         * Node class for TransferQueue.
         */
        static final class QNode {
            volatile QNode next;          // next node in queue
            volatile Object item;         // CAS'ed to or from null
            volatile Thread waiter;       // to control park/unpark
            final boolean isData;

            QNode(Object item, boolean isData) {
                this.item = item;
                this.isData = isData;
            }

            boolean casNext(QNode cmp, QNode val) {
                return next == cmp &&
                        UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
            }

            boolean casItem(Object cmp, Object val) {
                return item == cmp &&
                        UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
            }

            /**
             * Tries to cancel by CAS'ing ref to this as item.
             */
            void tryCancel(Object cmp) {
                UNSAFE.compareAndSwapObject(this, itemOffset, cmp, this);
            }

            boolean isCancelled() {
                return item == this;
            }

            /**
             * Returns true if this node is known to be off the queue
             * because its next pointer has been forgotten due to
             * an advanceHead operation.
             */
            boolean isOffList() {
                return next == this;
            }

            // Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
            private static final long itemOffset;
            private static final long nextOffset;

            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> k = QNode.class;
                    itemOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("item"));
                    nextOffset = UNSAFE.objectFieldOffset
                            (k.getDeclaredField("next"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }

        /**
         * Head of queue
         */
        transient volatile QNode head;
        /**
         * Tail of queue
         */
        transient volatile QNode tail;
        /**
         * Reference to a cancelled node that might not yet have been
         * unlinked from queue because it was the last inserted node
         * when it was cancelled.
         */
        transient volatile QNode cleanMe;

        TransferQueue() {
            QNode h = new QNode(null, false); // initialize to dummy node.
            head = h;
            tail = h;
        }

        /**
         * Tries to cas nh as new head; if successful, unlink
         * old head's next node to avoid garbage retention.
         */
        void advanceHead(QNode h, QNode nh) {
            if (h == head &&
                    UNSAFE.compareAndSwapObject(this, headOffset, h, nh))
                h.next = h; // forget old next
        }

        /**
         * Tries to cas nt as new tail.
         */
        void advanceTail(QNode t, QNode nt) {
            if (tail == t)
                UNSAFE.compareAndSwapObject(this, tailOffset, t, nt);
        }

        /**
         * Tries to CAS cleanMe slot.
         */
        boolean casCleanMe(QNode cmp, QNode val) {
            return cleanMe == cmp &&
                    UNSAFE.compareAndSwapObject(this, cleanMeOffset, cmp, val);
        }

        /**
         * Puts or takes an item.
         */
        @SuppressWarnings("unchecked")
        E transfer(E e, boolean timed, long nanos) {
            /* Basic algorithm is to loop trying to take either of
             * two actions:
             *
             * 1. If queue apparently empty or holding same-mode nodes,
             *    try to add node to queue of waiters, wait to be
             *    fulfilled (or cancelled) and return matching item.
             *
             * 2. If queue apparently contains waiting items, and this
             *    call is of complementary mode, try to fulfill by CAS'ing
             *    item field of waiting node and dequeuing it, and then
             *    returning matching item.
             *
             * In each case, along the way, check for and try to help
             * advance head and tail on behalf of other stalled/slow
             * threads.
             *
             * The loop starts off with a null check guarding against
             * seeing uninitialized head or tail values. This never
             * happens in current SynchronousQueue, but could if
             * callers held non-volatile/final ref to the
             * transferer. The check is here anyway because it places
             * null checks at top of loop, which is usually faster
             * than having them implicitly interspersed.
             */

            QNode s = null; // constructed/reused as needed
            boolean isData = (e != null);

            for (; ; ) {
                QNode t = tail;
                QNode h = head;
                if (t == null || h == null)         // saw uninitialized value
                    continue;                       // spin

                if (h == t || t.isData == isData) { // empty or same-mode
                    QNode tn = t.next;
                    if (t != tail)                  // inconsistent read
                        continue;
                    if (tn != null) {               // lagging tail
                        advanceTail(t, tn);
                        continue;
                    }
                    if (timed && nanos <= 0)        // can't wait
                        return null;
                    if (s == null)
                        s = new QNode(e, isData);
                    if (!t.casNext(null, s))        // failed to link in
                        continue;

                    advanceTail(t, s);              // swing tail and wait
                    Object x = awaitFulfill(s, e, timed, nanos);
                    if (x == s) {                   // wait was cancelled
                        clean(t, s);
                        return null;
                    }

                    if (!s.isOffList()) {           // not already unlinked
                        advanceHead(t, s);          // unlink if head
                        if (x != null)              // and forget fields
                            s.item = s;
                        s.waiter = null;
                    }
                    return (x != null) ? (E) x : e;

                } else {                            // complementary-mode
                    QNode m = h.next;               // node to fulfill
                    if (t != tail || m == null || h != head)
                        continue;                   // inconsistent read

                    Object x = m.item;
                    if (isData == (x != null) ||    // m already fulfilled
                            x == m ||                   // m cancelled
                            !m.casItem(x, e)) {         // lost CAS
                        advanceHead(h, m);          // dequeue and retry
                        continue;
                    }

                    advanceHead(h, m);              // successfully fulfilled
                    LockSupport.unpark(m.waiter);
                    return (x != null) ? (E) x : e;
                }
            }
        }

        /**
         * Spins/blocks until node s is fulfilled.
         *
         * @param s     the waiting node
         * @param e     the comparison value for checking match
         * @param timed true if timed wait
         * @param nanos timeout value
         * @return matched item, or s if cancelled
         */
        Object awaitFulfill(QNode s, E e, boolean timed, long nanos) {
            /* Same idea as TransferStack.awaitFulfill */
            final long deadline = timed ? System.nanoTime() + nanos : 0L;
            Thread w = Thread.currentThread();
            int spins = ((head.next == s) ?
                    (timed ? maxTimedSpins : maxUntimedSpins) : 0);
            for (; ; ) {
                if (w.isInterrupted())
                    s.tryCancel(e);
                Object x = s.item;
                if (x != e)
                    return x;
                if (timed) {
                    nanos = deadline - System.nanoTime();
                    if (nanos <= 0L) {
                        s.tryCancel(e);
                        continue;
                    }
                }
                if (spins > 0)
                    --spins;
                else if (s.waiter == null)
                    s.waiter = w;
                else if (!timed)
                    LockSupport.park(this);
                else if (nanos > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanos);
            }
        }

        /**
         * Gets rid of cancelled node s with original predecessor pred.
         */
        void clean(QNode pred, QNode s) {
            s.waiter = null; // forget thread
            /*
             * At any given time, exactly one node on list cannot be
             * deleted -- the last inserted node. To accommodate this,
             * if we cannot delete s, we save its predecessor as
             * "cleanMe", deleting the previously saved version
             * first. At least one of node s or the node previously
             * saved can always be deleted, so this always terminates.
             */
            while (pred.next == s) { // Return early if already unlinked
                QNode h = head;
                QNode hn = h.next;   // Absorb cancelled first node as head
                if (hn != null && hn.isCancelled()) {
                    advanceHead(h, hn);
                    continue;
                }
                QNode t = tail;      // Ensure consistent read for tail
                if (t == h)
                    return;
                QNode tn = t.next;
                if (t != tail)
                    continue;
                if (tn != null) {
                    advanceTail(t, tn);
                    continue;
                }
                if (s != t) {        // If not tail, try to unsplice
                    QNode sn = s.next;
                    if (sn == s || pred.casNext(s, sn))
                        return;
                }
                QNode dp = cleanMe;
                if (dp != null) {    // Try unlinking previous cancelled node
                    QNode d = dp.next;
                    QNode dn;
                    if (d == null ||               // d is gone or
                            d == dp ||                 // d is off list or
                            !d.isCancelled() ||        // d not cancelled or
                            (d != t &&                 // d not tail and
                                    (dn = d.next) != null &&  //   has successor
                                    dn != d &&                //   that is on list
                                    dp.casNext(d, dn)))       // d unspliced
                        casCleanMe(dp, null);
                    if (dp == pred)
                        return;      // s is already saved node
                } else if (casCleanMe(null, pred))
                    return;          // Postpone cleaning s
            }
        }

        private static final sun.misc.Unsafe UNSAFE;
        private static final long headOffset;
        private static final long tailOffset;
        private static final long cleanMeOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = TransferQueue.class;
                headOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("head"));
                tailOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("tail"));
                cleanMeOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("cleanMe"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * The transferer. Set only in constructor, but cannot be declared
     * as final without further complicating serialization.  Since
     * this is accessed only at most once per public method, there
     * isn't a noticeable performance penalty for using volatile
     * instead of final here.
     */
    private transient volatile Transferer<E> transferer;

    /**
     * Creates a {@code SynchronousQueue} with nonfair access policy.
     */
    public SynchronousQueue() {
        this(false);
    }

    /**
     * Creates a {@code SynchronousQueue} with the specified fairness policy.
     *
     * @param fair if true, waiting threads contend in FIFO order for
     *             access; otherwise the order is unspecified.
     */
    public SynchronousQueue(boolean fair) {
        transferer = fair ? new TransferQueue<E>() : new TransferStack<E>();
    }

    /**
     * Adds the specified element to this queue, waiting if necessary for
     * another thread to receive it.
     *
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        if (transferer.transfer(e, false, 0) == null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * up to the specified wait time for another thread to receive it.
     *
     * @return {@code true} if successful, or {@code false} if the
     * specified waiting time elapses before a consumer appears
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean offer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {
        if (e == null) throw new NullPointerException();
        if (transferer.transfer(e, true, unit.toNanos(timeout)) != null)
            return true;
        if (!Thread.interrupted())
            return false;
        throw new InterruptedException();
    }

    /**
     * Inserts the specified element into this queue, if another thread is
     * waiting to receive it.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        return transferer.transfer(e, true, 0) != null;
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * for another thread to insert it.
     *
     * @return the head of this queue
     * @throws InterruptedException {@inheritDoc}
     */
    public E take() throws InterruptedException {
        E e = transferer.transfer(null, false, 0);
        if (e != null)
            return e;
        Thread.interrupted();
        throw new InterruptedException();
    }

    /**
     * Retrieves and removes the head of this queue, waiting
     * if necessary up to the specified wait time, for another thread
     * to insert it.
     *
     * @return the head of this queue, or {@code null} if the
     * specified waiting time elapses before an element is present
     * @throws InterruptedException {@inheritDoc}
     */
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = transferer.transfer(null, true, unit.toNanos(timeout));
        if (e != null || !Thread.interrupted())
            return e;
        throw new InterruptedException();
    }

    /**
     * Retrieves and removes the head of this queue, if another thread
     * is currently making an element available.
     *
     * @return the head of this queue, or {@code null} if no
     * element is available
     */
    public E poll() {
        return transferer.transfer(null, true, 0);
    }

    /**
     * Always returns {@code true}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @return {@code true}
     */
    public boolean isEmpty() {
        return true;
    }

    /**
     * Always returns zero.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @return zero
     */
    public int size() {
        return 0;
    }

    /**
     * Always returns zero.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @return zero
     */
    public int remainingCapacity() {
        return 0;
    }

    /**
     * Does nothing.
     * A {@code SynchronousQueue} has no internal capacity.
     */
    public void clear() {
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param o the element
     * @return {@code false}
     */
    public boolean contains(Object o) {
        return false;
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param o the element to remove
     * @return {@code false}
     */
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Returns {@code false} unless the given collection is empty.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param c the collection
     * @return {@code false} unless given collection is empty
     */
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param c the collection
     * @return {@code false}
     */
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Always returns {@code false}.
     * A {@code SynchronousQueue} has no internal capacity.
     *
     * @param c the collection
     * @return {@code false}
     */
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Always returns {@code null}.
     * A {@code SynchronousQueue} does not return elements
     * unless actively waited on.
     *
     * @return {@code null}
     */
    public E peek() {
        return null;
    }

    /**
     * Returns an empty iterator in which {@code hasNext} always returns
     * {@code false}.
     *
     * @return an empty iterator
     */
    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    /**
     * Returns an empty spliterator in which calls to
     * {@link java.util.Spliterator#trySplit()} always return {@code null}.
     *
     * @return an empty spliterator
     * @since 1.8
     */
    public Spliterator<E> spliterator() {
        return Spliterators.emptySpliterator();
    }

    /**
     * Returns a zero-length array.
     *
     * @return a zero-length array
     */
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * Sets the zeroeth element of the specified array to {@code null}
     * (if the array has non-zero length) and returns it.
     *
     * @param a the array
     * @return the specified array
     * @throws NullPointerException if the specified array is null
     */
    public <T> T[] toArray(T[] a) {
        if (a.length > 0)
            a[0] = null;
        return a;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        for (E e; (e = poll()) != null; ) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        for (E e; n < maxElements && (e = poll()) != null; ) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /*
     * To cope with serialization strategy in the 1.5 version of
     * SynchronousQueue, we declare some unused classes and fields
     * that exist solely to enable serializability across versions.
     * These fields are never used, so are initialized only if this
     * object is ever serialized or deserialized.
     */

    @SuppressWarnings("serial")
    static class WaitQueue implements java.io.Serializable {
    }

    static class LifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3633113410248163686L;
    }

    static class FifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3623113410248163686L;
    }

    private ReentrantLock qlock;
    private WaitQueue waitingProducers;
    private WaitQueue waitingConsumers;

    /**
     * Saves this queue to a stream (that is, serializes it).
     *
     * @param s the stream
     * @throws java.io.IOException if an I/O error occurs
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        boolean fair = transferer instanceof TransferQueue;
        if (fair) {
            qlock = new ReentrantLock(true);
            waitingProducers = new FifoWaitQueue();
            waitingConsumers = new FifoWaitQueue();
        } else {
            qlock = new ReentrantLock();
            waitingProducers = new LifoWaitQueue();
            waitingConsumers = new LifoWaitQueue();
        }
        s.defaultWriteObject();
    }

    /**
     * Reconstitutes this queue from a stream (that is, deserializes it).
     *
     * @param s the stream
     * @throws ClassNotFoundException if the class of a serialized object
     *                                could not be found
     * @throws java.io.IOException    if an I/O error occurs
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (waitingProducers instanceof FifoWaitQueue)
            transferer = new TransferQueue<E>();
        else
            transferer = new TransferStack<E>();
    }

    // Unsafe mechanics
    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
        try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
        }
    }

}
