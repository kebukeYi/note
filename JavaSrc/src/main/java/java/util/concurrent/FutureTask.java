package java.util.concurrent;

import java.util.concurrent.locks.LockSupport;

/**
 * A cancellable asynchronous computation.  This class provides a base
 * implementation of {@link Future}, with methods to start and cancel
 * a computation, query to see if the computation is complete, and
 * retrieve the result of the computation.  The result can only be
 * retrieved when the computation has completed; the {@code get}
 * methods will block if the computation has not yet completed.  Once
 * the computation has completed, the computation cannot be restarted
 * or cancelled (unless the computation is invoked using
 * {@link #runAndReset}).
 *
 * <p>A {@code FutureTask} can be used to wrap a {@link Callable} or
 * {@link Runnable} object.  Because {@code FutureTask} implements
 * {@code Runnable}, a {@code FutureTask} can be submitted to an
 * {@link Executor} for execution.
 *
 * <p>In addition to serving as a standalone class, this class provides
 * {@code protected} functionality that may be useful when creating
 * customized task classes.
 *
 * @param <V> The result type returned by this FutureTask's {@code get} methods
 * @author Doug Lea
 * @since 1.5
 */
public class FutureTask<V> implements RunnableFuture<V> {
    /*
     * Revision notes: This differs from previous versions of this
     * class that relied on AbstractQueuedSynchronizer, mainly to
     * avoid surprising users about retaining interrupt status during
     * cancellation races. Sync control in the current design relies
     * on a "state" field updated via CAS to track completion, along
     * with a simple Treiber stack to hold waiting threads.
     *
     * Style note: As usual, we bypass overhead of using
     * AtomicXFieldUpdaters and instead directly use Unsafe intrinsics.
     */

    /**
     * The run state of this task, initially NEW.  The run state
     * transitions to a terminal state only in methods set,
     * setException, and cancel.  During completion, state may take on
     * transient values of COMPLETING (while outcome is being set) or
     * INTERRUPTING (only while interrupting the runner to satisfy a
     * cancel(true)). Transitions from these intermediate to final
     * states use cheaper ordered/lazy writes because values are unique
     * and cannot be further modified.
     * <p>
     * Possible state transitions:
     * NEW -> COMPLETING -> NORMAL
     * NEW -> COMPLETING -> EXCEPTIONAL
     * NEW -> CANCELLED
     * NEW -> INTERRUPTING -> INTERRUPTED
     */
    //当前task状态
    private volatile int state;

    //还未执行 默认
    private static final int NEW = 0;

    // 任务处于完成中，什么是完成中呢？有两种情况
    // 1. 任务正常被线程执行完成了，但是还没有将返回值赋值给outcome属性
    // 2. 任务在执行过程中出现了异常，被捕获了，然后处理异常了，在将异常对象赋值给outcome属性之前
    private static final int COMPLETING = 1;

    //当前任务正常结束
    private static final int NORMAL = 2;

    // 任务出了异常，并将异常对象赋值给outcome属性之后
    private static final int EXCEPTIONAL = 3;

    // 调用cancle(false)，任务被取消了
    private static final int CANCELLED = 4;

    // 调用cancle(true)，任务取消，但是在线程中断之前
    private static final int INTERRUPTING = 5;

    //  调用cancle(true)，任务取消，但是在线程中断之后
    private static final int INTERRUPTED = 6;

    /**
     * The underlying callable; nulled out after running
     * submit 提交（runable/callable）
     */
    private Callable<V> callable;

    /**
     * The result to return or exception to throw from get()
     * 正常 结果保存结果  callable 返回值
     * 非正常 callable 抛出异常，outcome 保存异常
     */
    private Object outcome; // non-volatile, protected by state reads/writes

    /**
     * The thread running the callable; CASed during run()
     * 当前任务被线程执行期间 保存当前执行任务线程的引用
     */
    private volatile Thread runner;

    /**
     * Treiber stack of waiting threads
     * 用来保存等待获取任务返回值的线程的等待队列，当我们在主线程中调用Future.get()方法时，就会将主线程封装成一个WaitNode。
     * 当有多个线程同时调用Future.get()方法时，WaitNode会通过next属性来维护一个链表
     * 采用头插法  保存 想得到结果的线程 节点
     */
    private volatile WaitNode waiters;

    /**
     * Returns result or throws exception for completed task.
     *
     * @param s completed state value
     */
    @SuppressWarnings("unchecked")
    private V report(int s) throws ExecutionException {
        //正常保存的是 callable 的结果
        //非正常时候 callable 抛出的异常
        Object x = outcome;
        //当前任务正常结束
        if (s == NORMAL) {
            return (V) x;
        }
        //被取消状态
        if (s >= CANCELLED) {
            //抛出异常
            throw new CancellationException();
        }
        throw new ExecutionException((Throwable) x);
    }

    /**
     * Creates a {@code FutureTask} that will, upon running, execute the
     * given {@code Callable}.
     *
     * @param callable the callable task
     * @throws NullPointerException if the callable is null
     */
    //可返回值
    public FutureTask(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        //业务类
        this.callable = callable;
        this.state = NEW;
    }

    /**
     * Creates a {@code FutureTask} that will, upon running, execute the
     * given {@code Runnable}, and arrange that {@code get} will return the
     * given result on successful completion.
     *
     * @param runnable the runnable task
     * @param result   the result to return on successful completion. If
     *                 you don't need a particular result, consider using
     *                 constructions of the form:
     *                 {@code Future<?> f = new FutureTask<Void>(runnable, null)}
     * @throws NullPointerException if the runnable is null
     */
    //
    public FutureTask(Runnable runnable, V result) {
        //使用适配器模式 改变runnable 为callable ，当有线程get操作上时 得到的结果可能为传进来的参数 result 的值  or 可能为 null
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;
    }

    @Override
    public boolean isCancelled() {
        return state >= CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state != NEW;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        //条件一：state == NEW  表示当前任务 处于运行中 或者 处于线程池 任务队列中
        if (!(state == NEW &&
                //条件二：UNSAFE.compareAndSwapInt(this, stateOffset, NEW, mayInterruptIfRunning ? INTERRUPTING : CANCELLED)
                //cas 尝试设置NEW 为 中断中 或者 取消状态  否则 设置失败
                UNSAFE.compareAndSwapInt(this, stateOffset, NEW,
                        mayInterruptIfRunning ? INTERRUPTING : CANCELLED)))
            return false;
        try {    // in case call to interrupt throws exception
            //需要发送中断信号
            if (mayInterruptIfRunning) {
                try {
                    //执行当前任务的线程 有可能是 null ，是null 的情况是 ：当前任务在队列中 还没有线程获取到它尼。
                    Thread t = runner;
                    //正在执行任务中
                    if (t != null) {
                        //给一个中断信号
                        //如果你的程序是响应中断逻辑的 那就处理中断逻辑
                        //否则什么也不会发生
                        t.interrupt();
                    }
                } finally {
                    //设置状态
                    UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED);
                }
            }
        } finally {
            //会将唤醒所有get（）阻塞的线程
            finishCompletion();
        }
        return true;
    }

    /**
     * @throws CancellationException {@inheritDoc}
     *                               不是一个线程在 get
     *                               多个线程在等待结果
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        //获取当前状态
        int s = state;
        //未执行、正在执行、正完成。调用 get 的外部线程会被阻塞住
        if (s <= COMPLETING) {
            //返回 任务的当前状态；线程可能睡了一会了
            s = awaitDone(false, 0L);
        }
        //执行返回
        return report(s);
    }

    /**
     * @throws CancellationException {@inheritDoc}
     */
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null)
            throw new NullPointerException();
        int s = state;
        // 如果状态处于NEW或者COMPLETING状态，表示任务还没有执行完成，需要等待
        if (s <= COMPLETING &&
                // awaitDone()进行等待
                (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING) {
            throw new TimeoutException();
        }
        // 返回结果
        return report(s);
    }

    /**
     * Protected method invoked when this task transitions to state
     * {@code isDone} (whether normally or via cancellation). The
     * default implementation does nothing.  Subclasses may override
     * this method to invoke completion callbacks or perform
     * bookkeeping. Note that you can query status inside the
     * implementation of this method to determine whether this task
     * has been cancelled.
     */
    protected void done() {
    }

    /**
     * Sets the result of this future to the given value unless
     * this future has already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
     * upon successful completion of the computation.
     *
     * @param v the value
     */
    //保存结果
    protected void set(V v) {
        //cas 把状态设置成 未完成状态
        //可能失败状态：外部线程等不及了 在等待 set 时 把任务取消掉了  不太可能发生
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            //结果赋值
            outcome = v;
            //再次赋值正常结束
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL);
            //唤醒waiters这个属性构成的等待队列中的线程
            finishCompletion();
        }
    }

    /**
     * Causes this future to report an {@link ExecutionException}
     * with the given throwable as its cause, unless this future has
     * already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
     * upon failure of the computation.
     *
     * @param t the cause of failure
     */
    //处理异常
    protected void setException(Throwable t) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            //拿到的是异常了 不再是结果了
            outcome = t;
            //设置状态
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL);
            //唤醒waiters这个属性构成的等待队列中的线程
            finishCompletion();
        }
    }

    //任务执行入口
    @Override
    public void run() {
        //条件一：说明任务已经执行过了  或者 被取消了
        if (state != NEW ||
                //条件二：cas 失败 说明任务已经被其他线程抢占了
                !UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread())) {
            return;
        }
        //执行到这里：当前线程抢占任务成功
        try {
            //自己封装逻辑的callable
            Callable<V> c = callable;
            //1. 防止空指针
            // 2. 防止外部被cancel 掉当前任务
            if (c != null && state == NEW) {
                V result;
                //表示callable.run 执行成功
                boolean ran;
                try {
                    //调用自己的 业务逻辑
                    // 执行任务
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    //表明自己写的代码存在bug了
                    result = null;
                    ran = false;
                    //处理异常
                    // 出异常时将state置为EXCEPTIONAL
                    setException(ex);
                }
                //任务成功执行保存结果
                if (ran) {
                    // 设置任务状态为COMPLETING，然后保存返回值，最后再设置为NORMAL
                    set(result);
                }
            }
        } finally {
            // runner must be non-null until state is settled to prevent concurrent calls to run()
            //设置线程为null
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            int s = state;
            if (s >= INTERRUPTING) {
                //
                handlePossibleCancellationInterrupt(s);
            }
        }
    }

    /**
     * Executes the computation without setting its result, and then
     * resets this future to initial state, failing to do so if the
     * computation encounters an exception or is cancelled.  This is
     * designed for use with tasks that intrinsically execute more
     * than once.
     *
     * @return {@code true} if successfully run and reset
     */
    //
    protected boolean runAndReset() {
        if (state != NEW ||
                !UNSAFE.compareAndSwapObject(this, runnerOffset,
                        null, Thread.currentThread()))
            return false;
        boolean ran = false;
        int s = state;
        try {
            Callable<V> c = callable;
            if (c != null && s == NEW) {
                try {
                    c.call(); // don't set result
                    ran = true;
                } catch (Throwable ex) {
                    setException(ex);
                }
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            s = state;
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
        return ran && s == NEW;
    }

    /**
     * Ensures that any interrupt from a possible cancel(true) is only
     * delivered to a task while in run or runAndReset.
     */
    //
    private void handlePossibleCancellationInterrupt(int s) {
        // It is possible for our interrupter to stall before getting a
        // chance to interrupt us.  Let's spin-wait patiently.
        if (s == INTERRUPTING) {
            while (state == INTERRUPTING) {
                Thread.yield(); // wait out pending interrupt
            }
        }

        // assert state == INTERRUPTED;

        // We want to clear any interrupt we may have received from
        // cancel(true).  However, it is permissible to use interrupts
        // as an independent mechanism for a task to communicate with
        // its caller, and there is no way to clear only the
        // cancellation interrupt.
        //
        // Thread.interrupted();
    }

    /**
     * Simple linked list nodes to record waiting threads in a Treiber
     * stack.  See other classes such as Phaser and SynchronousQueue
     * for more detailed explanation.
     */
    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }

    /**
     * Removes and signals all waiting threads, invokes done(), and
     * nulls out callable.
     */
    //
    private void finishCompletion() {
        //q=waiters 头节点
        for (WaitNode q; (q = waiters) != null; ) {
            //使用 cas waiters 置为 null  怕外部线程取消当前任务
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                //自选大法好
                for (; ; ) {
                    //获取当前节点封装的thread
                    Thread t = q.thread;
                    //说明当前线程不为null
                    if (t != null) {
                        //help gc
                        q.thread = null;
                        //503 行阻塞；唤醒当前节点对映的线程
                        LockSupport.unpark(t);
                    }
                    //获取下一个节点
                    WaitNode next = q.next;
                    //说明是最后一个节点
                    if (next == null) {
                        break;
                    }
                    //help gc
                    q.next = null;
                    //下一轮工作
                    q = next;
                }
                break;
            }
        }

        done();
        //将callable help gc
        callable = null;
    }

    /**
     * Awaits completion or aborts on interrupt or timeout.
     *
     * @param timed true if use timed waits
     * @param nanos time to wait, if timed
     * @return state upon completion
     */
    //需要进入等待状态 等待正确结果
    private int awaitDone(boolean timed, long nanos) throws InterruptedException {
        //不是超时
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        //当前线程封装成的节点对象
        WaitNode q = null;
        //表示当前线程节点 有没有入队
        boolean queued = false;
        //自旋
        for (; ; ) {
            //如果 是被其他线程使用中断方式被唤醒 后 会把 中断位 设置为 false 的
            if (Thread.interrupted()) {
                //出队
                removeWaiter(q);
                //get 返回中断异常
                throw new InterruptedException();
            }
            //假设当前线程是被其他线程 unpark 唤醒的话 会正常执行 自旋逻辑
            //获取最新的状态
            int s = state;
            //说明：当前任务有结果了 or 坏结果了
            if (s > COMPLETING) {
                //需要把当前线程封装的节点设置为null  help gc
                if (q != null) {
                    q.thread = null;
                }
                return s;
                //说明：任务 接近完后状态； 这里让当前任务 再释放 cpu , 进行下一次抢占 cpu , 为何?
            } else if (s == COMPLETING) {
                //主动让出 CPU
                Thread.yield();
                //条件成立：第一次自旋  当前线程还未创建节点对象   此时开始创建节点对象
            } else if (q == null) {
                q = new WaitNode();
                //条件：第二次自旋：当前线程已经创建节点对象了  还未入队  开始入队操作
            } else if (!queued) {
                //将当前线程节点的 next 指向 头节点   waiters 一直指向头节点
                q.next = waiters;
                //cas 设置 waiters 指向 当前线程 ；失败的话 可能别的线程抢先入队了
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset, waiters, q);
            }
            //条件：第三次自旋：超时参数会执行此逻辑
            else if (timed) {
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    removeWaiter(q);
                    return state;
                }
                //线程进入等待状态 会自动被唤醒
                LockSupport.parkNanos(this, nanos);
            } else {
                //当前 get 的线程会被park 阻塞掉，线程状态会变化曾waiting 状态 线程休眠了
                //除非有的线程 唤醒  or  中断 此线程
                LockSupport.park(this);
            }
        }

    }

    /**
     * Tries to unlink a timed-out or interrupted wait node to avoid
     * accumulating garbage.  Internal nodes are simply unspliced
     * without CAS since it is harmless if they are traversed anyway
     * by releasers.  To avoid effects of unsplicing from already
     * removed nodes, the list is retraversed in case of an apparent
     * race.  This is slow when there are a lot of nodes, but we don't
     * expect lists to be long enough to outweigh higher-overhead
     * schemes.
     */
    //出队方法
    private void removeWaiter(WaitNode node) {
        if (node != null) {
            node.thread = null;
            retry:
            for (; ; ) {          // restart on removeWaiter race
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    //那到头节点的下一个节点
                    s = q.next;
                    //
                    if (q.thread != null) {
                        pred = q;
                    } else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) // check for race
                            continue retry;
                    } else if (!UNSAFE.compareAndSwapObject(this, waitersOffset,
                            q, s))
                        continue retry;
                }
                break;
            }
        }
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = FutureTask.class;
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
