package java.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A synchronization aid that allows a set of threads to all wait for
 * each other to reach a common barrier point.  CyclicBarriers are
 * useful in programs involving a fixed sized party of threads that
 * must occasionally wait for each other. The barrier is called
 * <em>cyclic</em> because it can be re-used after the waiting threads
 * are released.
 *
 * <p>A {@code CyclicBarrier} supports an optional {@link Runnable} command
 * that is run once per barrier point, after the last thread in the party
 * arrives, but before any threads are released.
 * This <em>barrier action</em> is useful
 * for updating shared-state before any of the parties continue.
 *
 * <p><b>Sample usage:</b> Here is an example of using a barrier in a
 * parallel decomposition design:
 *
 * <pre> {@code
 * class Solver {
 *   final int N;
 *   final float[][] data;
 *   final CyclicBarrier barrier;
 *
 *   class Worker implements Runnable {
 *     int myRow;
 *     Worker(int row) { myRow = row; }
 *     public void run() {
 *       while (!done()) {
 *         processRow(myRow);
 *
 *         try {
 *           barrier.await();
 *         } catch (InterruptedException ex) {
 *           return;
 *         } catch (BrokenBarrierException ex) {
 *           return;
 *         }
 *       }
 *     }
 *   }
 *
 *   public Solver(float[][] matrix) {
 *     data = matrix;
 *     N = matrix.length;
 *     Runnable barrierAction =
 *       new Runnable() { public void run() { mergeRows(...); }};
 *     barrier = new CyclicBarrier(N, barrierAction);
 *
 *     List<Thread> threads = new ArrayList<Thread>(N);
 *     for (int i = 0; i < N; i++) {
 *       Thread thread = new Thread(new Worker(i));
 *       threads.add(thread);
 *       thread.start();
 *     }
 *
 *     // wait until done
 *     for (Thread thread : threads)
 *       thread.join();
 *   }
 * }}</pre>
 * <p>
 * Here, each worker thread processes a row of the matrix then waits at the
 * barrier until all rows have been processed. When all rows are processed
 * the supplied {@link Runnable} barrier action is executed and merges the
 * rows. If the merger
 * determines that a solution has been found then {@code done()} will return
 * {@code true} and each worker will terminate.
 *
 * <p>If the barrier action does not rely on the parties being suspended when
 * it is executed, then any of the threads in the party could execute that
 * action when it is released. To facilitate this, each invocation of
 * {@link #await} returns the arrival index of that thread at the barrier.
 * You can then choose which thread should execute the barrier action, for
 * example:
 * <pre> {@code
 * if (barrier.await() == 0) {
 *   // log the completion of this iteration
 * }}</pre>
 *
 * <p>The {@code CyclicBarrier} uses an all-or-none breakage model
 * for failed synchronization attempts: If a thread leaves a barrier
 * point prematurely because of interruption, failure, or timeout, all
 * other threads waiting at that barrier point will also leave
 * abnormally via {@link BrokenBarrierException} (or
 * {@link InterruptedException} if they too were interrupted at about
 * the same time).
 *
 * <p>Memory consistency effects: Actions in a thread prior to calling
 * {@code await()}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions that are part of the barrier action, which in turn
 * <i>happen-before</i> actions following a successful return from the
 * corresponding {@code await()} in other threads.
 *
 * @author Doug Lea
 * 简单来说：CyclicBarrier允许⼀组线程互相等待，直到到达某个公共屏障点。
 * 叫做cyclic是因为当所有等待线程都被释放以后，CyclicBarrier可以被重⽤(对⽐于CountDownLatch是不能重⽤的)
 * 使⽤说明：
 * CountDownLatch注重的是等待其他线程完成，其他线程都已经完成了,消亡了, 主流程再自己走;
 * CountDownLatch的计数器减为0后，不会重置，因此不能重复使用.
 * CyclicBarrier注重的是：当线程到达某个状态后，暂停下来等待其他线程，所有线程均到达以后，所有线程继可以执⾏,不会消亡;
 * CyclicBarrier的计数器减为0后，可以重置计数器，从而可以再次使用，这一点通过类名中含有Cyclic（循环）就能看出.
 */
public class CyclicBarrier {
    /**
     * Each use of the barrier is represented as a generation instance.
     * The generation changes whenever the barrier is tripped, or
     * is reset. There can be many generations associated with threads
     * using the barrier - due to the non-deterministic way the lock
     * may be allocated to waiting threads - but only one of these
     * can be active at a time (the one to which {@code count} applies)
     * and all the rest are either broken or tripped.
     * There need not be an active generation if there has been a break
     * but no subsequent reset.
     */
    private static class Generation {
        //表示当前“代”是否被打破，如果代被打破 ，那么再来到这一代的线程 就会直接抛出 BrokenException异常
        //且在这一 代 挂起的线程 都会被唤醒，然后抛出 BrokerException 异常
        boolean broken = false;
    }

    /**
     * The lock for guarding barrier entry
     */
    //因为 barrier 实现是依赖于 Condition 条件队列的，condition 条件队列必须依赖 lock 才能使用
    // 用来保证线程安全，防止多个线程同时修改count时，出现线程不安全的情况
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Condition to wait on until tripped
     */
    //线程挂起实现使用的 condition 队列，条件：当前 代 所有线程到位，这个条件队列内的线程 才会被唤醒
    // 等待队列
    private final Condition trip = lock.newCondition();


    /**
     * The number of parties
     */
    //记录计数器的初始值
    //Barrier需要参与进来的线程数量
    private final int parties;

    /* The command to run when tripped */
    //当前代 最后一个到位的线程 需要执行的事件
    //CyclicBarrier支持当计数器减为0后，先执行一个Runnable任务，然后执行阻塞在屏障处的线程
    private final Runnable barrierCommand;

    /**
     * The current generation
     * 表示barrier对象 当前 “代”
     * 当计数器重置时，也会重置该属性。当出现超时等待时，会令generation中的broken属性为true
     */
    private Generation generation = new Generation();

    /**
     * Number of parties still waiting. Counts down from parties to 0
     * on each generation.  It is reset to parties on each new
     * generation or when broken.
     */
    //计数器，当调用await()方法时，会令count减1
    //表示当前“代”还有多少个线程 未到位
    //初始值为parties
    private int count;

    /**
     * Updates state on barrier trip and wakes up everyone.
     * Called only while holding lock.
     */
    private void nextGeneration() {
        // 将在 trip 条件队列内挂起的线程 全部唤醒
        trip.signalAll();

        //重置count为parties
        // set up next generation
        count = parties;

        //开启新的一代..使用一个新的 generation对象，表示新的一代，新的一代和上一代 没有任何关系
        generation = new Generation();
    }

    /**
     * Sets current barrier generation as broken and wakes up everyone.
     * Called only while holding lock.
     */
    private void breakBarrier() {
        //将 代 中的broken设置为true，表示这一代是被打破了的，再来到这一代的线程，直接抛出异常.
        generation.broken = true;
        //重置count为parties
        count = parties;
        // 将在trip条件队列内挂起的线程 全部唤醒，唤醒后的线程 会检查当前代 是否是打破的，
        // 如果是打破的话，接下来的逻辑和 开启下一代 唤醒的逻辑不一样.
        trip.signalAll();
    }

    /**
     * Main barrier code, covering the various policies.
     * timed：表示当前调用await方法的线程是否指定了 超时时长，如果 true 表示 线程是响应超时的
     * nanos：线程等待超时时长 纳秒，如果timed == false ,nanos == 0
     */
    private int dowait(boolean timed, long nanos) throws InterruptedException, BrokenBarrierException, TimeoutException {
        //获取全局锁
        final ReentrantLock lock = this.lock;
        //加锁
        //为什么要加锁呢？
        //因为 barrier 的挂起 和 唤醒 依赖的组件是 condition
        lock.lock();

        try {
            //获取barrier当前的 “代”
            final Generation g = generation;
            //在第一次进入时，肯定为false（默认值就是false），该字段的值只有当线程调用await(long timeout,TimeUint unit)方法，且出现了超时情况，才会为true
            //如果当前代是已经被打破状态，则当前调用await方法的线程，直接抛出Broken异常
            if (g.broken) {
                throw new BrokenBarrierException();
            }

            //如果当前线程的中断标记位 为 true，则打破当前代，然后当前线程抛出 中断异常（让所有等待的线程醒来）
            if (Thread.interrupted()) {
                //1.设置当前代的状态为broken状态  2.唤醒在trip 条件队列内的线程
                breakBarrier();
                throw new InterruptedException();
            }
            //执行到这里，说明 当前线程中断状态是正常的 false， 当前代的broken为 false（未打破状态）
            //正常逻辑..
            //假设 parties 给的是 5，那么index对应的值为 4,3,2,1,0
            int index = --count;

            //条件成立：说明当前线程是最后一个到达barrier的线程，此时需要做什么呢？
            //如果递减后的结果为0，说明所有线程达到屏障
            if (index == 0) {  // tripped
                //标记：true表示 最后一个线程 执行cmd时未抛异常。  false，表示最后一个线程执行cmd时抛出异常了.
                //cmd就是创建 barrier对象时 指定的第二个 Runnable接口实现，这个可以为null
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    //条件成立：说明创建barrier对象时 指定 Runnable接口了，这个时候最后一个到达的线程 就需要执行这个接口任务
                    if (command != null) {
                        command.run();
                    }
                    //command.run()未抛出异常的话，那么线程会执行到这里。
                    ranAction = true;
                    // 在nextGeneration()会唤醒等待队列中的所有线程，边让计数器的count值重置
                    //1.唤醒 trip 条件队列内挂起的线程，被唤醒的线程 会依次 获取到 lock ，然后依次退出 await 方法
                    //2.重置 count 为 parties
                    //3.创建一个新的 generation 对象，表示新的一代
                    nextGeneration();
                    //返回0，因为当前线程是此 代 最后一个到达的线程，所以Index == 0
                    return 0;
                } finally {
                    //如果command.run()执行抛出异常的话，会进入到这里。
                    if (!ranAction) {
                        breakBarrier();
                    }
                }
            }

            // 执行到这里，说明当前线程 并不是最后一个到达 Barrier 的线程..此时需要进入一个自旋中...

            // loop until tripped, broken, interrupted, or timed out
            //自旋，一直到 条件满足、当前代被打破、线程被中断，等待超时
            for (; ; ) {
                try {
                    //条件成立：说明当前线程是不指定超时时间的
                    if (!timed) {
                        //当前线程 会 释放掉 lock，然后进入到 trip 条件队列的尾部，然后挂起自己，等待被唤醒
                        trip.await();
                    } else if (nanos > 0L) {
                        // 说明当前线程调用 await 方法时 是指定了 超时时间的！
                        nanos = trip.awaitNanos(nanos);
                    }
                } catch (InterruptedException ie) {
                    // 抛出中断异常，会进来这里
                    // 什么时候会抛出 InterruptedException 异常呢？
                    // Node 节点在 条件队列内 时 收到中断信号时 会抛出中断异常！

                    // 条件一：g == generation 成立，说明当前代并没有变化
                    // 条件二：! g.broken 当前代如果没有被打破，那么当前线程就去打破，并且抛出异常...
                    if (g == generation && !g.broken) {
                        //唤醒
                        breakBarrier();
                        throw ie;
                    } else {
                        //执行到 else  有几种情况？
                        //1.代 发生了变化，这个时候就不需要抛出中断异常了，因为 代已经更新了，这里唤醒后就走正常逻辑了..只不过设置下 中断标记
                        //2.代 没有发生变化，但是 代 被打破了，此时也不用返回中断异常，执行到下面的时候会抛出  brokenBarrier 异常。也记录下中断标记位
                        Thread.currentThread().interrupt();
                    }
                }

                //唤醒后，执行到这里，有几种情况？
                //1.正常情况，当前barrier开启了新的一代（trip.signalAll()）
                //2.当前Generation被打破，此时也会唤醒所有在trip上挂起的线程
                //3.当前线程trip中等待超时，然后主动转移到 阻塞队列 然后获取到锁 唤醒

                //条件成立：当前代已经被打破
                if (g.broken) {
                    //线程唤醒后依次抛出BrokenBarrier异常
                    throw new BrokenBarrierException();
                }

                //唤醒后，执行到这里，有几种情况？
                //1.正常情况，当前barrier开启了新的一代（trip.signalAll()）
                //3.当前线程trip中等待超时，然后主动转移到 阻塞队列 然后获取到锁 唤醒。

                //条件成立：说明当前线程挂起期间，最后一个线程到位了，然后触发了开启新的一代的逻辑，此时唤醒trip条件队列内的线程
                if (g != generation) {
                    //返回当前线程的index
                    return index;
                }

                //唤醒后，执行到这里，有几种情况？
                //3.当前线程trip中等待超时，然后主动转移到 阻塞队列 然后获取到锁 唤醒
                if (timed && nanos <= 0L) {
                    //打破 barrier
                    breakBarrier();
                    //抛出超时异常.
                    throw new TimeoutException();
                }
            }//for over

        } finally {
            lock.unlock();
        }
    }

    /**
     * Creates a new {@code CyclicBarrier} that will trip when the
     * given number of parties (threads) are waiting upon it, and which
     * will execute the given barrier action when the barrier is tripped,
     * performed by the last thread entering the barrier.
     *
     * @param parties       the number of threads that must invoke {@link #await}
     *                      before the barrier is tripped
     * @param barrierAction the command to execute when the barrier is
     *                      tripped, or {@code null} if there is no action
     * @throws IllegalArgumentException if {@code parties} is less than 1
     */
    // barrierAction是一个Runnable，当计数器减为0时，会先执行barrierAction，然后再打开屏障
    public CyclicBarrier(int parties, Runnable barrierAction) {
        //因为小于等于0 的barrier没有任何意义..
        if (parties <= 0) throw new IllegalArgumentException();

        this.parties = parties;
        //count的初始值 就是 parties，后面 当前代 每到位一个线程，count--
        this.count = parties;
        this.barrierCommand = barrierAction;
    }

    /**
     * Creates a new {@code CyclicBarrier} that will trip when the
     * given number of parties (threads) are waiting upon it, and
     * does not perform a predefined action when the barrier is tripped.
     *
     * @param parties the number of threads that must invoke {@link #await}
     *                before the barrier is tripped
     * @throws IllegalArgumentException if {@code parties} is less than 1
     */
    public CyclicBarrier(int parties) {
        // parties用来指定计数器的大小
        this(parties, null);
    }

    /**
     * Returns the number of parties required to trip this barrier.
     *
     * @return the number of parties required to trip this barrier
     */
    public int getParties() {
        return parties;
    }

    /**
     * Waits until all {@linkplain #getParties parties} have invoked
     * {@code await} on this barrier.
     *
     * <p>If the current thread is not the last to arrive then it is
     * disabled for thread scheduling purposes and lies dormant until
     * one of the following things happens:
     * <ul>
     * <li>The last thread arrives; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * one of the other waiting threads; or
     * <li>Some other thread times out while waiting for barrier; or
     * <li>Some other thread invokes {@link #reset} on this barrier.
     * </ul>
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>If the barrier is {@link #reset} while any thread is waiting,
     * or if the barrier {@linkplain #isBroken is broken} when
     * {@code await} is invoked, or while any thread is waiting, then
     * {@link BrokenBarrierException} is thrown.
     *
     * <p>If any thread is {@linkplain Thread#interrupt interrupted} while waiting,
     * then all other waiting threads will throw
     * {@link BrokenBarrierException} and the barrier is placed in the broken
     * state.
     *
     * <p>If the current thread is the last thread to arrive, and a
     * non-null barrier action was supplied in the constructor, then the
     * current thread runs the action before allowing the other threads to
     * continue.
     * If an exception occurs during the barrier action then that exception
     * will be propagated in the current thread and the barrier is placed in
     * the broken state.
     *
     * @return the arrival index of the current thread, where index
     * {@code getParties() - 1} indicates the first
     * to arrive and zero indicates the last to arrive
     * @throws InterruptedException   if the current thread was interrupted
     *                                while waiting
     * @throws BrokenBarrierException if <em>another</em> thread was
     *                                interrupted or timed out while the current thread was
     *                                waiting, or the barrier was reset, or the barrier was
     *                                broken when {@code await} was called, or the barrier
     *                                action (if present) failed due to an exception
     */
    //让线程等待在阻塞在屏障处，并令计数器减1，不支持超时等待
    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }

    /**
     * Waits until all {@linkplain #getParties parties} have invoked
     * {@code await} on this barrier, or the specified waiting time elapses.
     *
     * <p>If the current thread is not the last to arrive then it is
     * disabled for thread scheduling purposes and lies dormant until
     * one of the following things happens:
     * <ul>
     * <li>The last thread arrives; or
     * <li>The specified timeout elapses; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * one of the other waiting threads; or
     * <li>Some other thread times out while waiting for barrier; or
     * <li>Some other thread invokes {@link #reset} on this barrier.
     * </ul>
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then {@link TimeoutException}
     * is thrown. If the time is less than or equal to zero, the
     * method will not wait at all.
     *
     * <p>If the barrier is {@link #reset} while any thread is waiting,
     * or if the barrier {@linkplain #isBroken is broken} when
     * {@code await} is invoked, or while any thread is waiting, then
     * {@link BrokenBarrierException} is thrown.
     *
     * <p>If any thread is {@linkplain Thread#interrupt interrupted} while
     * waiting, then all other waiting threads will throw {@link
     * BrokenBarrierException} and the barrier is placed in the broken
     * state.
     *
     * <p>If the current thread is the last thread to arrive, and a
     * non-null barrier action was supplied in the constructor, then the
     * current thread runs the action before allowing the other threads to
     * continue.
     * If an exception occurs during the barrier action then that exception
     * will be propagated in the current thread and the barrier is placed in
     * the broken state.
     *
     * @param timeout the time to wait for the barrier
     * @param unit    the time unit of the timeout parameter
     * @return the arrival index of the current thread, where index
     * {@code getParties() - 1} indicates the first
     * to arrive and zero indicates the last to arrive
     * @throws InterruptedException   if the current thread was interrupted
     *                                while waiting
     * @throws TimeoutException       if the specified timeout elapses.
     *                                In this case the barrier will be broken.
     * @throws BrokenBarrierException if <em>another</em> thread was
     *                                interrupted or timed out while the current thread was
     *                                waiting, or the barrier was reset, or the barrier was broken
     *                                when {@code await} was called, or the barrier action (if
     *                                present) failed due to an exception
     */
    public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        return dowait(true, unit.toNanos(timeout));
    }

    /**
     * Queries if this barrier is in a broken state.
     *
     * @return {@code true} if one or more parties broke out of this
     * barrier due to interruption or timeout since
     * construction or the last reset, or a barrier action
     * failed due to an exception; {@code false} otherwise.
     */
    public boolean isBroken() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return generation.broken;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Resets the barrier to its initial state.  If any parties are
     * currently waiting at the barrier, they will return with a
     * {@link BrokenBarrierException}. Note that resets <em>after</em>
     * a breakage has occurred for other reasons can be complicated to
     * carry out; threads need to re-synchronize in some other way,
     * and choose one to perform the reset.  It may be preferable to
     * instead create a new barrier for subsequent use.
     */
    //重置屏障
    public void reset() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            breakBarrier();   // break the current generation
            nextGeneration(); // start a new generation
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the number of parties currently waiting at the barrier.
     * This method is primarily useful for debugging and assertions.
     *
     * @return the number of parties currently blocked in {@link #await}
     */
    public int getNumberWaiting() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return parties - count;
        } finally {
            lock.unlock();
        }
    }
}
