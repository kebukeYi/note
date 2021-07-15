package java.util.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import sun.misc.Unsafe;

/**
 * Provides a framework for implementing blocking locks and related
 * synchronizers (semaphores, events, etc) that rely on（依赖于先进先出队列）
 * first-in-first-out (FIFO) wait queues.  This class is designed to
 * be a useful basis for most kinds of synchronizers that rely on a
 * single atomic {@code int} value to represent state. Subclasses
 * must define the protected methods that change this state, and which
 * define what that state means in terms of this object being acquired
 * or released.  通过给定的int 值用来表示状态，该状态用于表示锁匙获取还是释放的；通过给定的方法改变状态的值。
 * Given these, the other methods in this class carry
 * out all queuing and blocking mechanics.
 * Subclasses can maintain other state fields, but only the atomically updated {@code int}
 * value manipulated using methods {@link #getState}, {@link #setState} and {@link #compareAndSetState} is tracked with respect
 * to synchronization.
 *
 * <p>Subclasses should be defined as non-public internal helper
 * classes that are used to implement the synchronization properties
 * of their enclosing class.  Class
 * {@code AbstractQueuedSynchronizer} does not implement any
 * synchronization interface.  Instead it defines methods such as
 * {@link #acquireInterruptibly} that can be invoked as
 * appropriate by concrete locks and related synchronizers to
 * implement their public methods.子类一般使用内部类用来实现需要的同步的工作
 *
 * <p>This class supports either or both a default <em>exclusive</em>
 * mode and a <em>shared</em> mode. 有两种模式：独占和共享
 * When acquired in exclusive mode,
 * attempted acquires by other threads cannot succeed. Shared mode
 * acquires by multiple threads may (but need not) succeed. This class
 * does not &quot;understand&quot; these differences except in the
 * mechanical sense that when a shared mode acquire succeeds, the next
 * waiting thread (if one exists) must also determine whether it can
 * acquire as well. Threads waiting in the different modes share the
 * same FIFO queue. Usually, implementation subclasses support only
 * one of these modes, but both can come into play for example in a
 * {@link ReadWriteLock}. Subclasses that support only exclusive or
 * only shared modes need not define the methods supporting the unused mode.
 * 这个类提供了两种模式:独占和共享,一般地之类会只有一种模式,但 ReadWriteLocki两种模式是并存,子类不需要再定义别的方法来实现这两种模式
 *
 * <p>This class defines a nested {@link ConditionObject} class that
 * can be used as a {@link Condition} implementation by subclasses
 * supporting exclusive mode for which method {@link #isHeldExclusively} reports whether synchronization is exclusively
 * held with respect to the current thread, method {@link #release}
 * invoked with the current {@link #getState} value fully releases
 * this object, and {@link #acquire}, given this saved state value,
 * eventually restores this object to its previous acquired state.  No
 * {@code AbstractQueuedSynchronizer} method otherwise creates such a
 * condition, so if this constraint cannot be met, do not use it.  The
 * behavior of {@link ConditionObject} depends of course on the
 * semantics of its synchronizer implementation.
 *
 * <p>This class provides inspection, instrumentation, and monitoring
 * methods for the internal queue, as well as similar methods for
 * condition objects. These can be exported as desired into classes
 * using an {@code AbstractQueuedSynchronizer} for their
 * synchronization mechanics.
 *
 * <p>Serialization of this class stores only the underlying atomic
 * integer maintaining state, so deserialized objects have empty
 * thread queues. Typical subclasses requiring serializability will
 * define a {@code readObject} method that restores this to a known
 * initial state upon deserialization.
 * 序列化相关:子类一股需要定
 * 义 readojbect,方法来记住已经
 * 初始化的记录(否则反序列化,
 * 这些状态会丢失)
 *
 * <h3>Usage</h3>
 *
 * <p>To use this class as the basis of a synchronizer, redefine the
 * following methods, as applicable, by inspecting and/or modifying
 * the synchronization state using {@link #getState}, {@link
 * #setState} and/or {@link #compareAndSetState}:
 *
 * <ul>
 * <li> {@link #tryAcquire}
 * <li> {@link #tryRelease}
 * <li> {@link #tryAcquireShared}
 * <li> {@link #tryReleaseShared}
 * <li> {@link #isHeldExclusively}
 * </ul>
 * <p>
 * Each of these methods by default throws {@link
 * UnsupportedOperationException}.  Implementations of these methods
 * must be internally thread-safe, and should in general be short and
 * not block. Defining these methods is the <em>only</em> supported
 * means of using this class. All other methods are declared
 * {@code final} because they cannot be independently varied.
 *
 * <p>You may also find the inherited methods from {@link
 * AbstractOwnableSynchronizer} useful to keep track of the thread
 * owning an exclusive synchronizer.  You are encouraged to use them
 * -- this enables monitoring and diagnostic tools to assist users in
 * determining which threads hold locks.
 *
 * <p>Even though this class is based on an internal FIFO queue, it
 * does not automatically enforce FIFO acquisition policies.  The core
 * of exclusive synchronization takes the form:
 *
 * <pre>
 *     独占模式
 * Acquire:
 *     while (!tryAcquire(arg)) {
 *        <em>enqueue thread if it is not already queued</em>;
 *        <em>possibly block current thread</em>;
 *     }
 *
 * Release:
 *     if (tryRelease(arg))
 *        <em>unblock the first queued thread</em>;
 * </pre>
 * <p>
 * (Shared mode is similar but may involve cascading signals.)
 *
 * <p id="barging">
 * Because checks in acquire are invoked before
 * enqueuing, a newly acquiring thread may <em>barge</em> ahead of
 * others that are blocked and queued.  However, you can, if desired,
 * define {@code tryAcquire} and/or {@code tryAcquireShared} to
 * disable barging by internally invoking one or more of the inspection
 * methods, thereby providing a <em>fair</em> FIFO acquisition order.
 * <p>
 * In particular, most fair synchronizers can define {@code tryAcquire}
 * to return {@code false} if {@link #hasQueuedPredecessors} (a method
 * specifically designed to be used by fair synchronizers) returns
 * {@code true}.  Other variations are possible.
 *
 * <p>Throughput and scalability are generally highest for the
 * default barging (also known as <em>greedy</em>,
 * <em>renouncement</em>, and <em>convoy-avoidance</em>) strategy.
 * While this is not guaranteed to be fair or starvation-free, earlier
 * queued threads are allowed to recontend before later queued
 * threads, and each recontention has an unbiased chance to succeed
 * against incoming threads.  Also, while acquires do not
 * &quot;spin&quot; in the usual sense, they may perform multiple
 * invocations of {@code tryAcquire} interspersed with other
 * computations before blocking.  This gives most of the benefits of
 * spins when exclusive synchronization is only briefly held, without
 * most of the liabilities when it isn't. If so desired, you can
 * augment this by preceding calls to acquire methods with
 * "fast-path" checks, possibly prechecking {@link #hasContended}
 * and/or {@link #hasQueuedThreads} to only do so if the synchronizer
 * is likely not to be contended.
 *
 * <p>This class provides an efficient and scalable basis for
 * synchronization in part by specializing its range of use to
 * synchronizers that can rely on {@code int} state, acquire, and
 * release parameters, and an internal FIFO wait queue. When this does
 * not suffice, you can build synchronizers from a lower level using
 * {@link java.util.concurrent.atomic atomic} classes, your own custom
 * {@link java.util.Queue} classes, and {@link LockSupport} blocking
 * support.
 * 这个类捏供了 state、获取、释
 * 放、还有内部的队列给我们实
 * 现同步。如果觉得功能不够的
 * 话,可以自己使用 atomic包和
 * Queue类来自己实现
 *
 * <h3>Usage Examples</h3>
 *
 * <p>Here is a non-reentrant mutual exclusion lock class that uses
 * the value zero to represent the unlocked state, and one to
 * represent the locked state. While a non-reentrant lock
 * does not strictly require recording of the current owner
 * thread, this class does so anyway to make usage easier to monitor.
 * It also supports conditions and exposes
 * one of the instrumentation methods:
 *
 *  <pre> {@code
 * class Mutex implements Lock, java.io.Serializable {
 *
 *   // Our internal helper class
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     // Reports whether in locked state
 *     protected boolean isHeldExclusively() {
 *       return getState() == 1;
 *     }
 *
 *     // Acquires the lock if state is zero
 *     public boolean tryAcquire(int acquires) {
 *       assert acquires == 1; // Otherwise unused
 *       if (compareAndSetState(0, 1)) {
 *         setExclusiveOwnerThread(Thread.currentThread());
 *         return true;
 *       }
 *       return false;
 *     }
 *
 *     // Releases the lock by setting state to zero
 *     protected boolean tryRelease(int releases) {
 *       assert releases == 1; // Otherwise unused
 *       if (getState() == 0) throw new IllegalMonitorStateException();
 *       setExclusiveOwnerThread(null);
 *       setState(0);
 *       return true;
 *     }
 *
 *     // Provides a Condition
 *     Condition newCondition() { return new ConditionObject(); }
 *
 *     // Deserializes properly
 *     private void readObject(ObjectInputStream s)
 *         throws IOException, ClassNotFoundException {
 *       s.defaultReadObject();
 *       setState(0); // reset to unlocked state
 *     }
 *   }
 *
 *   // The sync object does all the hard work. We just forward to it.
 *   private final Sync sync = new Sync();
 *
 *   public void lock()                { sync.acquire(1); }
 *   public boolean tryLock()          { return sync.tryAcquire(1); }
 *   public void unlock()              { sync.release(1); }
 *   public Condition newCondition()   { return sync.newCondition(); }
 *   public boolean isLocked()         { return sync.isHeldExclusively(); }
 *   public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
 *   public void lockInterruptibly() throws InterruptedException {
 *     sync.acquireInterruptibly(1);
 *   }
 *   public boolean tryLock(long timeout, TimeUnit unit)
 *       throws InterruptedException {
 *     return sync.tryAcquireNanos(1, unit.toNanos(timeout));
 *   }
 * }}</pre>
 *
 * <p>Here is a latch class that is like a
 * {@link java.util.concurrent.CountDownLatch CountDownLatch}
 * except that it only requires a single {@code signal} to
 * fire. Because a latch is non-exclusive, it uses the {@code shared}
 * acquire and release methods.
 *
 *  <pre> {@code
 * class BooleanLatch {
 *
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     boolean isSignalled() { return getState() != 0; }
 *
 *     protected int tryAcquireShared(int ignore) {
 *       return isSignalled() ? 1 : -1;
 *     }
 *
 *     protected boolean tryReleaseShared(int ignore) {
 *       setState(1);
 *       return true;
 *     }
 *   }
 *
 *   private final Sync sync = new Sync();
 *   public boolean isSignalled() { return sync.isSignalled(); }
 *   public void signal()         { sync.releaseShared(1); }
 *   public void await() throws InterruptedException {
 *     sync.acquireSharedInterruptibly(1);
 *   }
 * }}</pre>
 *
 * @author Doug Lea
 * @since 1.5
 */
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements java.io.Serializable {

    private static final long serialVersionUID = 7373984972572414691L;

    /**
     * Creates a new {@code AbstractQueuedSynchronizer} instance
     * with initial synchronization state of zero.
     */
    protected AbstractQueuedSynchronizer() {
    }

    /**
     * Wait queue node class.
     *
     * <p>The wait queue is a variant of a "CLH" (Craig, Landin, and
     * Hagersten) lock queue. CLH locks are normally used for
     * spinlocks(自旋锁).  We instead use them for blocking synchronizers, but
     * use the same basic tactic of holding some of the control
     * information about a thread in the predecessor of its node.
     * A "status"  field in each node keeps track of whether a thread should block. 判断线程是否被阻塞。
     * A node is signalled when its predecessor releases.  Each node of the queue otherwise serves as a
     * specific-notification-style monitor holding a single waiting
     * thread. The status field does NOT control whether threads are
     * granted locks etc though.  A thread may try to acquire if it is
     * first in the queue. But being first does not guarantee success;
     * it only gives the right to contend.  So the currently released
     * contender thread may need to rewait.
     *
     * <p>To enqueue into a CLH lock, you atomically splice it in as new tail. To dequeue, you just set the head field.
     * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
     *
     * <p>Insertion into a CLH queue requires only a single atomic
     * operation on "tail", so there is a simple atomic point of
     * demarcation from unqueued to queued. Similarly, dequeuing
     * involves only updating the "head". However, it takes a bit
     * more work for nodes to determine who their successors are,
     * in part to deal with possible cancellation due to timeouts
     * and interrupts.
     *
     * <p>The "prev" links (not used in original CLH locks), are mainly
     * needed to handle cancellation. If a node is cancelled, its
     * successor is (normally) relinked to a non-cancelled
     * predecessor. For explanation of similar mechanics in the case
     * of spin locks, see the papers by Scott and Scherer at
     * http://www.cs.rochester.edu/u/scott/synchronization/
     *
     * <p>We also use "next" links to implement blocking mechanics.
     * The thread id for each node is kept in its own node, so a
     * predecessor signals the next node to wake up by traversing
     * next link to determine which thread it is.  Determination of
     * successor must avoid races with newly queued nodes to set
     * the "next" fields of their predecessors.  This is solved
     * when necessary by checking backwards from the atomically
     * updated "tail" when a node's successor appears to be null.
     * (Or, said differently, the next-links are an optimization
     * so that we don't usually need a backward scan.)
     *
     * <p>Cancellation introduces some conservatism to the basic
     * algorithms.  Since we must poll for cancellation of other
     * nodes, we can miss noticing whether a cancelled node is
     * ahead or behind us. This is dealt with by always unparking
     * successors upon cancellation, allowing them to stabilize on
     * a new predecessor, unless we can identify an uncancelled
     * predecessor who will carry this responsibility.
     *
     * <p>CLH queues need a dummy header node to get started. But
     * we don't create them on construction, because it would be wasted
     * effort if there is never contention. Instead, the node
     * is constructed and head and tail pointers are set upon first
     * contention.
     *
     * <p>Threads waiting on Conditions use the same nodes, but
     * use an additional link. Conditions only need to link nodes
     * in simple (non-concurrent) linked queues because they are
     * only accessed when exclusively held.  Upon await, a node is
     * inserted into a condition queue.  Upon signal, the node is
     * transferred to the main queue.  A special value of status
     * field is used to mark which queue a node is on.
     *
     * <p>Thanks go to Dave Dice, Mark Moir, Victor Luchangco, Bill
     * Scherer and Michael Scott, along with members of JSR-166
     * expert group, for helpful ideas, discussions, and critiques
     * on the design of this class.
     */
    static final class Node {
        /**
         * Marker to indicate a node is waiting in shared mode
         * 标记一个结点（对应的线程）在共享模式下等待
         */
        static final Node SHARED = new Node();
        /**
         * Marker to indicate a node is waiting in exclusive mode
         * 标记一个结点（对应的线程）在独占模式下等待
         */
        static final Node EXCLUSIVE = null;

        /**
         * waitStatus value to indicate thread has cancelled
         * waitStatus的值，表示该结点（对应的线程）已被取消
         */
        static final int CANCELLED = 1;

        /**
         * waitStatus value to indicate successor's thread needs unparking
         * waitStatus的值，表示后继结点的状态（对应的线程）需要被唤醒
         */
        static final int SIGNAL = -1;

        /**
         * waitStatus value to indicate thread is waiting on condition
         * waitStatus的值，表示该结点（对应的线程）在等待某一条件
         */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         * waitStatus的值，表示有资源可用，新head结点需要继续唤醒后继结点（共享模式下，
         * 多线程并发释放资源，而head唤醒其后继结点后，需要把多出来的资源留给后面的结点；设置新的head结点时，会继续唤醒其后继结点）
         */
        static final int PROPAGATE = -3;

        /**
         * Status field, taking on only the values:
         * SIGNAL:     The successor of this node is (or will soon be)
         * blocked (via park), so the current node must
         * unpark its successor when it releases or
         * cancels. To avoid races, acquire methods must
         * first indicate they need a signal,
         * then retry the atomic acquire, and then,
         * on failure, block.
         * CANCELLED:  This node is cancelled due to timeout or interrupt.
         * Nodes never leave this state. In particular,
         * a thread with cancelled node never again blocks.
         * CONDITION:  This node is currently on a condition queue.
         * It will not be used as a sync queue node
         * until transferred, at which time the status
         * will be set to 0. (Use of this value here has
         * nothing to do with the other uses of the
         * field, but simplifies mechanics.)
         * PROPAGATE:  A releaseShared should be propagated to other
         * nodes. This is set (for head node only) in
         * doReleaseShared to ensure propagation
         * continues, even if other operations have
         * since intervened.
         * 0:          None of the above
         * <p>
         * The values are arranged numerically to simplify use.
         * Non-negative values mean that a node doesn't need to
         * signal. So, most code doesn't need to check for particular
         * values, just for sign.
         * <p>
         * The field is initialized to 0 for normal sync nodes, and
         * CONDITION for condition nodes.  It is modified using CAS
         * (or when possible, unconditional volatile writes).
         * 等待状态，取值范围，-3，-2，-1，0，1
         * == 0 时默认状态
         * > 0  取消状态
         * ==-1   如果当前node 是head节点时需要唤醒之后的后继机节点
         */
        volatile int waitStatus;

        /**
         * Link to predecessor node that current node/thread relies on
         * for checking waitStatus. Assigned during enqueuing, and nulled
         * out (for sake of GC) only upon dequeuing.  Also, upon
         * cancellation of a predecessor, we short-circuit while
         * finding a non-cancelled one, which will always exist
         * because the head node is never cancelled: A node becomes
         * head only as a result of successful acquire. A
         * cancelled thread never succeeds in acquiring, and a thread only
         * cancels itself, not any other node.
         * 前驱结点
         */
        volatile Node prev;

        /**
         * Link to the successor node that the current node/thread
         * unparks upon release. Assigned during enqueuing, adjusted
         * when bypassing cancelled predecessors, and nulled out (for
         * sake of GC) when dequeued.  The enq operation does not
         * assign next field of a predecessor until after attachment,
         * so seeing a null next field does not necessarily mean that
         * node is at end of queue. However, if a next field appears
         * to be null, we can scan prev's from the tail to
         * double-check.  The next field of cancelled nodes is set to
         * point to the node itself instead of null, to make life
         * easier for isOnSyncQueue.
         * 后继结点
         */
        volatile Node next;

        /**
         * The thread that enqueued this node.  Initialized on
         * construction and nulled out after use.
         * 结点对应的线程
         */
        volatile Thread thread;

        /**
         * Link to next node waiting on condition, or the special
         * value SHARED.  Because condition queues are accessed only
         * when holding in exclusive mode, we just need a simple
         * linked queue to hold nodes while they are waiting on
         * conditions. They are then transferred to the queue to
         * re-acquire. And because conditions can only be exclusive,
         * we save a field by using special value to indicate shared
         * mode.
         * 等待队列里下一个等待条件的结点
         */
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode.
         * 判断共享模式的方法
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * Returns previous node, or throws NullPointerException if null.
         * Use when predecessor cannot be null.  The null check could
         * be elided, but is present to help the VM.
         *
         * @return the predecessor of this node
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null) throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    /**
     * Head of the wait queue, lazily initialized.  Except for
     * initialization, it is modified only via method setHead.  Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     * 头节点 任何时候  头节点对应的线程是拿到锁的当前线程
     */
    private transient volatile Node head;

    /**
     * Tail of the wait queue, lazily initialized.  Modified only via
     * method enq to add new wait node.
     * 阻塞队列不包含 头节点
     * 尾节点
     */
    private transient volatile Node tail;

    /**
     * The synchronization state.
     * 表示资源
     * 独占模式： 0： 未加锁  >0：已经加锁状态
     */
    private volatile int state;

    /**
     * Returns the current value of synchronization state.
     * This operation has memory semantics of a {@code volatile} read.
     *
     * @return current state value
     */
    protected final int getState() {
        return state;
    }

    /**
     * Sets the value of synchronization state.
     * This operation has memory semantics of a {@code volatile} write.
     *
     * @param newState the new state value
     */
    protected final void setState(int newState) {
        state = newState;
    }

    /**
     * Atomically sets synchronization state to the given updated
     * value if the current state value equals the expected value.
     * This operation has memory semantics of a {@code volatile} read
     * and write.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that the actual
     * value was not equal to the expected value.
     */
    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // Queuing utilities

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices
     * to improve responsiveness with very short timeouts.
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * Inserts node into queue, initializing if necessary. See picture above.
     *
     * @param node the node to insert
     * @return node's predecessor
     * 自旋CAS插入竞争等待队列
     */
    private Node enq(final Node node) {
        // 无限循环  stop the world !!!
        for (; ; ) {
            Node t = tail;
            // 如果尾结点为null，说明这是第一次出现多个线程同时争抢锁，因此同步队列没有被初始化，tail和head此时均为null
            if (t == null) {
                // 设置head节点，注意此时是new了一个Node节点，节点的next,prev,thread属性均为null
                // 这是因为对于头结点而言，作为队列的头部，它的next指向本来就应该是null
                // 而头结点的thread属性为空，这是AQS同步器特意为之的
                // 头结点的prev属性此时为空，当进行第二次for循环的时候，tail结点就不为空了，因此不会进入到if语句中，而是进入到else语句中，在这里对头结点的prev进行了赋值
                if (compareAndSetHead(new Node())) {
                    // 第一次初始化队列时，头结点和尾结点是同一个结点
                    tail = head;
                }
                //注意：此时并没有返回 而是继续for循环 下一次将会执行 以下 普通入队 逻辑
            } else {
                // 队尾结点不为null，说明队列已经进行了初始化，这个时候只需要将当前线程表示的node结点加入到队列的尾部

                // 设置当前线程所代表的节点的prev的指针，使其指向尾结点
                node.prev = t;
                // 利用CAS操作设置队尾结点
                if (compareAndSetTail(t, node)) {
                    // 然后修改队列中倒数第二个节点的next指针，使其指向队尾结点
                    t.next = node;
                    // 然后返回的是队列中倒数第二个节点（也就是旧队列中的尾结点）
                    return t;
                }
            }
        }
    }

    /**
     * Creates and enqueues node for current thread and given mode.
     *
     * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
     * @return the new node
     * addWaiter(Node.EXCLUSIVE) 方法把这个线程插入到等待队列中，其中传入的参数代表要插入的Node是独占式的
     */
    private Node addWaiter(Node mode) {
        //生成该线程对应的Node节点
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure

        // 然后需要把当前线程代表的节点添加到队列的尾部，此时队尾可能是null，因为可能这是第一次出现多个线程争抢锁，队列还没有初始化，即头尾均为null
        //获取队尾节点 尾插法
        Node pred = tail;

        //队列中存在节点  队列不为空
        if (pred != null) {
            // 如果队尾不为null,就直接将当前线程代表的node添加到队尾，修改node节点中prev的指针指向
            node.prev = pred;
            // 利用CAS操作设置队尾，如果设置成功，就修改倒数第二个节点的next的指针指向，然后方法return结束
            // 在多个线程并发的情况下，CAS操作可能会失败，如果失败，就会进入到后面的逻辑
            if (compareAndSetTail(pred, node)) {
                //指向完成后 需要把之前的尾节点 指向当前节点 双向链表么
                //Todo 存在并发问题 ：CAS 成功了 下层代码还未进行赋值  其他地方就直接调用  pred.next = node 了
                pred.next = node;
                return node;
            }
        }

        // 完整入队方法 : 进行同步队列的初始化 + 当前线程的入队操作
        // 1. 如果等待队列为空
        // 2. CAS 失败
        enq(node);
        // 将当前线程代表的节点返回
        return node;
    }

    /**
     * Sets head of queue to be node, thus dequeuing. Called only by
     * acquire methods.  Also nulls out unused fields for sake of GC
     * and to suppress unnecessary signals and traversals.
     *
     * @param node the node
     */
    private void setHead(Node node) {
        head = node;

        node.thread = null;

//        node.thread=Thread.currentThread();

        node.prev = null;
    }

    /**
     * Wakes up node's successor, if one exists.
     *
     * @param node the node
     *             传入的是 head 头节点
     */
    private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        //获取当前节点等待状态
        int ws = node.waitStatus;

        //当是 -1 状态时 需要唤醒后续节点
        if (ws < 0) {
            //设置成 0 状态 原因：当前当前节点已经完成喊后继节点的任务了
            compareAndSetWaitStatus(node, ws, 0);
        }

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        //获取后置节点的第一个节点
        Node s = node.next;

        //1.后置节点为null ?   1. cas 设置新节点为 tail 调用此方法时 那么后继节点就是null   2. 入队逻辑中   652行   pred.next = node 还未赋值 为 null
        // 2.说明 node节点 为取消状态，得找一个合适的可以被唤醒的节点
        if (s == null || s.waitStatus > 0) {
            s = null;
            //为何从尾部找？
            //从尾部向前获取到一个 里 node 最近的 并且 不为 null 并且 状态不是取消的节点
            /**
             * 同步队列中的节点所代表的线程，可能被取消了，此时这个节点的waitStatus=1
             * 因此这个时候利用for循环从同步队列尾部开始向前遍历，判断节点是不是被取消了
             * 正常情况下，当头结点释放锁以后，应该唤醒同步队列中的第二个节点，但是如果第二个节点的线程被取消了，此时就不能唤醒它了,
             * 就应该判断第三个节点，如果第三个节点也被取消了，再依次往后判断，直到第一次出现没有被取消的节点。如果都被取消了，此时s==null，所以不会唤醒任何线程
             */
            for (Node t = tail; t != null && t != node; t = t.prev) {
                if (t.waitStatus <= 0) {
                    s = t;
                }
            }
            //以上循环可能找不到 node 节点 node 可能是null
        }

        //如果找到可以唤醒的 节点
        //那么之前在 963行 parkAndCheckInterrupt 执行的 park 操作就会继续醒来自旋获取锁
        if (s != null) {
            LockSupport.unpark(s.thread);
        }
    }

    /**
     * Release action for shared mode -- signals successor and ensures propagation.
     * 共享模式下的释放动作-表示后继信号并确保传播
     * (Note: For exclusive mode, release just amounts to calling unparkSuccessor of head if it needs signal.)
     * 对于独占模式，如果需要信号，释放仅相当于调用head的unparkSuccessor。
     * /**
     * 都有哪几种路径会调用到doReleaseShared方法呢？
     * 1. 调用 latch.countDown() -> 得到 AQS.state == 0 -> doReleaseShared() 唤醒当前阻塞队列内的 head.next 对应的线程
     * 2.被唤醒的线程 -> doAcquireSharedInterruptibly.parkAndCheckInterrupt() 被唤醒 -> setHeadAndPropagate() -> doReleaseShared()
     */
    private void doReleaseShared() {
        //开始自旋
        for (; ; ) {
            //获取当前 AQS 内的 头结点
            Node h = head;
            //条件一：h != null 成立，说明阻塞队列不为空..
            //不成立：h == null 什么时候会是这样呢？
            //latch创建出来后，没有任何线程调用过 await() 方法之前，有线程调用 latch.countDown() 操作 且触发了 唤醒阻塞节点的逻辑.. 这不是错误用法吗

            //条件二：h != tail 成立，说明当前阻塞队列内，除了head节点以外  还有其他节点
            // 不成立：h == tail  -> head 和 tail 指向的是同一个node对象。 什么时候会有这种情况呢？
            //1. 正常唤醒情况下，依次获取到 共享锁，当前线程执行到这里时 （这个线程就是 tail 节点）
            //2. 第一个调用 await() 方法的线程 与 调用 countDown() 且触发唤醒阻塞节点的线程 出现并发了..
            //   因为await()线程是第一个调用 latch.await()的线程，此时队列内什么也没有，需要初始化，它需要补充创建一个Head节点，然后再次自旋时入队
            //   在await()线程入队完成之前，假设当前队列内 只有 刚刚补充创建的空元素 head
            //   同期，外部有一个调用 countDown() 的线程，将 state 值从1，修改为0了，那么这个线程需要做 唤醒 阻塞队列内元素的逻辑..
            //   注意：调用 await() 的线程 因为完全入队完成之后，再次回到上层方法 doAcquireSharedInterruptibly 会进入到自旋中，
            //   获取当前元素的前驱，判断自己是 head.next， 所以接下来该线程又会将自己设置为 head，然后该线程就从 await() 方法返回了...
            if (h != null && h != tail) {
                //执行到 if 里面，说明当前 head 一定有 后继节点!

                int ws = h.waitStatus;
                // 当前 head 状态 为 signal 说明 后继节点并没有被唤醒过呢...
                if (ws == Node.SIGNAL) {
                    //唤醒后继节点前 将head节点的状态改为 0
                    //这里为什么，使用 CAS 呢？ 回头说...
                    //当 doReleaseShared 方法 存在多个线程 唤醒 head.next 逻辑时，
                    // CAS 可能会失败...
                    //案例：
                    // t3 线程在 if(h == head) 返回false时，t3 会继续自旋. 参与到 唤醒下一个 head.next 的逻辑..
                    // t3 此时执行到 CAS WaitStatus(h,Node.SIGNAL, 0) 成功.. t4 在 t3 修改成功之前，也进入到 if (ws == Node.SIGNAL) 里面了，
                    //但是 t4 修改 CAS WaitStatus(h,Node.SIGNAL, 0) 会失败，因为 t3 改过了...
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)) {
                        continue; // loop to recheck cases
                    }
                    // 唤醒后继节点
                    unparkSuccessor(h);
                } else if (ws == 0 && !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;                // loop on failed CAS
            }

            //条件成立：
            //1.说明刚刚唤醒的 后继节点，还没执行到 setHeadAndPropagate 方法里面的 设置当前唤醒节点为 head 的逻辑
            //这个时候，当前线程 直接跳出去...结束了..
            //此时用不用担心，唤醒逻辑 在这里断掉呢？
            //不需要担心，因为被唤醒的线程 早晚会执行到 doReleaseShared 方法

            //2. h == null  latch创建出来后，没有任何线程调用过 await() 方法之前，
            //有线程调用 latch.countDown() 操作 且触发了 唤醒阻塞节点的逻辑..错误用法
            //3. h == tail  -> head 和 tail 指向的是同一个 node 对象

            //条件不成立：
            //被唤醒的节点 非常积极，直接将自己设置为了新的head，此时 唤醒它的节点（前驱），执行h == head 条件会不成立..
            //此时 head节点的前驱，不会跳出 doReleaseShared 方法，会继续唤醒 新head 节点的后继...
            if (h == head)                   // loop if head changed
                break;
        }
    }

    /**
     * Sets head of queue, and checks if successor may be waiting
     * in shared mode, if so propagating if either propagate > 0 or
     * PROPAGATE status was set.
     * 传播
     *
     * @param node      the node
     * @param propagate the return value from a tryAcquireShared
     *                  设置当前节点为 head节点，并且向后传播！（依次唤醒！）
     */
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        //将当前节点设置为 新的 head节点。
        setHead(node);
        //调用 setHeadAndPropagete 时  propagate  == 1 一定成立
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            //获取当前节点的后继节点..
            Node s = node.next;
            //条件一：s == null  什么时候成立呢？  当前node节点已经是 tail了，条件一会成立。 doReleaseShared() 里面会处理这种情况..
            //条件二：前置条件，s != null ， 要求s节点的模式必须是 共享模式。 latch.await() -> addWaiter(Node.SHARED)
            if (s == null || s.isShared())
                // 基本上所有情况都会执行到 doReleasseShared() 方法
                doReleaseShared();
        }
    }

    // Utilities for various versions of acquire

    /**
     * Cancels an ongoing attempt to acquire.
     *
     * @param node the node
     *             取消指定线程参与竞争
     */
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null) {
            return;
        }

        //取消排队了，取消内置的 node 节点
        node.thread = null; //help GC

        // 获得当前取消排队的 node 的前驱
        Node pred = node.prev;
        //如果仍然是 取消状态 那么就循环找
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }

        // 拿到当前节点的 后继 节点
        // 1. 可能还是当前 node 节点
        //2. 可能获得的 node节点 的 ws>0
        Node predNext = pred.next;

        // 执行这一步的情况有：doAcquireInterruptibly() 方法中 ：线程被唤醒后 检查中断标记 ：是 true 的话 就执行此方法 随后设置节点状态 为 取消状态
        //将当前的 node 设置为取消状态
        node.waitStatus = Node.CANCELLED;

        /*
         * 当前取消排队的 node 节点的所在队列的位置不同  出队的策略也就不同：
         * 1. 当前 node 节点是队尾
         * 2. 当前 node 不是 head.next 节点 也不是 tail  节点
         * 3. 当前 node 是 head.next 节点
         */

        //1.当前节点就是 尾节点；就去设置tail 指针为 pred 节点
        if (node == tail && compareAndSetTail(node, pred)) {
            //断掉pred.next 指针 指向空
            compareAndSetNext(pred, predNext, null);
        } else {
            //保存节点状态的
            int ws;
            //2. pred != head 成立 说明 当前 node 不是 head.next 节点 也不是 tail  节点
            if (pred != head &&
                    // (ws = pred.waitStatus) == Node.SIGNAL 成立：说明node的前驱节点是 Signal 状态 ；不成立： 说明 前驱节点状态时0 或者 也是取消状态
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            //   (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL)) 设置前驱状态为 Signal 状态 表示要唤醒后继节点
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) && pred.thread != null) {
                //逻辑是 要让 pred.next -> node.next ，所以要保证 pred 节点状态为 SIGNAL 状态；
                //todo   不懂为何牵扯唤醒逻辑
                //出队： pred.next -> node.next 节点后， 当 node.next  节点被唤醒后
                // 调用 shouldParkAfterFailedAcquire 会让 node.next  节点越过取消状态的节点
                // 完成真正 出队
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                //3. 当前 node 是 head.next 节点
                //类似情况2  后继节点被唤醒后，调用 shouldParkAfterFailedAcquire 会让 node.next 节点越过取消状态的节点
                //队列的第三个节点 会 直接 与 head 建立双重指向的关系
                unparkSuccessor(node);
            }
            //出队的节点 让其后指针指向自己 帮助gc
            node.next = node; // help GC
        }
    }

    /**
     * Checks and updates status for a node that failed to acquire.
     * Returns true if thread should block. This is the main signal
     * control in all acquire loops.  Requires that pred == node.prev.
     *
     * @param pred node's predecessor holding status
     * @param node the node
     * @return {@code true} if thread should block
     * 总结：
     * 1.当前节点的前置节点是 取消状态 ，第一次来到这个方法时 会越过 取消状态的节点， 第二次 会返回true 然后park当前线程
     * 2.当前节点的前置节点状态是0，当前线程会设置前置节点的状态为 -1 ，第二次自旋来到这个方法时  会返回true 然后park当前线程.
     * <p>
     * 参数一：pred 当前线程node的前置节点
     * 参数二：node 当前线程对应node
     * 返回值：boolean  true 表示当前线程需要挂起..
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        // 在初始情况下，所有的Node节点的waitStatus均为0，因为在初始化时，waitStatus字段是int类型，我们没有显示给它赋值，所以它默认是0
        // 后面waitStatus字段会被修改
        //waitStatus：0 默认状态 new Node() ； -1 Signal状态，表示当前节点释放锁之后会唤醒它的第一个后继节点； >0 表示当前节点是CANCELED状态

        //在第一进入到shouldParkAfterFailedAcquire()时，所以waitStatus是默认值0（因为此时没有任何地方对它进行修改),所以ws=0
        int ws = pred.waitStatus;

        //表示 前置节点是一个可以 唤醒后置节点的节点  因此返回 true
        //普通状态下 第一次来到这里是不会为 -1 的
        if (ws == Node.SIGNAL) {
            // 如果ws = -1，就返回true
            // 如果第一次进入shouldParkAfterFailedAcquire()方法时，waitStatus=0,那么就会进入到后面的else语句中
            // 在else语句中，会将waitStatus设置为-1，那么当后面第二次进入到shouldParkAfterFailedAcquire()方法时，就会返回true了
            return true;
        }

        //表示 前置节点是 canceled 状态
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and indicate retry.
             * 前身已取消，跳过前任并指示重试
             */
            //找爸爸的过程，条件是什么呢？ 前置节点的 waitStatus <= 0 的情况
            do {
                //由于前置节点不可用 所以需要跳过前置节点 指向前前节点
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            //找到好爸爸后，退出循环
            //隐含着一种操作，CANCELED 状态的节点会被出队
            pred.next = node;
        }
        // 如果ws既不等于-1，也不大于0，就会进入到当前else语句中
        // 此时会调用CAS方法将pred节点的waitStatus字段的值改为-1
        else {
            //当前 node 前置节点的状态就是 0 的这一种情况
            //将当前线程 node 的前置 preNode，状态强制设置为 SIGNAl，表示前置节点释放锁之后需要 喊醒我...
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

    /**
     * Convenience method to interrupt current thread.
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * Convenience method to park and then check if interrupted
     *
     * @return {@code true} if interrupted
     * 如果某线程 A 调用park，那么除非另外一个线程调用 unpark(A) 给A一个许可，否则线程 A 将阻塞在 park 操作上
     */
    private final boolean parkAndCheckInterrupt() {
        // 通过 LockSupport 类的 park 方法来阻塞当前线程
        // 阻塞中,等待被唤醒；
        LockSupport.park(this);
        // 被唤醒后，返回中断标志
        //隐蔽点：由于此线程执行挂起 park 操作，当外部有线程想要中断这个线程时 并不会抛出中断异常，
        //只会改变当前线程内部的中断状态值；就会返回true；
        return Thread.interrupted();
    }

    /*
     * Various flavors of acquire, varying in exclusive/shared and
     * control modes.  Each is mostly the same, but annoyingly
     * different.  Only a little bit of factoring is possible due to
     * interactions of exception mechanics (including ensuring that we
     * cancel if tryAcquire throws exception) and other control, at
     * least not without hurting performance too much.
     */

    /**
     * Acquires in exclusive uninterruptible mode for thread already in queue. Used by condition wait methods as well as acquire.
     * 队列中的节点尝试获取到锁
     *
     * @param node the node
     * @param arg  the acquire argument
     * @return {@code true} if interrupted while waiting
     * 1. 当前节点是不是被park了尼？ 执行挂起操作
     * 2. 节点被唤醒之后的逻辑？
     * 参数一：当前线程包装出来的node节点  并且已经入队成功了的
     * 参数二：当先线程抢占资源成功后 设置 state 会用到
     * acquireQueued()作用:就是让线程以不间断的方式获取锁，如果获取不到，就会一直阻塞在这个方法里面，直到获取到锁，才会从该方法里面返回
     */
    final boolean acquireQueued(final Node node, int arg) {
        //true 表示当前线程 抢占资锁成功 普通情况下 【lock】 早晚会得到锁
        //false 表示  失败 需要执行出队的逻辑
        boolean failed = true;
        try {
            //当前线程是否 被中断
            boolean interrupted = false;
            // 无限for循环 没有锁 就是多个线程可进入
            for (; ; ) {
                //什么时候会执行到这里？
                //1.进入for循环后 当前线程未被 park 前会执行这
                //2. 挂起线程 被唤醒后 会执行 这里

                // 获取当前线程所代表节点的前一个节点
                final Node p = node.predecessor();

                // 条件一  成立: 如果 node 的前驱结点 p 是 head，表示 node 是第二个结点，就可以尝试去获取资源了  执行下一个判断;
                //条件一  不成立: 由于 && 短路效果 直接不走下一个判断了
                //条件二  成立：获得锁  执行逻辑
                //条件二  不成立：说明 head 的线程还未释放锁 当前线程还需继续 park

                // 如果前一个节点是头结点（即当前线程是队列中的第二个节点），那么就调用tryAcquire()方法让当前线程去尝试获取锁
                if (p == head && tryAcquire(arg)) {
                    // 拿到资源后，将 head 指向当前 结点
                    // 所以 head 所指的结点，就是当前获取到资源的那个结点
                    // 如果当前线程获取锁成功，那么就将当前线程所代表的节点设置为头结点(因为AQS的设计原则就是：队列中占有锁的线程一定是头结点)
                    setHead(node);
                    // 然后将原来的头节点的next指针设置为null，这样头节点就没有任何引用了，有助于GC回收。
                    p.next = null; // help GC
                    //当前线程获取锁的过程中没有发生异常
                    failed = false;
                    //记得执行 finally 代码
                    //返回interrupted所代表的值，表示当前线程是不是通过中断而醒来的
                    return interrupted;
                }

                // 如果前一个节点不是头结点 或者 前一个节点是头结点，但是当前节点调用tryAcquire()方法获取锁失败，那么就会执行到下面的if判断

                /**
                 * 在if判断中，先调用了shouldParkAfterFailedAcquire()方法
                 * 在第一次调用shouldParkAfterFailedAcquire()时，肯定返回false(为什么会返回false，可以看下shouldParkAfterFailedAcquire()方法的源码)
                 * 由于当前代码是处于无限for循环当中的，所以当后面出现第二次代码执行到这儿时，会再次调用shouldParkAfterFailedAcquire()方法，此时这个方法会返回true。
                 * 当shouldParkAfterFailedAcquire()返回true时，if判断中就会再调用parkAndCheckInterrupt()方法，该方法会将当前线程进行阻塞，直到这个线程被唤醒或者被中断。
                 * 因此当线程获取不到锁时，就会一直阻塞到这儿。直到被其他线程唤醒，才会继续向下执行，当线程醒来后，再次进入到当前代码的无限for循环中，除非线程获取到锁，才会return返回
                 */
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()) {
                    //设置中断标志位  而另外的 则是抛出中断异常
                    interrupted = true;
                }

            }//for结束
        } finally {
            // 如果在try中出现异常，那么此时failed就会为true，就会取消获取锁
            // failed的初始值为true，如果try语句中不出现异常，最终线程获取到锁后，就会将failed置为false，那么下面的if判断条件就不会成立，也就不执行cancelAcquire()方法了
            // 如果上面的try语句存在异常，那么就不会将failed设置为false，那么就会执行cancelAcquire()方法
            if (failed) {
                //取消指定线程参与竞争
                cancelAcquire(node);
            }
        }
    }

    /**
     * Acquires in exclusive interruptible mode.
     * 申请可中断的资源（独占模式）
     *
     * @param arg the acquire argument
     */
    private void doAcquireInterruptibly(int arg) throws InterruptedException {
        //将当前线程添加等CLH等待队列
        final Node node = addWaiter(Node.EXCLUSIVE);
        //初始化失败标志
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                //如果前置节点为首节点，并且当前线程能够成功获取锁
                if (p == head && tryAcquire(arg)) {
                    //将当前节点设置为首节点
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }

                // 如果自己可以休息了，就进入 waiting 状态，直到被 unpark()

                // 判断是否可以阻塞线程并做相应操作
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            // 一旦检测到中断请求，立即返回不再参与锁的竞争并且取消锁获取操作（即finally中的cancelAcquire操作）
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in exclusive timed mode.
     *
     * @param arg          the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout) throws InterruptedException {
        if (nanosTimeout <= 0L) return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                //判断是否等待超时，如果超时，则返回false
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L) {
                    return false;
                }
                //这里判断是否可以阻塞线程并做相应操作，跟之前分析的几个方法不一样的是，这里的阻塞多了一个判断，并且是在有限时间内阻塞，
                // 类似于sleep
                if (shouldParkAfterFailedAcquire(p, node) && nanosTimeout > spinForTimeoutThreshold) {
                    LockSupport.parkNanos(this, nanosTimeout);
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed) {
                cancelAcquire(node);
            }
        }
    }

    /**
     * Acquires in shared uninterruptible mode.
     * 申请共享模式的资源
     *
     * @param arg the acquire argument
     */
    private void doAcquireShared(int arg) {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared interruptible mode.
     * 申请可中断的资源（共享模式）
     *
     * @param arg the acquire argument
     */
    private void doAcquireSharedInterruptibly(int arg) throws InterruptedException {
        //将调用 latch.await() 方法的线程 包装成 node 加入到 AQS 的阻塞队列当中
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (; ; ) {
                //获取当前线程节点的前驱节点
                final Node p = node.predecessor();
                //条件成立，说明当前线程对应的节点 为 head.next节点
                if (p == head) {
                    //head.next 节点就有权利获取 共享锁了..
                    int r = tryAcquireShared(arg);

                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                //shouldParkAfterFailedAcquire  会给当前线程找一个好爸爸，最终给爸爸节点设置状态为 signal（-1），返回true
                //parkAndCheckInterrupt 挂起当前节点对应的线程...
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()) {
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared timed mode.
     *
     * @param arg          the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // Main exported methods

    /**
     * Attempts to acquire in exclusive mode. This method should query
     * if the state of the object permits it to be acquired in the
     * exclusive mode, and if so to acquire it.
     *
     * <p>This method is always invoked by the thread performing
     * acquire.  If this method reports failure, the acquire method
     * may queue the thread, if it is not already queued, until it is
     * signalled by a release from some other thread. This can be used
     * to implement method {@link Lock#tryLock()}.
     *
     * <p>The default
     * implementation throws {@link UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *            passed to an acquire method, or is the value saved on entry
     *            to a condition wait.  The value is otherwise uninterpreted
     *            and can represent anything you like.
     * @return {@code true} if successful. Upon success, this object has
     * been acquired.
     * @throws IllegalMonitorStateException  if acquiring would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     *                                       首先调用tryAcquire(arg)尝试去获取资源，立即返回，即便是没有获取到；
     *                                       前面提到了这个方法是在子类具体实现的。
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in exclusive
     * mode.
     *
     * <p>This method is always invoked by the thread performing release.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *            passed to a release method, or the current state value upon
     *            entry to a condition wait.  The value is otherwise
     *            uninterpreted and can represent anything you like.
     * @return {@code true} if this object is now in a fully released
     * state, so that any waiting threads may attempt to acquire;
     * and {@code false} otherwise.
     * @throws IllegalMonitorStateException  if releasing would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to acquire in shared mode. This method should query if
     * the state of the object permits it to be acquired in the shared
     * mode, and if so to acquire it.
     *
     * <p>This method is always invoked by the thread performing
     * acquire.  If this method reports failure, the acquire method
     * may queue the thread, if it is not already queued, until it is
     * signalled by a release from some other thread.
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *            passed to an acquire method, or is the value saved on entry
     *            to a condition wait.  The value is otherwise uninterpreted
     *            and can represent anything you like.
     * @return a negative value on failure; zero if acquisition in shared
     * mode succeeded but no subsequent shared-mode acquire can
     * succeed; and a positive value if acquisition in shared
     * mode succeeded and subsequent shared-mode acquires might
     * also succeed, in which case a subsequent waiting thread
     * must check availability. (Support for three different
     * return values enables this method to be used in contexts
     * where acquires only sometimes act exclusively.)  Upon
     * success, this object has been acquired.
     * @throws IllegalMonitorStateException  if acquiring would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in shared mode.
     *
     * <p>This method is always invoked by the thread performing release.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *            passed to a release method, or the current state value upon
     *            entry to a condition wait.  The value is otherwise
     *            uninterpreted and can represent anything you like.
     * @return {@code true} if this release of shared mode may permit a
     * waiting acquire (shared or exclusive) to succeed; and
     * {@code false} otherwise
     * @throws IllegalMonitorStateException  if releasing would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if synchronization is held exclusively with
     * respect to the current (calling) thread.  This method is invoked
     * upon each call to a non-waiting {@link ConditionObject} method.
     * (Waiting methods instead invoke {@link #release}.)
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}. This method is invoked
     * internally only within {@link ConditionObject} methods, so need
     * not be defined if conditions are not used.
     *
     * @return {@code true} if synchronization is held exclusively;
     * {@code false} otherwise
     * @throws UnsupportedOperationException if conditions are not supported
     */
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    /**
     * Acquires in exclusive mode, ignoring interrupts.  Implemented
     * by invoking at least once {@link #tryAcquire}, returning on success.
     * Otherwise the thread is queued, possibly repeatedly blocking and unblocking, invoking {@link
     * #tryAcquire} until success.  This method can be used
     * to implement method {@link Lock#lock}.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *            {@link #tryAcquire} but is otherwise uninterpreted and
     *            can represent anything you like.
     *            acquire(int)尝试获取资源，如果获取失败，将线程插⼊等待队列。addWaiter 插⼊等待队列后，
     *            acquire(int)并没有放弃获取资源，⽽是根据前置节点状态状态判断是否应该继续获取资源，如果
     *            前置节点是头结点，继续尝试获取资源，如果前置节点是SIGNAL状态，就中断当前线程，否则继
     *            续尝试获取资源。直到当前线程被park()或者获取到资源，acquire(int)结束。
     *            <p>
     *            获取资源的入口是acquire(int arg)方法。arg是要获取的资源的个数，在独占模式下始终为1。
     *            如果获取资源失败，就通过addWaiter(Node.EXCLUSIVE)方法把这个线程插入到等待队列中。
     *            其中传入的参数代表要插入的Node是独占式的。
     *            <p>
     *            lock获取锁过程中，忽略了中断，在成功获取锁之后，再根据中断标识处理中断;
     */
    //总结：只有当线程获取到锁时，acquire()方法才会结束；如果线程没有获取到锁，那么它就会一直阻塞在acquireQueued()方法中，那么acquire()方法就一直不结束。
    public final void acquire(int arg) {
        //tryAcquire() 返回 true -> 获取资源成功  !true=false   函数直接退出
        //tryAcquire() 返回 false -> 获取资源失败  !false=true  函数进行入队争抢资源
        //acquireQueued 挂起当前线程  和 唤醒之后的逻辑
        //acquireQueued() 返回 true : 表示 挂起线程过程中 线程被中断处理过 ; 否则表示未被中断过 , 是正常唤醒
        if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {
            //当线程从acquireQueued()方法处返回时，返回值有两种情况:
            // 1,如果返回false，表示线程不是被中断才唤醒的，
            // 所以此时在acquire()方法中，if判断不成立，就不会执行selfInterrupt()方法，而是直接返回。
            // 2.如果返回true，则表示线程是被中断才唤醒的，由于在parkAndCheckInterrupt()方法中调用了Thread.interrupted()方法，
            // 这会将线程的中断标识重置，所以此时需要返回true，使acquire()方法中的if判断成立，然后这样就会调用selfInterrupt()方法，
            // 将线程的中断标识重新设置一下。最后acquire()方法返回
            selfInterrupt();
        }
    }

    /**
     * Acquires in exclusive mode, aborting if interrupted.
     * Implemented by first checking interrupt status, then invoking
     * at least once {@link #tryAcquire}, returning on
     * success.  Otherwise the thread is queued, possibly repeatedly
     * blocking and unblocking, invoking {@link #tryAcquire}
     * until success or the thread is interrupted.  This method can be
     * used to implement method {@link Lock#lockInterruptibly}.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *            {@link #tryAcquire} but is otherwise uninterpreted and
     *            can represent anything you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    public final void acquireInterruptibly(int arg) throws InterruptedException {
        //第一个是判断线程的中断标识，如果为true，则抛出中断异常。
        if (Thread.interrupted()) throw new InterruptedException();
        //如果获取不到锁
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    /**
     * Attempts to acquire in exclusive mode, aborting if interrupted,
     * and failing if the given timeout elapses.  Implemented by first
     * checking interrupt status, then invoking at least once {@link
     * #tryAcquire}, returning on success.  Otherwise, the thread is
     * queued, possibly repeatedly blocking and unblocking, invoking
     * {@link #tryAcquire} until success or the thread is interrupted
     * or the timeout elapses.  This method can be used to implement
     * method {@link Lock#tryLock(long, TimeUnit)}.
     *
     * @param arg          the acquire argument.  This value is conveyed to
     *                     {@link #tryAcquire} but is otherwise uninterpreted and
     *                     can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout) throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();
        //尝试获取锁，如果成功则返回true，失败则调用 doAcquireNanos() 进行等待
        return tryAcquire(arg) || doAcquireNanos(arg, nanosTimeout);
    }

    /**
     * Releases in exclusive mode.  Implemented by unblocking one or more threads if {@link #tryRelease} returns true.
     * This method can be used to implement method {@link Lock#unlock}.
     *
     * @param arg the release argument.  This value is conveyed to
     *            {@link #tryRelease} but is otherwise uninterpreted and
     *            can represent anything you like.
     * @return the value returned from {@link #tryRelease}
     * ⾸先调⽤⼦类的tryRelease()⽅法释放锁,然后唤醒后继节点,在唤醒的过程中,需要判断后继节点是否满⾜情况,
     * 如果后继节点不为且不是作废状态,则唤醒这个后继节点,否则从tail节点向前寻找合适的节点,如果找到,则唤醒.
     */
    public final boolean release(int arg) {
        // 若返回 true 表示当前线程已经完全释放锁
        // 返回 false 表示 尚未完全释放锁
        if (tryRelease(arg)) {//子类实现
            // head 什么时机会被创建出来？ 初始化 时被创建出来
            // 当前线程 未释放锁 存在其他线程过来抢占锁  当获取不到时 将会创建 head 点
            Node h = head;
            //1.判断 CLH 队列的首节点是否为 null   已经初始化过了 发生过多线程竞争了  并且 head 节点的后一个节点 会把 head 节点的状态设置成 -1 状态
            //2.并判断等待状态是否正常 当不等于0 那么后面肯定插入过节点
            //3. 那么就得唤醒后继节点
            if (h != null && h.waitStatus != 0) {
                // 唤醒  下一个 阻塞/等待的节点
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }

    /**
     * Acquires in shared mode, ignoring interrupts.  Implemented by
     * first invoking at least once {@link #tryAcquireShared},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquireShared} until success.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *            {@link #tryAcquireShared} but is otherwise uninterpreted
     *            and can represent anything you like.
     */
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    /**
     * Acquires in shared mode, aborting if interrupted.  Implemented
     * by first checking interrupt status, then invoking at least once
     * {@link #tryAcquireShared}, returning on success.  Otherwise the
     * thread is queued, possibly repeatedly blocking and unblocking,
     * invoking {@link #tryAcquireShared} until success or the thread
     * is interrupted.
     *
     * @param arg the acquire argument.
     *            This value is conveyed to {@link #tryAcquireShared} but is
     *            otherwise uninterpreted and can represent anything
     *            you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    public final void acquireSharedInterruptibly(int arg) throws InterruptedException {
        //条件成立：说明当前调用 await() 方法的线程 已经是 中断状态了,直接抛出异常...
        if (Thread.interrupted()) throw new InterruptedException();
        // tryAcquireShared()方法是尝试获取锁
        //条件成立：说明当前 AQS.state > 0 ，说明此时应该线程入队，然后等待唤醒...
        //条件不成立：AQS.state == 0，此时就不会阻塞线程了,直接结束
        //对应业务层面 执行任务的线程已经将latch打破了 , 然后其他再调用latch.await()的线程，就不会在这里阻塞了
        //第一开始调用  latch.await() 的线程 返回的是 -1 < 0, 进入 这个逻辑;
        if (tryAcquireShared(arg) < 0) {
            //该方法的主要作用就是处理共享锁相关逻辑，如果共享锁获取失败时，就让线程进入到同步队列中park
            //在此处，如果计数器的值不为0，那么当前线程调用await()方法后，就会进入同步队列中park，
            // 直到有其他线程调用countDown()方法将计数器减为0时，才会将当前线程唤醒，或者当前线程被中断
            doAcquireSharedInterruptibly(arg);
        }
    }

    /**
     * Attempts to acquire in shared mode, aborting if interrupted, and
     * failing if the given timeout elapses.  Implemented by first
     * checking interrupt status, then invoking at least once {@link
     * #tryAcquireShared}, returning on success.  Otherwise, the
     * thread is queued, possibly repeatedly blocking and unblocking,
     * invoking {@link #tryAcquireShared} until success or the thread
     * is interrupted or the timeout elapses.
     *
     * @param arg          the acquire argument.  This value is conveyed to
     *                     {@link #tryAcquireShared} but is otherwise uninterpreted
     *                     and can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }

    /**
     * Releases in shared mode.  Implemented by unblocking one or more
     * threads if {@link #tryReleaseShared} returns true.
     *
     * @param arg the release argument.  This value is conveyed to
     *            {@link #tryReleaseShared} but is otherwise uninterpreted
     *            and can represent anything you like.
     * @return the value returned from {@link #tryReleaseShared}
     */
    public final boolean releaseShared(int arg) {
        //条件成立：说明当前调用 latch.countDown()  方法线程 正好是 state - 1 == 0 的这个线程，需要做触发唤醒 await 状态的线程
        if (tryReleaseShared(arg)) {
            //调用 countDown() 方法的线程 只有最后一个线程会进入到这个 if 块 里面，去调用 doReleaseShared() 唤醒 阻塞状态的线程的逻辑
            return true;
        }
        return false;
    }

    // Queue inspection methods

    /**
     * Queries whether any threads are waiting to acquire. Note that
     * because cancellations due to interrupts and timeouts may occur
     * at any time, a {@code true} return does not guarantee that any
     * other thread will ever acquire.
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     *
     * @return {@code true} if there may be other threads waiting to acquire
     */
    public final boolean hasQueuedThreads() {
        return head != tail;
    }

    /**
     * Queries whether any threads have ever contended to acquire this
     * synchronizer; that is if an acquire method has ever blocked.
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     *
     * @return {@code true} if there has ever been contention
     */
    public final boolean hasContended() {
        return head != null;
    }

    /**
     * Returns the first (longest-waiting) thread in the queue, or
     * {@code null} if no threads are currently queued.
     *
     * <p>In this implementation, this operation normally returns in
     * constant time, but may iterate upon contention if other threads are
     * concurrently modifying the queue.
     *
     * @return the first (longest-waiting) thread in the queue, or
     * {@code null} if no threads are currently queued
     */
    public final Thread getFirstQueuedThread() {
        // handle only fast path, else relay
        return (head == tail) ? null : fullGetFirstQueuedThread();
    }

    /**
     * Version of getFirstQueuedThread called when fastpath fails
     */
    private Thread fullGetFirstQueuedThread() {
        /*
         * The first node is normally head.next. Try to get its
         * thread field, ensuring consistent reads: If thread
         * field is nulled out or s.prev is no longer head, then
         * some other thread(s) concurrently performed setHead in
         * between some of our reads. We try this twice before
         * resorting to traversal.
         */
        Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (st = s.thread) != null) ||
                ((h = head) != null && (s = h.next) != null &&
                        s.prev == head && (st = s.thread) != null))
            return st;

        /*
         * Head's next field might not have been set yet, or may have
         * been unset after setHead. So we must check to see if tail
         * is actually first node. If not, we continue on, safely
         * traversing from tail back to head to find first,
         * guaranteeing termination.
         */

        Node t = tail;
        Thread firstThread = null;
        while (t != null && t != head) {
            Thread tt = t.thread;
            if (tt != null)
                firstThread = tt;
            t = t.prev;
        }
        return firstThread;
    }

    /**
     * Returns true if the given thread is currently queued.
     *
     * <p>This implementation traverses the queue to determine
     * presence of the given thread.
     *
     * @param thread the thread
     * @return {@code true} if the given thread is on the queue
     * @throws NullPointerException if the thread is null
     */
    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException();
        for (Node p = tail; p != null; p = p.prev)
            if (p.thread == thread)
                return true;
        return false;
    }

    /**
     * Returns {@code true} if the apparent first queued thread, if one
     * exists, is waiting in exclusive mode.  If this method returns
     * {@code true}, and the current thread is attempting to acquire in
     * shared mode (that is, this method is invoked from {@link
     * #tryAcquireShared}) then it is guaranteed that the current thread
     * is not the first queued thread.  Used only as a heuristic in
     * ReentrantReadWriteLock.
     */
    final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s;
        return (h = head) != null &&
                (s = h.next) != null &&
                !s.isShared() &&
                s.thread != null;
    }

    /**
     * Queries whether any threads have been waiting to acquire longer
     * than the current thread.
     *
     * <p>An invocation of this method is equivalent to (but may be
     * more efficient than):
     * <pre> {@code
     * getFirstQueuedThread() != Thread.currentThread() &&
     * hasQueuedThreads()}</pre>
     *
     * <p>Note that because cancellations due to interrupts and
     * timeouts may occur at any time, a {@code true} return does not
     * guarantee that some other thread will acquire before the current
     * thread.  Likewise, it is possible for another thread to win a
     * race to enqueue after this method has returned {@code false},
     * due to the queue being empty.
     *
     * <p>This method is designed to be used by a fair synchronizer to
     * avoid <a href="AbstractQueuedSynchronizer#barging">barging</a>.
     * Such a synchronizer's {@link #tryAcquire} method should return
     * {@code false}, and its {@link #tryAcquireShared} method should
     * return a negative value, if this method returns {@code true}
     * (unless this is a reentrant acquire).  For example, the {@code
     * tryAcquire} method for a fair, reentrant, exclusive mode
     * synchronizer might look like this:
     *
     * <pre> {@code
     * protected boolean tryAcquire(int arg) {
     *   if (isHeldExclusively()) {
     *     // A reentrant acquire; increment hold count
     *     return true;
     *   } else if (hasQueuedPredecessors()) {
     *     return false;
     *   } else {
     *     // try to acquire normally
     *   }
     * }}</pre>
     *
     * @return {@code true} if there is a queued thread preceding the
     * current thread, and {@code false} if the current thread
     * is at the head of the queue or the queue is empty
     * @since 1.7
     */
    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
                ((s = h.next) == null || s.thread != Thread.currentThread());
        //主要判断当前线程是否位于CLH 同步队列中的一个
    }


    // Instrumentation and monitoring methods

    /**
     * Returns an estimate of the number of threads waiting to
     * acquire.  The value is only an estimate because the number of
     * threads may change dynamically while this method traverses
     * internal data structures.  This method is designed for use in
     * monitoring system state, not for synchronization
     * control.
     *
     * @return the estimated number of threads waiting to acquire
     */
    public final int getQueueLength() {
        int n = 0;
        for (Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate.  The elements of the
     * returned collection are in no particular order.  This method is
     * designed to facilitate construction of subclasses that provide
     * more extensive monitoring facilities.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire in exclusive mode. This has the same properties
     * as {@link #getQueuedThreads} except that it only returns
     * those threads waiting due to an exclusive acquire.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (!p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire in shared mode. This has the same properties
     * as {@link #getQueuedThreads} except that it only returns
     * those threads waiting due to a shared acquire.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a string identifying this synchronizer, as well as its state.
     * The state, in brackets, includes the String {@code "State ="}
     * followed by the current value of {@link #getState}, and either
     * {@code "nonempty"} or {@code "empty"} depending on whether the
     * queue is empty.
     *
     * @return a string identifying this synchronizer, as well as its state
     */
    public String toString() {
        int s = getState();
        String q = hasQueuedThreads() ? "non" : "";
        return super.toString() +
                "[State = " + s + ", " + q + "empty queue]";
    }


    // Internal support methods for Conditions

    /**
     * Returns true if a node, always one that was initially placed on
     * a condition queue, is now waiting to reacquire on sync queue.
     *
     * @param node the node
     * @return true if is reacquiring
     * 说明：
     * 目的：判断是否在竞争队列中
     * 注意：
     */
    final boolean isOnSyncQueue(Node node) {
        //条件一：node.waitStatus == Node.CONDITION 条件成立，说明当前node一定是在
        //条件队列，节点肯定不在同步队列中，因为只有在等待队列时，才会为-2 ;
        // 因为 signal 方法迁移节点到  同步队列 前，会将 node 的状态设置为 0 ; 状态不对 就会直接判断不在 返回  false
        //条件二：前置条件：node.waitStatus != Node.CONDITION   ===>
        // 1.node.waitStatus == 0   (表示当前节点已经被 signal 了)
        // 2.node.waitStatus == 1 （当前线程是未持有锁调用 await 方法..最终会将 node 的状态修改为 取消状态..）
        // node.waitStatus == 0     为什么还要判断 node.prev == null ?
        // 因为 signal 方法 是先修改状态，再迁移
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;

        // 执行到这里，会是哪种情况 ？
        // node.waitStatus != CONDITION 且 node.prev != null  ===> 可以排除掉 node.waitStatus == 1 取消状态..
        //为什么可以排除取消状态？ 因为 signal 方法是不会把 取消状态的 node 迁移走的
        //设置 prev 引用的逻辑 是 迁移 阻塞队列 逻辑的设置的（enq()）
        //入阻塞队列的逻辑：1.设置 node.prev = tail;   2. cas 当前 node 为 阻塞队列的 tail 尾节点 成功才算是真正进入到 阻塞队列！ 3.pred.next = node
        //可以推算出，就算 prev 不是 null，也不能说明当前 node 已经成功入队到  阻塞队列 了

        // 在上一个判断的前提条件下，如果后置节点不为空，那么当前节点肯定在同步队列中
        //条件成立：说明当前节点已经成功入队到 同步队列，且当前节点后面已经有其它node了...
        if (node.next != null) // If has successor, it must be on queue
            return true;

        /**
         * 执行到这里，说明当前节点的状态为：node.prev != null 且 node.waitStatus == 0
         * findNodeFromTail 从 同步队列 的尾巴开始向前遍历查找 node ，如果查找到 返回 true,查找不到返回 false
         * 当前 node 有可能正在 signal 过程中，正在迁移中...还未完成...
         */
        return findNodeFromTail(node);
    }

    /**
     * Returns true if node is on sync queue by searching backwards from tail.
     * Called only when needed by isOnSyncQueue.
     *
     * @return true if present
     */
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        for (; ; ) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }

    /**
     * Transfers a node from a condition queue onto sync queue.
     * Returns true if successful.
     *
     * @param node the node
     * @return true if successfully transferred (else the node was
     * cancelled before signal)
     * 说明：在条件队列中进行调用的
     * 目的：将节点从条件等待队列移到同步队列
     * 注意：
     */
    final boolean transferForSignal(Node node) {
        // 将节点的waitStatus的值从-2改为-1。这里如果出现CAS失败，说明节点的waitStatus值被修改过，在条件等待队列中，只有当线程被取消后，才会去修改waitStatus
        // cas 修改当前节点的状态，修改为 0，因为当前节点马上要迁移到 同步队列 了
        // 成功：当前节点在条件队列中状态正常
        // 失败：1. 取消状态 （线程 await() 时 并没有未持有锁，最终线程对应的 node 会设置为 取消状态，错误使用）
        //            2. node 对应的线程 挂起期间，被其它线程使用 中断信号 唤醒过...（就会主动进入到 同步队列，这时也会修改状态为 0 ，还挺厉害）
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
            return false;
        }

        // enq 最终会将当前 node 入队到 阻塞队列，p 是当前节点在阻塞队列的 前驱节点
        Node p = enq(node);

        // ws 前驱节点的状态..
        int ws = p.waitStatus;

        //条件一：ws > 0 成立：说明前驱节点的状态在阻塞队列中是 取消状态 , 唤醒当前节点。
        //条件二：前置条件(ws <= 0)，
        //compareAndSetWaitStatus(p, ws, Node.SIGNAL) 返回true  表示设置前驱节点状态为 SIGNAl 状态成功
        //compareAndSetWaitStatus(p, ws, Node.SIGNAL) 返回false  ===> 什么时候会 false?
        //当前驱 node 对应的线程 是 lockInterrupt 入队的 node 时，是会响应中断的，外部线程给 前驱线程 中断信号之后，前驱 node 会将
        //状态修改为 取消状态 ，并且执行 出队逻辑..
        //前驱节点状态 只要不是 0 或者 -1 那么，就唤醒当前线程
        if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL)) {
            //唤醒当前 node 对应的线程...回头再说
            LockSupport.unpark(node.thread);
        }
        return true;
    }

    /**
     * Transfers node, if necessary, to sync queue after a cancelled wait.
     * Returns true if thread was cancelled before being signalled.
     *
     * @param node the node
     * @return true if cancelled before the node was signalled
     */
    final boolean transferAfterCancelledWait(Node node) {
        //条件成立：说明当前node一定是在 条件队列内(被唤醒的)，因为 signal 迁移节点到阻塞队列时，会将节点的状态修改为0
        if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
            // 中断唤醒的 node 也会被加入到 阻塞队列中！！
            // 而且并没有执行 断链操作
            enq(node);
            // true：表示是在条件队列内被中断的.
            return true;
        }

        //执行到这里有几种情况？
        //1.当前 node 已经被外部线程调用 signal 方法将其迁移到 阻塞队列内了；
        //2.当前 node 正在被外部线程调用 signal 方法将其迁移至 阻塞队列中 ，还没有完成，进行中状态..

        /*
         * If we lost out to a signal(), then we can't proceed
         * until it finishes its enq().  Cancelling during an
         * incomplete transfer is both rare and transient, so just
         * spin.
         */
        while (!isOnSyncQueue(node))

            Thread.yield();

        //false: 表示当前节点被中断唤醒时 不在 条件队列了..
        return false;
    }

    /**
     * Invokes release with current state value; returns saved state.
     * Cancels node and throws exception on failure.
     *
     * @param node the condition node for this wait
     * @return previous sync state
     * 说明：
     * 目的：完全释放掉当前线程对应的锁，并且返回之前持有锁的状态
     * 注意：
     */
    final int fullyRelease(Node node) {
        //完全释放锁是否成功，当failed失败时，说明当前线程是未持有锁调用 await 方法的线程..（错误写法..）
        // 假设失败，在 finally 代码块中 会将刚刚加入到 条件队列的 当前线程对应的  node 状态 修改为 取消状态
        //后继线程就会将 取消状态的 节点 给清理出去了..
        boolean failed = true;
        try {
            // 获取当前线程 所持有的 state 值 总数！
            int savedState = getState();

            // 释放锁，注意此时将同步变量的值传入进去了，如果是重入锁，且被重入过，那么此时savedState的值大于1
            // 此时释放锁时，会将同步变量state的值减为0。（通常可重入锁在释放锁时，每次只会将state减1，重入了几次就要释放几次，在这里是一下子全部释放）。
            if (release(savedState)) {
                //失败标记设置为 false = 不失败
                failed = false;
                // 返回当前线程释放的 state 值
                // 为什么要返回 savedState？
                // 我：调用 await() 后需要释放锁进入条件队列，然后可能进入竞争队列 , 当在竞争队列中被选中执行完业务代码后就需要执行接下来的unlock()逻辑,这就需要再次对状态进行--操作了；
                //小刘老师：因为在当你被迁移到“阻塞队列”后，再次被唤醒，且当前 node 在阻塞队列中是 head.next 而且
                // 当前 lock 状态是 state == 0 的情况下，当前 node 可以获取到锁，此时需要将 state 设置为 savedState
                return savedState;
            } else {
                // 当线程没有获取到锁时，调用该方法会释放失败，会抛出异常。
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = Node.CANCELLED;
        }
    }

    // Instrumentation methods for conditions

    /**
     * Queries whether the given ConditionObject
     * uses this synchronizer as its lock.
     *
     * @param condition the condition
     * @return {@code true} if owned
     * @throws NullPointerException if the condition is null
     */
    public final boolean owns(ConditionObject condition) {
        return condition.isOwnedBy(this);
    }

    /**
     * Queries whether any threads are waiting on the given condition
     * associated with this synchronizer. Note that because timeouts
     * and interrupts may occur at any time, a {@code true} return
     * does not guarantee that a future {@code signal} will awaken
     * any threads.  This method is designed primarily for use in
     * monitoring of the system state.
     *
     * @param condition the condition
     * @return {@code true} if there are any waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *                                      is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this synchronizer
     * @throws NullPointerException         if the condition is null
     */
    public final boolean hasWaiters(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }

    /**
     * Returns an estimate of the number of threads waiting on the
     * given condition associated with this synchronizer. Note that
     * because timeouts and interrupts may occur at any time, the
     * estimate serves only as an upper bound on the actual number of
     * waiters.  This method is designed for use in monitoring of the
     * system state, not for synchronization control.
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *                                      is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this synchronizer
     * @throws NullPointerException         if the condition is null
     */
    public final int getWaitQueueLength(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }

    /**
     * Returns a collection containing those threads that may be
     * waiting on the given condition associated with this
     * synchronizer.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate. The elements of the
     * returned collection are in no particular order.
     *
     * @param condition the condition
     * @return the collection of threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *                                      is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this synchronizer
     * @throws NullPointerException         if the condition is null
     */
    public final Collection<Thread> getWaitingThreads(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();
    }

    /**
     * Condition implementation for a {@link
     * AbstractQueuedSynchronizer} serving as the basis of a {@link
     * Lock} implementation.
     *
     * <p>Method documentation for this class describes mechanics,
     * not behavioral specifications from the point of view of Lock
     * and Condition users. Exported versions of this class will in
     * general need to be accompanied by documentation describing
     * condition semantics that rely on those of the associated
     * {@code AbstractQueuedSynchronizer}.
     *
     * <p>This class is Serializable, but all fields are transient,
     * so deserialized conditions have no waiters.
     */
    //内部类是可以访问到外部域的
    public class ConditionObject implements Condition, java.io.Serializable {

        private static final long serialVersionUID = 1173984872572414699L;
        /**
         * First node of condition queue.
         * 第一个节点
         */
        private transient Node firstWaiter;
        /**
         * Last node of condition queue.
         * 条件队列的最后一个节点
         */
        private transient Node lastWaiter;

        /**
         * Creates a new {@code ConditionObject} instance.
         */
        public ConditionObject() {
        }

        // Internal methods

        /**
         * Adds a new waiter to wait queue.
         *
         * @return its new wait node
         * 说明：调用 await 方法的线程 都是 持锁状态的，也就是说 addConditionWaiter 这里不存在并发！
         * 目的：将调用 await 方法的线程 包装成 node 节点 并且加入到队列中 并返回当前的 node；
         * 注意：在追加链表尾部时：可能是一个空对队列；不是空队列但是链表尾节点是取消状态节点，因此先来一边清除接节点，然后再追加尾节点；
         */
        private Node addConditionWaiter() {
            // 获取当前条件队列的尾节点的引用 保存到局部变量 t 中
            Node t = lastWaiter;

            // 条件一：t != null 成立：说明当前条件队列中，已经有node元素了..
            // 条件二：node 在 条件队列中时，它的状态是 CONDITION（-2）
            //  t.waitStatus != Node.CONDITION 成立：说明当前node发生中断了..
            if (t != null && t.waitStatus != Node.CONDITION) {
                // 清理条件队列中所有取消状态的节点 即waitStatus=1的Node节点
                unlinkCancelledWaiters();
                // 更新局部变量 t 为最新队尾引用，因为上面 unlinkCancelledWaiters 可能会更改 lastWaiter 引用
                t = lastWaiter;
            }

            //为当前线程创建node节点，设置状态为 CONDITION(-2)
            Node node = new Node(Thread.currentThread(), Node.CONDITION);

            //条件成立：说明条件队列中没有任何元素，当前线程是第一个进入队列的元素 == 如果等待队列还没有初始化
            //让 firstWaiter 指向当前 node
            if (t == null) {
                firstWaiter = node;
            }
            // 说明当前条件队列已经有其它node了 ，做追加操作
            else {
                t.nextWaiter = node;
            }

            //更新队尾引用指向 当前node。
            lastWaiter = node;
            //返回当前线程的node
            return node;
        }

        /**
         * Removes and transfers nodes until hit non-cancelled one or
         * null. Split out from signal in part to encourage compilers
         * to inline the case of no waiters.
         *
         * @param first (non-null) the first node on condition queue
         *              doSignal()方法的作用就是将等待队列的头节点从等待队列中移除，然后调用transferForSignal()方法将其加入到同步队列中，
         *              当transferForSignal()返回true时，表示节点被成功移到了同步队列中，返回false表示移动失败，只有一种情况会移动失败，
         *              那就是线程被取消了
         *              链接：https://juejin.cn/post/6844903987385204750
         */
        private void doSignal(Node first) {
            do {
                // firstWaiter = first.nextWaiter 因为当前 first 马上要出条件队列了，所以更新 firstWaiter 为 当前节点的下一个节点..
                // 如果当前节点的下一个节点 是 null，说明条件队列只有当前一个节点了...当前出队后，整个队列就空了..
                // 所以需要更新 lastWaiter = null
                if ((firstWaiter = first.nextWaiter) == null) {
                    lastWaiter = null;
                }
                // 当前 first 节点 出 条件队列，断开和下一个节点的关系.
                first.nextWaiter = null;

                // transferForSignal(first)
                // boolean：true 当前 first 节点迁移到阻塞队列成功  ,取反 直接退出 ;   false 迁移失败...取反继续执行
                // while循环 ：(first = firstWaiter) != null  当前 first 迁移失败，则将 first 更新为 first.next 继续尝试迁移..
                // 直至迁移某一个节点成功，或者 条件队列为 null 为止
                //因为当transferForSignal()返回false表示 条件等待队列中的，队列的头结点的状态是 取消状态，不能将它移到同步队列中，所以需要继续从条件等待队列找没有被取消的节点
            } while (!transferForSignal(first) && (first = firstWaiter) != null);
        }

        /**
         * Removes and transfers all nodes.
         *
         * @param first (non-null) the first node on condition queue
         */
        private void doSignalAll(Node first) {
            lastWaiter = firstWaiter = null;
            // 通过do...while循环，遍历等待队列的所有节点
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;
                // transferForSignal()方法将节点移到到同步队列
                transferForSignal(first);
                first = next;
            } while (first != null);
        }

        /**
         * Unlinks cancelled waiter nodes from condition queue.
         * Called only while holding lock. This is called when
         * cancellation occurred during condition wait, and upon
         * insertion of a new waiter when lastWaiter is seen to have
         * been cancelled. This method is needed to avoid garbage
         * retention in the absence of signals. So even though it may
         * require a full traversal, it comes into play only when
         * timeouts or cancellations occur in the absence of
         * signals. It traverses all nodes rather than stopping at a
         * particular target to unlink all pointers to garbage nodes
         * without requiring many re-traversals during cancellation
         * storms.
         */
        private void unlinkCancelledWaiters() {
            //表示循环当前节点，从链表的第一个节点开始 向后迭代处理.
            Node t = firstWaiter;
            //当前链表上一个正常状态的node节点
            Node trail = null;

            while (t != null) {
                //当前节点的下一个节点.
                Node next = t.nextWaiter;
                //条件成立：说明当前节点状态为 取消状态
                if (t.waitStatus != Node.CONDITION) {
                    //更新 nextWaiter为null
                    t.nextWaiter = null;
                    //条件成立：说明遍历到的节点还未碰到过正常节点..
                    if (trail == null) {
                        //更新firstWaiter指针为下个节点就可以
                        firstWaiter = next;
                    }
                    //让上一个正常节点指向 取消节点的 下一个节点..中间有问题的节点 被跳过去了..
                    else {
                        trail.nextWaiter = next;
                    }

                    //条件成立：当前节点为队尾节点了，更新lastWaiter 指向最后一个正常节点 就Ok了
                    if (next == null) {
                        lastWaiter = trail;
                    }
                }
                //条件不成立执行到else，说明当前节点是正常节点
                else {
                    trail = t;
                }

                t = next;
            }
        }

        // public methods

        /**
         * Moves the longest-waiting thread, if one exists, from the
         * wait queue for this condition to the wait queue for the
         * owning lock.
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         *                                      目的：
         */
        public final void signal() {
            //判断调用signal方法的线程是否是独占锁持有线程，如果不是，直接抛出异常..
            if (!isHeldExclusively()) throw new IllegalMonitorStateException();
            //获取 条件队列的 第一个 node
            Node first = firstWaiter;
            if (first != null) {
                //第一个节点不为 null，则将第一个节点 进行迁移到 同步队列 的逻辑..
                doSignal(first);
            }
        }

        /**
         * Moves all threads from the wait queue for this condition to
         * the wait queue for the owning lock.
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         *                                      signalAll()方法的作用就是唤醒等待队列中的所有节点，而signal()方法只唤醒等待队列的第一个节点
         */
        public final void signalAll() {
            if (!isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }
            Node first = firstWaiter;
            if (first != null) {
                doSignalAll(first);
            }
        }

        /**
         * Implements uninterruptible condition wait.
         * <ol>
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * </ol>
         */
        public final void awaitUninterruptibly() {
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }

        /*
         * For interruptible waits, we need to track whether to throw
         * InterruptedException, if interrupted while blocked on
         * condition, versus reinterrupt current thread, if
         * interrupted while blocked waiting to re-acquire.
         */

        /**
         * Mode meaning to reinterrupt on exit from wait
         * 模式意味着在等待退出时重新中断
         */
        private static final int REINTERRUPT = 1;
        /**
         * Mode meaning to throw InterruptedException on exit from wait
         */
        private static final int THROW_IE = -1;

        /**
         * Checks for interrupt, returning THROW_IE if interrupted
         * before signalled, REINTERRUPT if after signalled, or
         * 0 if not interrupted.
         */
        private int checkInterruptWhileWaiting(Node node) {
            //Thread.interrupted() 返回当前线程中断标记位，并且重置当前标记位 为 false
            return Thread.interrupted() ?
                    //TransferAfterCancelledWait 这个方法只有在线程是 被 中断唤醒时 才会调用！
                    // transferAfterCancelledWait 判断节点是否在 条件队列 中被中断的，若是 则返回 true 否则 返回false；
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) : 0;
        }

        /**
         * Throws InterruptedException, reinterrupts current thread, or
         * does nothing, depending on mode.
         */
        private void reportInterruptAfterWait(int interruptMode) throws InterruptedException {
            //条件成立：说明在条件队列内发生过中断，此时await方法抛出中断异常
            if (interruptMode == THROW_IE) {
                throw new InterruptedException();
            }
            // 条件成立：说明在条件队列外发生的中断，此时设置当前线程的中断标记位 为 true
            // 中断处理 交给 你的业务处理，如果你不处理，那什么事 也不会发生了...
            else if (interruptMode == REINTERRUPT) {
                selfInterrupt();
            }
        }

        /**
         * Implements interruptible condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled or interrupted.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         * 加锁访问的；
         */
        public final void await() throws InterruptedException {
            //是否是中断状态
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            //将调用 await 方法的线程 包装成 node 节点 并且加入到条件队列中 并返回当前的 node
            Node node = addConditionWaiter();

            // 完全释放掉当前线程对应的锁（将state置为0）
            // 为什么要释放锁呢？  加着锁 挂起后，谁还能救你呢？
            int savedState = fullyRelease(node);

            //  0  在 condition 队列挂起期间未接收过过中断信号
            // -1 在 condition 队列挂起期间接收到中断信号了
            // 1  在 condition 队列挂起期间为未接收到中断信号，但是迁移到“阻塞队列”之后 接收过中断信号
            int interruptMode = 0;

            // isOnSyncQueue 返回 true 表示当前线程对应的 node 已经迁移到 “阻塞队列” 了
            // 返回 false 说明当前 node 仍然还在 条件队列 中，需要继续park！
            // 判断同步节点是否在同步队列中，
            // 如果在当前线程释放锁后，锁被其他线程抢到了，此时当前节点就不在同步队列中了
            // 如果锁没有被抢占到，节点就会再同步队列中(当前线程是持有锁的线程，所以它是头结点)
            while (!isOnSyncQueue(node)) {
                // 挂起当前 node 对应的线程， 接下来去看 signal 过程...
                LockSupport.park(this);
                //挂起就需要 唤醒了

                //醒来后会判断自己在等待过程中有没有被中断过...
                //什么时候会被唤醒？都有几种情况呢？
                //1.常规路径：外部线程获取到 lock 之后，调用了 signal() 方法 转移条件队列的头节点到 阻塞队列， 当这个节点获取到锁后，会唤醒
                //2.转移至阻塞队列后，发现阻塞队列中的前驱节点状态 是 取消状态，此时会唤醒当前节点
                //3.当前节点挂起期间，被外部线程使用中断唤醒..

                //checkInterruptWhileWaiting() ：就算在 condition 队列挂起期间 线程发生中断了，对应的 node 也会被迁移到 “阻塞队列”
                // checkInterruptWhileWaiting() :
                //       返回0表示没有被中断过
                //       返回-1表示需要抛出异常
                //       返回1表示需要重置中断标识
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }

            // 执行到这里，就说明 当前 node 已经迁移到 “条件阻塞队列”了

            // acquireQueued() ：竞争队列的逻辑..
            // 条件一：返回 true 表示在阻塞队列中 被外部线程 中断 唤醒过...
            // 条件二：interruptMode != THROW_IE 成立，说明当前 node 在条件队列内 未发生过中断
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                // 设置 interruptMode = REINTERRUPT
                interruptMode = REINTERRUPT;

            //考虑下 node.nextWaiter != null 条件什么时候成立呢？
            //其实是 node 在条件队列内时 如果被外部线程 中断唤醒时，会加入到阻塞队列，但是并未设置nextWaiter = null
            // 当条件等待队列中还有其他线程在等待时，需要判断条件等待队列中有没有线程被取消，如果有，则将它们清除
            if (node.nextWaiter != null) { // clean up if cancelled
                // 清理条件队列内取消状态的节点..
                unlinkCancelledWaiters();
            }

            //条件成立：说明挂起期间 发生过中断（1.条件队列内的挂起 2.条件队列之外的挂起）
            if (interruptMode != 0) {
                reportInterruptAfterWait(interruptMode);
            }
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         */
        public final long awaitNanos(long nanosTimeout) throws InterruptedException {
            if (Thread.interrupted()) throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            int interruptMode = 0;

            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    //挂起
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }

            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return deadline - System.nanoTime();
        }

        /**
         * Implements absolute timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * <li> If timed out while blocked in step 4, return false, else true.
         * </ol>
         */
        public final boolean awaitUntil(Date deadline) throws InterruptedException {
            long abstime = deadline.getTime();
            if (Thread.interrupted()) throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * <li> If timed out while blocked in step 4, return false, else true.
         * </ol>
         */
        public final boolean await(long time, TimeUnit unit) throws InterruptedException {
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        //  support for instrumentation

        /**
         * Returns true if this condition was created by the given
         * synchronization object.
         *
         * @return {@code true} if owned
         */
        final boolean isOwnedBy(AbstractQueuedSynchronizer sync) {
            return sync == AbstractQueuedSynchronizer.this;
        }

        /**
         * Queries whether any threads are waiting on this condition.
         * Implements {@link AbstractQueuedSynchronizer#hasWaiters(ConditionObject)}.
         *
         * @return {@code true} if there are any waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        protected final boolean hasWaiters() {
            if (!isHeldExclusively()) throw new IllegalMonitorStateException();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    return true;
            }
            return false;
        }

        /**
         * Returns an estimate of the number of threads waiting on this condition.
         * 返回在此条件下等待的线程数的估计值。
         * Implements {@link AbstractQueuedSynchronizer#getWaitQueueLength(ConditionObject)}.
         *
         * @return the estimated number of waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        protected final int getWaitQueueLength() {
            if (!isHeldExclusively()) throw new IllegalMonitorStateException();
            int n = 0;
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    ++n;
            }
            return n;
        }

        /**
         * Returns a collection containing those threads that may be
         * waiting on this Condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitingThreads(ConditionObject)}.
         *
         * @return the collection of threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively()) throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<Thread>();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }

    /**
     * Setup to support compareAndSet. We need to natively implement
     * this here: For the sake of permitting future enhancements, we
     * cannot explicitly subclass AtomicInteger, which would be
     * efficient and useful otherwise. So, as the lesser of evils, we
     * natively implement using hotspot intrinsics API. And while we
     * are at it, we do the same for other CASable fields (which could
     * otherwise be done with atomic field updaters).
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     * CAS head field. Used only by enq.
     */
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    /**
     * CAS tail field. Used only by enq.
     */
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /**
     * CAS waitStatus field of a node.
     */
    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    /**
     * CAS next field of a node.
     */
    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
