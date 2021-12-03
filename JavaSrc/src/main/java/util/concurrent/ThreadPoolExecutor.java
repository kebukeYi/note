package java.util.concurrent;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

/**
 * An {@link ExecutorService} that executes each submitted task using
 * one of possibly several pooled threads, normally configured
 * using {@link Executors} factory methods.
 *
 * <p>Thread pools address 解决 two different problems: they usually
 * provide improved performance when executing large numbers of
 * asynchronous tasks, due to reduced per-task invocation overhead,
 * and they provide a means of bounding and managing the resources,
 * including threads, consumed when executing a collection of tasks.
 * Each {@code ThreadPoolExecutor} also maintains some basic
 * statistics, such as the number of completed tasks.
 * 线程池一般用来解决两个好处:
 * 1线程重复利用,减少创建线程个数提高性能
 * 2捏供限定和管理资源的手段
 *
 * <p>To be useful across a wide range of contexts, this class
 * provides many adjustable parameters 可调参数 and extensibility
 * hooks. However, programmers are urged  督促 to use the more convenient
 * {@link Executors} factory methods {@link
 * Executors#newCachedThreadPool} (unbounded thread pool, with
 * automatic thread reclamation), {@link Executors#newFixedThreadPool}
 * (fixed size thread pool) and {@link
 * Executors#newSingleThreadExecutor} (single background thread), that
 * preconfigure settings for the most common usage
 * scenarios.情景 Otherwise, use the following guide when manually
 * configuring and tuning this class:
 *
 * <dl>
 *
 * <dt>Core and maximum pool sizes</dt>
 *
 * <dd>A {@code ThreadPoolExecutor} will automatically adjust the
 * pool size (see {@link #getPoolSize})
 * according to the bounds set by
 * corePoolSize (see {@link #getCorePoolSize}) and
 * maximumPoolSize (see {@link #getMaximumPoolSize}).
 * <p>
 * When a new task is submitted in method {@link #execute(Runnable)},
 * and fewer than corePoolSize threads are running, a new thread is
 * created to handle the request, even if other worker threads are
 * idle.  If there are more than corePoolSize but less than
 * maximumPoolSize threads running, a new thread will be created only
 * if the queue is full.  By setting corePoolSize and maximumPoolSize
 * the same, you create a fixed-size thread pool. By setting
 * maximumPoolSize to an essentially unbounded value such as {@code
 * Integer.MAX_VALUE}, you allow the pool to accommodate an arbitrary
 * number of concurrent tasks. Most typically, core and maximum pool
 * sizes are set only upon construction, but they may also be changed
 * dynamically using {@link #setCorePoolSize} and {@link
 * #setMaximumPoolSize}. </dd>
 * 例子:
 * 如果运行的线程少于 corePoolsize,则创建新线程
 * 来处理请求,即使其他辅助线程是空闲的
 * 2.如果运行的线程大于 core Poolsize,小于
 * maximum Poolsize,则仅当队列满时才创建新线程
 * 3.如果设置的 core Poolsize和 maximum Poolsize
 * 相同,则创建了固定大小的线程池
 * 4如果设置了 maximum Poolsize最大值,那么允许池
 * 适应任意数的并发任务
 *
 *
 * <dt>On-demand construction</dt> 按需初始化线程池
 *
 * <dd>By default, even core threads are initially created and
 * started only when new tasks arrive, but this can be overridden
 * dynamically using method {@link #prestartCoreThread} or {@link
 * #prestartAllCoreThreads}.  You probably want to prestart threads if
 * you construct the pool with a non-empty queue. </dd>
 * 默认:只有当任务请求过来,才会初始化核心线程当然了,可以使用方法重写!
 *
 * <dt>Creating new threads</dt>
 *
 * <dd>New threads are created using a {@link ThreadFactory}.  If not
 * otherwise specified, a {@link Executors#defaultThreadFactory} is
 * used, that creates threads to all be in the same {@link
 * ThreadGroup} and with the same {@code NORM_PRIORITY} priority and 相同优先级 没有守护线程 可自定义
 * non-daemon status. By supplying a different ThreadFactory, you can
 * alter the thread's name, thread group, priority, daemon status,
 * etc. If a {@code ThreadFactory} fails to create a thread when asked
 * by returning null from {@code newThread}, the executor will 如果线程创建失败( return nul)了,执行器会继
 * 续执行,但是不会执行任务
 * continue, but might not be able to execute any tasks. Threads
 * should possess the "modifyThread" {@code RuntimePermission}. If
 * worker threads or other threads using the pool do not possess this
 * permission, service may be degraded: configuration changes may not
 * take effect in a timely manner, and a shutdown pool may remain in a
 * state in which termination is possible but not completed.</dd>
 *
 * <dt>Keep-alive times</dt> 线程空闲时间
 *
 * <dd>If the pool currently has more than corePoolSize threads,
 * excess threads will be terminated if they have been idle for more
 * than the keepAliveTime (see {@link #getKeepAliveTime(TimeUnit)}).
 * This provides a means of reducing resource consumption when the
 * pool is not being actively used. If the pool becomes more active
 * later, new threads will be constructed. This parameter can also be
 * changed dynamically using method {@link #setKeepAliveTime(long,
 * TimeUnit)}.  Using a value of {@code Long.MAX_VALUE} {@link
 * TimeUnit#NANOSECONDS} effectively disables idle threads from ever
 * terminating prior to shut down. By default, the keep-alive policy
 * applies only when there are more than corePoolSize threads. But
 * method {@link #allowCoreThreadTimeOut(boolean)} can be used to
 * apply this time-out policy to core threads as well, so long as the
 * keepAliveTime value is non-zero. </dd>
 *
 * <dt>Queuing</dt>
 *
 * <dd>Any {@link BlockingQueue} may be used to transfer and hold
 * submitted tasks.  The use of this queue interacts with pool sizing:
 *
 * <ul>
 *
 * <li> If fewer than corePoolSize threads are running, the Executor
 * always prefers adding a new thread rather than queuing.</li>
 *
 * <li> If corePoolSize or more threads are running, the Executor
 * always prefers queuing a request rather than adding a new
 * thread.</li>
 *
 * <li> If a request cannot be queued, a new thread is created unless
 * this would exceed maximumPoolSize, in which case, the task will be
 * rejected.</li>
 *
 * </ul>
 * <p>
 * There are three general strategies for queuing:  三种队列策略
 * <ol>
 *
 * <li> <em> Direct handoffs.</em> A good default choice for a work
 * queue is a {@link SynchronousQueue} that hands off tasks to threads
 * without otherwise holding them. Here, an attempt to queue a task
 * will fail if no threads are immediately available to run it, so a
 * new thread will be constructed. This policy avoids lockups when
 * handling sets of requests that might have internal dependencies.
 * Direct handoffs generally require unbounded maximumPoolSizes to
 * avoid rejection of new submitted tasks. This in turn admits the
 * possibility of unbounded thread growth when commands continue to
 * arrive on average faster than they can be processed.  </li>
 * 1.该策略不会将任务放到队列中,而是会直接移交
 * 给执行它的线程
 * 2.如果当前没有线程执行它,那很可能会新建一个
 * 线程
 * 3.-般用于线程池是无界限的情况
 *
 * <li><em> Unbounded queues.</em> Using an unbounded queue (for
 * example a {@link LinkedBlockingQueue} without a predefined
 * capacity) will cause new tasks to wait in the queue when all
 * corePoolSize threads are busy. Thus, no more than corePoolSize
 * threads will ever be created. (And the value of the maximumPoolSize
 * therefore doesn't have any effect.)  This may be appropriate when
 * each task is completely independent of others, so tasks cannot
 * affect each others execution; for example, in a web page server.
 * While this style of queuing can be useful in smoothing out
 * transient bursts of requests, it admits the possibility of
 * unbounded work queue growth when commands continue to arrive on
 * average faster than they can be processed.  </li>
 * 无界限策略;
 * 1.如果所有的核心线程都在工作,那么新的线
 * 程会在队列中等待
 * 2因此,线程的创建不会多于核心线程的数三(
 * 其他的都在队列中等待了)
 *
 *
 * <li><em>Bounded queues.</em> A bounded queue (for example, an
 * {@link ArrayBlockingQueue}) helps prevent resource exhaustion when
 * used with finite maximumPoolSizes, but can be more difficult to
 * tune and control.  Queue sizes and maximum pool sizes may be traded
 * off for each other: Using large queues and small pools minimizes
 * CPU usage, OS resources, and context-switching overhead, but can
 * lead to artificially low throughput.  If tasks frequently block (for
 * example if they are I/O bound), a system may be able to schedule
 * time for more threads than you otherwise allow. Use of small queues
 * generally requires larger pool sizes, which keeps CPUs busier but
 * may encounter unacceptable scheduling overhead, which also
 * decreases throughput.  </li>
 * * 有界限策略
 *  * 1.避免资源耗尽的情况发生
 *  * 2.如果线程池比较小,而队列比较大。一
 *  * 定程度上减少内存的使用主。但是代价是
 *  * 限制吞吐!
 * </ol>
 *
 * </dd>
 *
 * <dt>Rejected tasks</dt>
 *
 * <dd>New tasks submitted in method {@link #execute(Runnable)} will be
 * <em>rejected</em> when the Executor has been shut down, and also when
 * the Executor uses finite bounds for both maximum threads and work queue
 * capacity, and is saturated.  In either case, the {@code execute} method
 * invokes the {@link
 * RejectedExecutionHandler#rejectedExecution(Runnable, ThreadPoolExecutor)}
 * method of its {@link RejectedExecutionHandler}.  Four predefined handler
 * policies are provided:
 * 线程池关闭
 * 2线程数满了和队列饱和了
 * 以上两种情况就会发生拒绝任务的时候,会
 * 抛出异常~
 * <ol>
 *
 * <li> In the default {@link ThreadPoolExecutor.AbortPolicy}, the
 * handler throws a runtime {@link RejectedExecutionException} upon
 * rejection. </li> 直接抛出异常 默认策略
 *
 * <li> In {@link ThreadPoolExecutor.CallerRunsPolicy}, the thread
 * that invokes {@code execute} itself runs the task. This provides a
 * simple feedback control mechanism that will slow d own the rate that
 * new tasks are submitted. </li>  用调用者所在的线程来执行任务
 *
 * <li> In {@link ThreadPoolExecutor.DiscardPolicy}, a task that
 * cannot be executed is simply dropped.  </li> 直接删除此任务
 *
 * <li>In {@link ThreadPoolExecutor.DiscardOldestPolicy}, if the
 * executor is not shut down, the task at the head of the work queue
 * is dropped, and then execution is retried (which can fail again,
 * causing this to be repeated.) </li>  丢弃最旧的任务
 *
 * </ol>
 * <p>
 * It is possible to define and use other kinds of {@link
 * RejectedExecutionHandler} classes. Doing so requires some care
 * especially when policies are designed to work only under particular
 * capacity or queuing policies. </dd>
 *
 * <dt>Hook methods</dt>
 *
 * <dd>This class provides {@code protected} overridable
 * {@link #beforeExecute(Thread, Runnable)} and
 * {@link #afterExecute(Runnable, Throwable)} methods that are called
 * before and after execution of each task.  These can be used to
 * manipulate the execution environment; for example, reinitializing
 * ThreadLocals, gathering statistics, or adding log entries.
 * Additionally, method {@link #terminated} can be overridden to perform
 * any special processing that needs to be done once the Executor has
 * fully terminated.
 * 钩子方法;给我们来对其进
 * 行扩展。可以用来添加日志
 * 、计时、监视等等
 * <p>If hook or callback methods throw exceptions, internal worker
 * threads may in turn fail and abruptly terminate.</dd>
 *
 * <dt>Queue maintenance</dt>
 *
 * <dd>Method {@link #getQueue()} allows access to the work queue
 * for purposes of monitoring and debugging.  Use of this method for
 * any other purpose is strongly discouraged.  Two supplied methods,
 * {@link #remove(Runnable)} and {@link #purge} are available to
 * assist in storage reclamation when large numbers of queued tasks
 * become cancelled.</dd>
 *
 * <dt>Finalization</dt>
 *
 * <dd>A pool that is no longer referenced in a program <em>AND</em>
 * has no remaining threads will be {@code shutdown} automatically. If
 * you would like to ensure that unreferenced pools are reclaimed even
 * if users forget to call {@link #shutdown}, then you must arrange
 * that unused threads eventually die, by setting appropriate
 * keep-alive times, using a lower bound of zero core threads and/or
 * setting {@link #allowCoreThreadTimeOut(boolean)}.  </dd>
 *
 * </dl>
 *
 * <p><b>Extension example</b>. Most extensions of this class
 * override one or more of the protected hook methods. For example,
 * here is a subclass that adds a simple pause/resume feature:
 *
 *  <pre> {@code
 * class PausableThreadPoolExecutor extends ThreadPoolExecutor {
 *   private boolean isPaused;
 *   private ReentrantLock pauseLock = new ReentrantLock();
 *   private Condition unpaused = pauseLock.newCondition();
 *
 *   public PausableThreadPoolExecutor(...) { super(...); }
 *
 *   protected void beforeExecute(Thread t, Runnable r) {
 *     super.beforeExecute(t, r);
 *     pauseLock.lock();
 *     try {
 *       while (isPaused) unpaused.await();
 *     } catch (InterruptedException ie) {
 *       t.interrupt();
 *     } finally {
 *       pauseLock.unlock();
 *     }
 *   }
 *
 *   public void pause() {
 *     pauseLock.lock();
 *     try {
 *       isPaused = true;
 *     } finally {
 *       pauseLock.unlock();
 *     }
 *   }
 *
 *   public void resume() {
 *     pauseLock.lock();
 *     try {
 *       isPaused = false;
 *       unpaused.signalAll();
 *     } finally {
 *       pauseLock.unlock();
 *     }
 *   }
 * }}</pre>
 * 线程池在内部实际上构建了一个生产者消费者模型，将线程和任务两者解耦，并不直接关联，从而良好的缓冲任务，复用线程
 *
 * @author Doug Lea
 * @since 1.5
 */
public class ThreadPoolExecutor extends AbstractExecutorService {
    /**
     * The main pool control state, ctl, is an atomic integer packing  two conceptual fields
     * workerCount, indicating the effective number of threads
     * runState,    indicating whether running, shutting down etc
     * <p>
     * In order to pack them into one int, we limit workerCount to
     * (2^29)-1 (about 500 million) threads rather than (2^31)-1 (2
     * billion) otherwise representable. If this is ever an issue in
     * the future, the variable can be changed to be an AtomicLong,
     * and the shift/mask constants below adjusted. But until the need
     * arises, this code is a bit faster and simpler using an int.
     * <p>
     * The workerCount is the number of workers that have been
     * permitted to start and not permitted to stop.  The value may be
     * transiently different from the actual number of live threads,
     * for example when a ThreadFactory fails to create a thread when
     * asked, and when exiting threads are still performing
     * bookkeeping before terminating. The user-visible pool size is
     * reported as the current size of the workers set.
     * <p>
     * The runState provides the main lifecycle control, taking on values:
     * <p>
     * RUNNING:  Accept new tasks and process queued tasks
     * SHUTDOWN: Don't accept new tasks, but process queued tasks
     * STOP:     Don't accept new tasks, don't process queued tasks,
     * and interrupt in-progress tasks
     * TIDYING:  All tasks have terminated, workerCount is zero,
     * the thread transitioning to state TIDYING
     * will run the terminated() hook method
     * TERMINATED: terminated() has completed
     * <p>
     * The numerical order among these values matters, to allow
     * ordered comparisons. The runState monotonically increases over
     * time, but need not hit each state. The transitions are:
     * <p>
     * RUNNING -> SHUTDOWN
     * On invocation of shutdown(), perhaps implicitly in finalize()
     * (RUNNING or SHUTDOWN) -> STOP
     * On invocation of shutdownNow()
     * SHUTDOWN -> TIDYING
     * When both queue and pool are empty
     * STOP -> TIDYING
     * When pool is empty
     * TIDYING -> TERMINATED
     * When the terminated() hook method has completed
     * <p>
     * Threads waiting in awaitTermination() will return when the
     * state reaches TERMINATED.
     * <p>
     * Detecting the transition from SHUTDOWN to TIDYING is less
     * straightforward than you'd like because the queue may become
     * empty after non-empty and vice versa during SHUTDOWN state, but
     * we can only terminate if, after seeing that it is empty, we see
     * that workerCount is 0 (which sometimes entails a recheck -- see
     * below).
     */
    //如果要我们去实现一个线程池，可能第一反应就是用一个单独的变量来表示线程池的状态，再用另一个变量来表示线程池中线程的数量.但是
    //使用一个变量  ctl 来 记录“线程池中的任务数量”和“线程池的状态”两个信息
    // 高3位 表示当前线程池运行时状态
    // 低29位 表示当前线程池中存活的线程数量
    //为什么这么设计？https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651751537&idx=1&sn=c50a434302cc06797828782970da190e&chksm=bd125d3c8a65d42aaf58999c89b6a4749f092441335f3c96067d2d361b9af69ad4ff1b73504c&scene=21#wechat_redirect
    //用一个变量去存储两个值，可避免在做相关决策时，出现不一致的情况，不必为了维护两者的一致，而占用锁资源
    //通过阅读线程池源代码也可以发现，经常出现要同时判断线程池运行状态和线程数量的情况。线程池也提供了若干方法去供用户获得线程池当前的运行状态、线程个数。
    // 这里都使用的是位运算的方式，相比于基本运算，速度也会快很多。
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    //表示 在ctl中 存放 当前线程数量的 位数 32-3=29位
    private static final int COUNT_BITS = Integer.SIZE - 3;
    //低COUNT_BITS位能表达的最大数值  000 111111111111111111111=5亿多
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    /**
     * 线程的状态： 5种 ; 线程有6种状态
     * RUNNING：线程池能够接受新任务，以及对新添加的任务进⾏处理。
     * SHUTDOWN：线程池不可以接受新任务，但是可以对已添加的任务进⾏处理。
     * STOP：线程池不接收新任务，不处理已添加的任务，并且会中断正在处理的任务。
     * TIDYING：当所有的任务已终⽌，ctl记录的"任务数量"为0，有效线程数也为0，线程池会变为TIDYING状态。
     * 当线程池变为TIDYING状态时，会执⾏钩⼦函数terminated()。terminated()在ThreadPoolExecutor类中
     * 是空的，若⽤户想在线程池变为TIDYING时，进⾏相应的处理；可以通过重载terminated()函数来实现。
     * TERMINATED：线程池彻底终⽌的状态。
     * 以下状态依次递增
     */
    //111  0000000000000000
    private static final int RUNNING = -1 << COUNT_BITS; //线程池创建之后的初始状态，这种状态下可以执行任务
    //000  0000000000000000
    private static final int SHUTDOWN = 0 << COUNT_BITS;//该状态下线程池不再接受新任务，但是会将工作队列中的任务执行完毕
    //001  0000000000000000
    private static final int STOP = 1 << COUNT_BITS;//该状态下线程池不再接受新任务，也不会处理工作队列中的剩余任务，并且将会中断所有工作线程
    //010  0000000000000000
    private static final int TIDYING = 2 << COUNT_BITS;//该状态下所有任务都已终止或者处理完成，将会执行 terminated( )钩子方法
    //011  0000000000000000
    private static final int TERMINATED = 3 << COUNT_BITS;//执行完terminated( )钩子方法之后的状态

    // Packing and unpacking ctl
    // 获取线程池的运行状态
    // 计算线程池的状态，计算结果的低29位全为0，因此最终结果就是线程池的状态
    private static int runStateOf(int c) {
        //~000 111111111111111111111 => 111 0000000000000000
        // c =ctl =111 00000000000000111
        //&          111 00000000000000000
        //             111 00000000000000000
        //             RUNNING
        return c & ~CAPACITY;
    }

    // 获取当前线程池数量
    // 工作线程的数量，计算结果的高3位全是0，因此最终结果就是工作线程的数量
    // c= ctl =111 00000000000000111
    // 111 00000000000000111
    //000 11111111111111111
    //000 00000000000000111
    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    // 用来重置当前线程池 ctl 值 时会用到
    // rs 表示线程池状态  wc 表示线程池数量
    // 根据线程池的状态和工作线程的数量，计算 ctl，实际上就是将两者合并成ctl 用来表示两个总变量的
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    /*
     * Bit field accessors that don't require unpacking ctl.
     * These depend on the bit layout and on workerCount being never negative.
     */

    // 比较当前 ctl 所表示的状态 是否小于某个状态 s
    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    // 比较当前 c  所表示的状态 是否大于 某个状态 s
    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    //是否是   RUNNING 状态
    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    /**
     * Attempts to CAS-increment the workerCount field of ctl.
     * 尝试CAS递增 ctl 的workerCount字段
     */
    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    /**
     * Attempts to CAS-decrement the workerCount field of ctl.
     * 尝试CAS递减ctl的workerCount字段
     */
    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }

    /**
     * Decrements the workerCount field of ctl. This is called only on
     * abrupt termination of a thread (see processWorkerExit). Other
     * decrements are performed within getTask.
     * 将 ctl 减一
     * 一定会成功 会一直在重试
     */
    private void decrementWorkerCount() {
        do {
        } while (!compareAndDecrementWorkerCount(ctl.get()));
    }

    /**
     * The queue used for holding tasks and handing off to worker
     * threads.  We do not require that workQueue.poll() returning
     * null necessarily means that workQueue.isEmpty(), so rely
     * solely on isEmpty to see if the queue is empty (which we must
     * do for example when deciding whether to transition from
     * SHUTDOWN to TIDYING).  This accommodates special-purpose
     * queues such as DelayQueues for which poll() is allowed to
     * return null even if it may later return non-null when delays
     * expire.
     * 任务队列
     * 达到核心线程数量时，会返给在队列中 ；会有多种实现方式；
     */
    private final BlockingQueue<Runnable> workQueue;

    /**
     * Lock held on access to workers set and related bookkeeping.
     * While we could use a concurrent set of some sort, it turns out
     * to be generally preferable to use a lock. Among the reasons is
     * that this serializes interruptIdleWorkers, which avoids
     * unnecessary interrupt storms, especially during shutdown.
     * Otherwise exiting threads would concurrently interrupt those
     * that have not yet interrupted. It also simplifies some of the
     * associated statistics bookkeeping of largestPoolSize etc. We
     * also hold mainLock on shutdown and shutdownNow, for the sake of
     * ensuring workers set is stable while separately checking
     * permission to interrupt and actually interrupting.
     * 线程池 全局锁，加线程、减线程、改变线程状态  需要加锁实现；
     */
    private final ReentrantLock mainLock = new ReentrantLock();

    /**
     * Set containing all worker threads in pool. Accessed only when
     * holding mainLock.
     * 存放线程的地方
     */
    private final HashSet<Worker> workers = new HashSet<Worker>();

    /**
     * Wait condition to support awaitTermination
     * 条件队列
     * 当外部线程调用 awaitTermination() 方法时 ，外部线程会等待当前线程池的状态为 termination 为止；
     * 等待是如何实现的 ？就是把外部线程封装成 waitnode 节点放入到 Condition 队列中；外部线程就会被 park 掉 处于 watiting 状态；
     * 当线程池状态变为 termination  时，就会唤醒 termination.signalAll() ， 会进入阻塞队列 ，头节点获取抢占 mainlock，抢占到的线程会执行
     * awaitTermination 后面的程序，最后这些线程都会正常执行；
     * termination.await() 会将线程阻塞在这里；
     * termination.signalAll() 会将阻塞新的线程依次唤醒；
     */
    private final Condition termination = mainLock.newCondition();

    /**
     * Tracks largest attained pool size. Accessed only under
     * mainLock.
     * 记录线程池 生命周期中 线程峰值
     */
    private int largestPoolSize;

    /**
     * Counter for completed tasks. Updated only on termination of
     * worker threads. Accessed only under mainLock.
     * 记录线程池  所完成任务总数
     */
    private long completedTaskCount;

    /*
     * All user control parameters are declared as volatiles so that
     * ongoing actions are based on freshest values, but without need
     * for locking, since no internal invariants depend on them
     * changing synchronously with respect to other actions.
     */

    /**
     * Factory for new threads. All threads are created using this
     * factory (via method addWorker).  All callers must be prepared
     * for addWorker to fail, which may reflect a system or user's
     * policy limiting the number of threads.  Even though it is not
     * treated as an error, failure to create threads may result in
     * new tasks being rejected or existing ones remaining stuck in
     * the queue.
     * <p>
     * We go further and preserve pool invariants even in the face of
     * errors such as OutOfMemoryError, that might be thrown while
     * trying to create threads.  Such errors are rather common due to
     * the need to allocate a native stack in Thread.start, and users
     * will want to perform clean pool shutdown to clean up.  There
     * will likely be enough memory available for the cleanup code to
     * complete without encountering yet another OutOfMemoryError.
     */
    private volatile ThreadFactory threadFactory;

    /**
     * Handler called when saturated or shutdown in execute.
     * 拒绝策略：默认四种：采用 Abort Handler 抛出异常
     */
    private volatile RejectedExecutionHandler handler;

    /**
     * Timeout in nanoseconds for idle threads waiting for work.
     * Threads use this timeout when there are more than corePoolSize
     * present or if allowCoreThreadTimeOut. Otherwise they wait
     * forever for new work.
     * 空闲线程存活时间
     */
    private volatile long keepAliveTime;

    /**
     * If false (default), core threads stay alive even when idle.  如果为false（默认），则即使处于空闲状态，核心线程也保持活动状态
     * If true, core threads use keepAliveTime to time out waiting for work. 如果为true，则核心线程使用keepAliveTime来超时等待工作
     * 控制核心线程 是否在空闲时间内被回收 ：true 可以
     */
    private volatile boolean allowCoreThreadTimeOut;

    /**
     * Core pool size is the minimum number of workers to keep alive
     * (and not allow to time out etc) unless allowCoreThreadTimeOut
     * is set, in which case the minimum is zero.
     * 核心数量
     */
    private volatile int corePoolSize;

    /**
     * Maximum pool size. Note that the actual maximum is internally
     * bounded by CAPACITY.
     * 最大线程限制
     */
    private volatile int maximumPoolSize;

    /**
     * The default rejected execution handler
     * 拒绝策略：抛出异常的方式
     */
    private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();

    /**
     * Permission required for callers of shutdown and shutdownNow.
     * We additionally require (see checkShutdownAccess) that callers
     * have permission to actually interrupt threads in the worker set
     * (as governed by Thread.interrupt, which relies on
     * ThreadGroup.checkAccess, which in turn relies on
     * SecurityManager.checkAccess). Shutdowns are attempted only if
     * these checks pass.
     * <p>
     * All actual invocations of Thread.interrupt (see
     * interruptIdleWorkers and interruptWorkers) ignore
     * SecurityExceptions, meaning that the attempted interrupts
     * silently fail. In the case of shutdown, they should not fail
     * unless the SecurityManager has inconsistent policies, sometimes
     * allowing access to a thread and sometimes not. In such cases,
     * failure to actually interrupt threads may disable or delay full
     * termination. Other uses of interruptIdleWorkers are advisory,
     * and failure to actually interrupt will merely delay response to
     * configuration changes so is not handled exceptionally.
     */
    //
    private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");

    /*
     *The context to be used when executing the finalizer, or null.
     *
     */
    private final AccessControlContext acc;

    /**
     * Class Worker mainly maintains interrupt control state for
     * threads running tasks, along with other minor bookkeeping.
     * This class opportunistically extends AbstractQueuedSynchronizer
     * to simplify acquiring and releasing a lock surrounding each
     * task execution.  This protects against interrupts that are
     * intended to wake up a worker thread waiting for a task from
     * instead interrupting a task being run.  We implement a simple
     * non-reentrant mutual exclusion lock rather than use
     * ReentrantLock because we do not want worker tasks to be able to
     * reacquire the lock when they invoke pool control methods like
     * setCorePoolSize.  Additionally, to suppress interrupts until
     * the thread actually starts running tasks, we initialize lock
     * state to a negative value, and clear it upon start (in
     * runWorker).
     * 采用了AQS 的独占模式：两个重要属性：state 状态属性、exclusiveOwnerThread 所拥有线程
     * state =0 时未被占用 ；>0 时被占用 ；<0 时 初始状态 不能被强锁；
     * exclusiveOwnerThread 表示独占锁的线程
     * 美团：Worker是通过继承AQS，使用AQS来实现独占锁这个功能。
     * 没有使用可重入锁 ReentrantLock，而是使用AQS，为的就是实现不可重入的特性去反应线程现在的执行状态
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
        /**
         * This class will never be serialized, but we provide a
         * serialVersionUID to suppress a javac warning.
         */
        private static final long serialVersionUID = 6138294804551838833L;

        /**
         * Thread this worker is running in.  Null if factory fails.
         * work 内部封装的内部线程
         */
        final Thread thread;
        /**
         * Initial task to run.  Possibly null.
         * 假设 firstTask 不为 null ，当 work 内部的线程启动后 优先执行 firstTask ，执行完毕后 会从队列中获取；
         */
        Runnable firstTask;
        /**
         * Per-thread task counter
         * 完成任务数量
         */
        volatile long completedTasks;

        /**
         * Creates with given first task and thread from ThreadFactory.
         *
         * @param firstTask the first task (null if none)
         *                  firstTask 可以为null，
         */
        Worker(Runnable firstTask) {
            //设置 AQS 的 state 状态为初始化状态 不能来抢锁；
            setState(-1); // inhibit interrupts until runWorker 禁止中断，直到 runWorker
            this.firstTask = firstTask;
            //使用线程工厂 创建一个线程 并且将当前的 worker 指定为 runnable ,当线程启动时，会以 worker.run()为入口， 就是下一个方法；
            // 注意在通过ThreadFactory创建线程时，将Worker自身也就是this，传入了进去，也就是说最后创建出来的线程对象，它里面的target属性就是指向这个Worker对象
            this.thread = getThreadFactory().newThread(this);
        }

        /**
         * Delegates main run loop to outer runWorker
         * 当 worker 启动时 会调用 run 方法
         */
        public void run() {
            runWorker(this);
        }

        // Lock methods
        //
        // The value 0 represents the unlocked state.
        // The value 1 represents the locked state.
        //当前的 worker 独占锁 是否被占用
        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        //尝试去占用 worker 的独占锁
        protected boolean tryAcquire(int unused) {
            //使用 aqs 来设置状态  -> 不可重入 -> 不可在 interruptIdleWorkers() 处被中断 -> 可以继续执行已经拿到的任务
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 尝试释放锁  外部不会直接调用此方法  是 aqs 内部调用的
        protected boolean tryRelease(int unused) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        //加锁，加锁失败时，会一直重试，直到成功为止
        public void lock() {
            acquire(1);
        }

        //尝试加锁，成功的话就返回true ,失败的话就直接返回false 不会阻塞；
        public boolean tryLock() {
            return tryAcquire(1);
        }

        //一般情况下  调用此方法  要保证当前线程是持有锁的
        //特殊情况：刚初始化时 state ==-1 ，调用 unlock  表示设置 state ==0 ；
        //启动 worker 之前会调用 unlock 方法，会强制刷新当前 独占线程 为null ；
        public void unlock() {
            release(1);
        }

        //
        public boolean isLocked() {
            return isHeldExclusively();
        }

        //
        void interruptIfStarted() {
            Thread t;
            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                }
            }
        }
    }

    /*
     * Methods for setting control state
     */

    /**
     * Transitions runState to given target, or leaves it alone if already at least the given target.
     *
     * @param targetState the desired state, either SHUTDOWN or STOP
     *                    (but not TIDYING or TERMINATED -- use tryTerminate for that)
     */
    private void advanceRunState(int targetState) {
        for (; ; ) {
            int c = ctl.get();
            //条件成立：假设 targetState==SHUTDOWN ，说明 当前线程池状态是 >= SHUTDOWN
            if (runStateAtLeast(c, targetState) || ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c)))) {
                break;
            }
        }
    }

    /**
     * Transitions to TERMINATED state if either (SHUTDOWN and pool
     * and queue empty) or (STOP and pool empty).  If otherwise
     * eligible to terminate but workerCount is nonzero, interrupts an
     * idle worker to ensure that shutdown signals propagate. This
     * method must be called following any action that might make
     * termination possible -- reducing worker count or removing tasks
     * from the queue during shutdown. The method is non-private to
     * allow access from ScheduledThreadPoolExecutor.
     * 尝试设为 TERMINATED 状态
     */
    final void tryTerminate() {
        //自旋设置
        for (; ; ) {
            int c = ctl.get();
            // 1. isRunning(c)  成立 直接返回就行，线程池很正常 ！
            if (isRunning(c) ||
                    //2.   runStateAtLeast(c, TIDYING)   成立：说明，已经有其他线程在 在执行 TIDYING-》 TERMINATED，当前线程直接回去
                    runStateAtLeast(c, TIDYING) ||
                    //3. (runStateOf(c) == SHUTDOWN && !workQueue.isEmpty()
                    //SHUTDOWN 特殊情况  直接返回 ，因为得等待队列中的任务处理完毕，再转化状态。
                    (runStateOf(c) == SHUTDOWN && !workQueue.isEmpty())) {
                return;
            }

            //什么情况会来到这里？
            //1. 线程池状态 >= STOP
            //2. 线程池状态为 SHUTDOWN 且  队列为空

            //说明：当前线程数量不为0
            if (workerCountOf(c) != 0) {
                //中断一个空闲线程
                //空闲线程  在哪里空闲的？ queue.take | queue.poll
                //1. 唤醒后的线程 会在getTask()方法返回null
                //2. 执行退出的逻辑的 时候会再次调用 tryTerminate 唤醒下一个空闲线程
                //3.
                //最终空闲线程都会在这里退出 非空闲线程 当执行完 任务 ，也会调用 tryTerminate 方法 有可能会走到这里。
                interruptIdleWorkers(ONLY_ONE);
                return;
            }

            //执行到这里的线程
            //最后一个线程

            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                //强制设置状态 为 TIDYING
                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                    try {
                        //调用线程终止钩子函数  一般留给子类实现
                        terminated();
                    } finally {
                        //设置线程池状态 为 TERMINATED
                        ctl.set(ctlOf(TERMINATED, 0));
                        //唤醒调用  awaitTermination 方法的线程
                        termination.signalAll();
                    }
                    return;
                }
            } finally {
                //解锁
                mainLock.unlock();
            }
            // else retry on failed CAS  否则重试失败的CAS
        }
    }

    /*
     * Methods for controlling interrupts to worker threads.
     * 控制工作线程中断的方法
     */

    /**
     * If there is a security manager, makes sure caller has
     * permission to shut down threads in general (see shutdownPerm).
     * If this passes, additionally makes sure the caller is allowed
     * to interrupt each worker thread. This might not be true even if
     * first check passed, if the SecurityManager treats some threads
     * specially.
     */
    private void checkShutdownAccess() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(shutdownPerm);
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                for (Worker w : workers) {
                    security.checkAccess(w.thread);
                }
            } finally {
                mainLock.unlock();
            }
        }
    }

    /**
     * Interrupts all threads, even if active. Ignores SecurityExceptions
     * (in which case some threads may remain uninterrupted).
     */
    private void interruptWorkers() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers) {
                //interruptIfStarted 如果 worker 中的 thread 是启动状态 给他一个中断信号
                w.interruptIfStarted();
            }
        } finally {
            //释放全局锁
            mainLock.unlock();
        }
    }

    /**
     * Interrupts threads that might be waiting for tasks (as
     * indicated by not being locked) so they can check for
     * termination or configuration changes. Ignores
     * SecurityExceptions (in which case some threads may remain
     * uninterrupted).
     *
     * @param onlyOne If true, interrupt at most one worker. This is
     *                called only from tryTerminate when termination is otherwise
     *                enabled but there are still other workers.  In this case, at
     *                most one waiting worker is interrupted to propagate shutdown
     *                signals in case all threads are currently waiting.
     *                Interrupting any arbitrary thread ensures that newly arriving
     *                workers since shutdown began will also eventually exit.
     *                To guarantee eventual termination, it suffices to always
     *                interrupt only one idle worker, but shutdown() interrupts all
     *                idle workers so that redundant workers exit promptly, not
     *                waiting for a straggler task to finish.
     *                onlyOne ：true 只中断一个线程  ；false 则中断所有线程
     */
    private void interruptIdleWorkers(boolean onlyOne) {
        //可重入锁
        final ReentrantLock mainLock = this.mainLock;
        //获得锁
        mainLock.lock();
        try {
            //迭代所有worker
            for (Worker w : workers) {
                //获取 worker 中的线程
                Thread t = w.thread;
                //条件一：!t.isInterrupted()==true  说明当前迭代的 这个线程尚未中断
                //条件二： w.tryLock()  成立 ：说明当前 worker 处于空闲状态 可以去给它一个中断信号， 目前worker 中的线程在 queue.take | queue.poll
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        //给当前一个线程一个中断信号
                        // 处于queue阻塞的线程 会被唤醒 唤醒后 进入下一个自旋时 可能是返回 null 执行相关退出的逻辑
                        t.interrupt();
                    } catch (SecurityException ignore) {
                    } finally {
                        //释放锁
                        w.unlock();
                    }
                }
                //跳出循环
                if (onlyOne) {
                    break;
                }
            }
        } finally {
            //释放锁
            mainLock.unlock();
        }
    }

    /**
     * Common form of interruptIdleWorkers, to avoid having to
     * remember what the boolean argument means.
     */
    private void interruptIdleWorkers() {
        interruptIdleWorkers(false);
    }

    private static final boolean ONLY_ONE = true;

    /*
     * Misc utilities, most of which are also exported to
     * ScheduledThreadPoolExecutor
     */

    /**
     * Invokes the rejected execution handler for the given command.
     * Package-protected for use by ScheduledThreadPoolExecutor.
     */
    final void reject(Runnable command) {
        handler.rejectedExecution(command, this);
    }

    /**
     * Performs any further cleanup following run state transition on
     * invocation of shutdown.  A no-op here, but used by
     * ScheduledThreadPoolExecutor to cancel delayed tasks.
     */
    void onShutdown() {
    }

    /**
     * State check needed by ScheduledThreadPoolExecutor to
     * enable running tasks during shutdown.
     *
     * @param shutdownOK true if should return true if SHUTDOWN
     */
    final boolean isRunningOrShutdown(boolean shutdownOK) {
        int rs = runStateOf(ctl.get());
        return rs == RUNNING || (rs == SHUTDOWN && shutdownOK);
    }

    /**
     * Drains the task queue into a new list, normally using
     * drainTo. But if the queue is a DelayQueue or any other kind of
     * queue for which poll or drainTo may fail to remove some
     * elements, it deletes them one by one.
     */
    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> q = workQueue;
        ArrayList<Runnable> taskList = new ArrayList<Runnable>();
        q.drainTo(taskList);
        if (!q.isEmpty()) {
            for (Runnable r : q.toArray(new Runnable[0])) {
                if (q.remove(r))
                    taskList.add(r);
            }
        }
        return taskList;
    }

    /*
     * Methods for creating, running and cleaning up after workers
     */

    /**
     * Checks if a new worker can be added with respect to current
     * pool state and the given bound (either core or maximum). If so,
     * the worker count is adjusted accordingly, and, if possible, a
     * new worker is created and started, running firstTask as its
     * first task. This method returns false if the pool is stopped or
     * eligible to shut down. It also returns false if the thread
     * factory fails to create a thread when asked.  If the thread
     * creation fails, either due to the thread factory returning
     * null, or due to an exception (typically OutOfMemoryError in
     * Thread.start()), we roll back cleanly.
     *
     * @param firstTask the task the new thread should run first (or
     *                  null if none). Workers are created with an initial first task
     *                  (in method execute()) to bypass queuing when there are fewer
     *                  than corePoolSize threads (in which case we always start one),
     *                  or when the queue is full (in which case we must bypass queue).
     *                  Initially idle threads are usually created via
     *                  prestartCoreThread or to replace other dying workers.
     * @param core      if true use corePoolSize as bound, else
     *                  maximumPoolSize. (A boolean indicator is used here rather than a
     *                  value to ensure reads of fresh values after checking other pool
     *                  state).
     * @return true if successful
     * firstTask：表示 启动 worker 之后，worker 自动到 queue 中获取任务如果不是空，则就执行；
     * core：传入true表示的是当前线程池中的线程数还没有达到核心线程数，传false表示当前线程数已经大于等于核心线程数了
     * 返回值总结：
     * true：表示创建成功 启动成功
     * false：表示失败
     * 为何失败：
     * 1. 线程池状态为 rs > shutdown （STOP TIDYING ）
     * 2. rs == shutdown 但是队列中已经没有任务了 或者 当前状态是 shutdown 且 队列不为空  但是  firstTask 不为 null
     * 3. 当前线程池 已经达到指定指标（coresize/或者达到最大线程池）
     * 4. 创建的线程是 null
     */
    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        //外部自旋  获取线程池状态
        for (; ; ) {
            //获取当前 ctl 包括线程池状态以及线程数量
            int c = ctl.get();
            //获取当前线程池运行状态
            int rs = runStateOf(c);
            //条件一：表示不是 running 状态  ，当线程池的状态大于SHUTDOWN时，返回false。因为线程池处于关闭状态了，就不能再接受任务了
            if (rs >= SHUTDOWN &&
                    //条件二：！（当前状态是  SHUTDOWN 并且 提交的任务为空   并且  当前队列不是空）
                    //排除当前这种情况，当前线程池 SHUTDOWN 状态 但是队列中还有任务没有执行完毕，这个时候允许添加 worker 不允许添加 task
                    !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty())) {
                //当线程池状态 大于 SHUTDOWN
                // rs == SHUTDOWN  但是队列中已经没有任务了 或者  rs == SHUTDOWN  且  firstTask！=null
                return false;
            }//以上代码就是判断 当前线程池状态 是否允许添加线程

            //内部自旋  获取创建线程令牌的过程
            for (; ; ) {
                //获取当前线程池数量
                int wc = workerCountOf(c);
                // 1. 线程数大于等于理论上的最大数(2^29-1)，则直接返回false。（因为线程数不能再增加了）
                // 2. 根据core来决定线程数应该和谁比较。当线程数大于核心线程数或者最大线程数时，直接返回false。
                // （因为当大于核心线程数时，表示此时任务应该直接添加到队列中(如果队列满了，可能入队失败)；
                // 当大于最大线程数时，肯定不能再新创建线程了，不然设置最大线程数有毛用）
                if (wc >= CAPACITY || wc >= (core ? corePoolSize : maximumPoolSize)) {
                    //无法添加线程了
                    return false;
                }

                // 线程数+1，如果设置成功，就跳出外层循环
                //成功：尝试添加线程池数量 申请了一个令牌
                // 失败：其他线程修改过 ctl 这个值了
                if (compareAndIncrementWorkerCount(c)) {
                    //一定是 cas 成功了 申请到令牌了
                    //直接跳出了外部自旋
                    break retry;
                }

                //cas 失败 没有申请到令牌
                //获取最新的 ctl 得值
                c = ctl.get();

                //判断当前的线程池状态是否发生过变化 ，如果在外部调用过 shutdown
                // 再次获取线程池的状态，并将其与前面获取到的值比较，
                // 如果相同，说明线程池的状态没有发生变化，继续在内循环中进行循环
                // 如果不相同，说明在这期间，线程池的状态发生了变化，需要跳到外层循环，然后再重新进行循环
                if (runStateOf(c) != rs) {
                    // 否则CAS由于 workerCount 更改而失败；重试外部循环 判断当前线程池状态 是否 允许创建线程
                    continue retry;
                }
            }   //内部自旋  获取创建线程令牌的过程 over
        } //外部自旋  获取线程池状态 over

        //可以创建了

        //创建的 worker 是否已经启动了
        boolean workerStarted = false;
        //创建的 worker 是否放入在池子中了
        boolean workerAdded = false;
        //后面创建 worker 的引用
        Worker w = null;

        //开始尝试创建
        try {
            //开始创建 worker  线程应该是创建好了
            w = new Worker(firstTask);
            //对应的线程 赋值给 t 变量
            final Thread t = w.thread;
            //为了防止 ThreadFactory 有 bug ，因为 ThreadFactory 是一个接口 谁都可以创建
            if (t != null) {
                //保存全局锁
                final ReentrantLock mainLock = this.mainLock;
                //持有全局锁  可能会阻塞 直到获取即可 ；同一时刻 操纵 线程池内部状态 的相关操作 都必须持有锁
                //来保证线程安全，因为这一步workers.add(w)存在并发的可能，所以需要通过获取锁来保证线程安全
                mainLock.lock();

                //从这里加锁之后 其他线程无法修改此处的线程池状态
                try {
                    //线程池 运行状态
                    int rs = runStateOf(ctl.get());

                    //条件一：成立：就是 最正常状态 running
                    if (rs < SHUTDOWN ||
                            //条件二：前置条件 不是 running 状态 -> 成立：rs == SHUTDOWN && firstTask == null  不再判断队列是否为空了
                            (rs == SHUTDOWN && firstTask == null)) {

                        //当线程 start 后 线程 isAlive 会返回 true
                        //防止 刚创建 就被 start 了
                        // 判断worker线程是否已经启动了，如果已经启动，就抛出异常
                        // 个人觉得这一步没有任何意义，因为worker线程是刚new出来的，没有在任何地方启动
                        if (t.isAlive()) {
                            throw new IllegalThreadStateException();
                        }

                        //添加线程到set中
                        workers.add(w);
                        //获取线程池数
                        int s = workers.size();
                        if (s > largestPoolSize) {
                            largestPoolSize = s;
                        }
                        //添加成功
                        workerAdded = true;
                    }
                } finally {
                    //记得解锁
                    mainLock.unlock();
                }
                // 启动线程
                if (workerAdded) {
                    //当调用线程的start()方法之后，如果线程获取到CPU的执行权，那么就会执行线程的run()方法，在线程的run()方法中，
                    // 会执行线程中target属性的run()方法。这里线程的target属性就是我们创建的worker对象，因此最终会执行到Worker的run()方法
                    //那么线程是怎么消亡的呢???
                    t.start();
                    workerStarted = true;
                }
            }//添加完毕
        } finally {
            //如果执行失败 需要进行清理工作 清理令牌
            if (!workerStarted) {
                //清理工作
                //1. 释放令牌
                //2. 将当前的 worker 清除出去
                addWorkerFailed(w);
            }
        }
        //返回新创建的线程是否启动的状态
        return workerStarted;
    }

    /**
     * Rolls back the worker thread creation.
     * - removes worker from workers, if present
     * - decrements worker count
     * - rechecks for termination, in case the existence of this
     * worker was holding up termination
     */
    private void addWorkerFailed(Worker w) {
        //持有全局锁
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (w != null) {
                //清理
                workers.remove(w);
            }
            //线程池计数器 恢复减 1 前面+1 过 这里需要减一 相当于归还令牌
            decrementWorkerCount();
            //尝试清除
            tryTerminate();
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Performs cleanup and bookkeeping for a dying worker. Called
     * only from worker threads. Unless completedAbruptly is set,
     * assumes that workerCount has already been adjusted to account
     * for exit.  This method removes thread from worker set, and
     * possibly terminates the pool or replaces the worker if either
     * it exited due to user task exception or if fewer than
     * corePoolSize workers are running or queue is non-empty but
     * there are no workers.
     *
     * @param w                 the worker
     * @param completedAbruptly if the worker died due to user exception
     */
    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        //条件成立：代表当前这个 w 是发生异常退出的，执行 task 任务过程中向上抛出异常了
        //异常退出 ctl 数量减一
        if (completedAbruptly) {
            decrementWorkerCount();
        }

        //获取全局锁
        final ReentrantLock mainLock = this.mainLock;
        //加锁 执行退出线程池操作
        mainLock.lock();
        try {
            //将当前的 w 完成的 task 汇总到 线程池的 completedTaskCount
            completedTaskCount += w.completedTasks;
            //移除这个 w 从池子中 移除
            workers.remove(w);
        } finally {
            //释放全局锁
            mainLock.unlock();
        }

        //stop the world
        tryTerminate();

        //获取最新 ctl 值
        int c = ctl.get();
        //条件成立：当前状态为 running 或者 shutdown 状态 进入 if 条件判断
        //其他就直接返回 null
        if (runStateLessThan(c, STOP)) {

            //条件成立：当前线程是正常正常退出
            if (!completedAbruptly) {
                //min 表示线程池 最低持有线程池数量
                //allowCoreThreadTimeOut==true  表示核心线程数 也会被收回收
                //allowCoreThreadTimeOut==false  表示 min==corePoolSize (corePoolSize会不会等于 0 呢?)
                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
                //假设min ==0 成立 需要判断条件二：
                //如果队列不为空，表示需要留一个线程
                if (min == 0 && !workQueue.isEmpty()) {
                    min = 1;
                }

                //条件成立：线程池中还拥有足够多的线程
                //考虑一个问题：workerCountOf(c) >= min => (0>=0)
                //什么情况下：当线程池中的核心线程数是可以被回收的情况下 , 会出现这种情况，当前的线程池的数量 会变成 0
                //再次提交任务时 会再创建线程
                if (workerCountOf(c) >= min) {
                    return;
                }
            }

            //1. 当前线程是异常退出 得重新创建一个 w 顶上去
            //2.当前任务队列 不为空  表示需要创建 一个线程  当前状态为 running | shutdown
            //3. 当前线程数量 < 核心线程数量   此时还会创建线程 还会维护 线程数量在 核心数量中
            addWorker(null, false);
        }
    }

    /**
     * Performs blocking or timed wait for a task, depending on
     * current configuration settings, or returns null if this worker
     * must exit because of any of:
     * 1. There are more than maximumPoolSize workers (due to
     * a call to setMaximumPoolSize).
     * 2. The pool is stopped.
     * 3. The pool is shutdown and the queue is empty.
     * 4. This worker timed out waiting for a task, and timed-out
     * workers are subject to termination (that is,
     * {@code allowCoreThreadTimeOut || workerCount > corePoolSize})
     * both before and after the timed wait, and if the queue is
     * non-empty, this worker is not the last thread in the pool.
     *
     * @return task, or null if the worker must exit, in which case
     * workerCount is decremented
     * 线程池 获取任务
     * 什么情况下会返回 null ？
     * 1. rs >= STOP 成立说明 ：当前线程池最低状态也是STOP了，一定得返回null
     * 2.前置条件：rs >= SHUTDOWN 且 rs < STOP  = workQueue.isEmpty()  ，一定得返回 null
     * 3. 线程池中的线程数量 超过最大限制时，会有一部分返回 null
     * 4. 当线程池中的线程数超过 核心线程数时，会有一部分线程 超时获取 后返回 null
     */
    private Runnable getTask() {
        //表示是否超时获取任务
        boolean timedOut = false; // Did the last poll() time out?
        //开始自旋
        for (; ; ) {
            //获取最新的 线程池 ctl  状态
            int c = ctl.get();
            //获取线程池的运行状态
            int rs = runStateOf(c);

            //条件一： rs >= SHUTDOWN  那几个状态
            //条件二： (rs >= STOP || workQueue.isEmpty())
            //2.1 ：rs >= STOP  一定要返回 null
            //2.2 ：前提条件：rs >= SHUTDOWN 且 rs < STOP  和  workQueue.isEmpty() ：说明当前线程池状态为 shutdown 状态 且 任务队列为空 一并返回 null
            //返回 null，运行 runWorker 方法就会将返回 null 的线程 执行线程推出线程池的逻辑
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                //线程池线程数量减一
                //线程销毁
                decrementWorkerCount();
                return null;
            }

            //执行到这里有几种情况：
            //1. 线程池状态是 running 状态
            //2. 线程池是 shutdown 状态 但是队列还未空  可以创建线程

            //得到线程池的工作数量
            int wc = workerCountOf(c);

            //timed=true  表示当前这个线程 获取 task 是支持超时机制的  使用的是 queue.poll(***,***); 当获取 task()超时 的情况下 , 下一次自旋就可能返回 null 了
            //timed=false 表示当前这个线程 获取 task 是不支持超时机制的  当前线程 会使用 queue.take()

            //情况一：allowCoreThreadTimeOut ==true  表示核心的线程也可被回收 所有线程 -> 核心线程不需要保留
            //情况二：allowCoreThreadTimeOut ==false  表示当前线程会维护 核心的线程 -> 核心线程需要保留
            //条件二: (wc > corePoolSize) 成立 -> 说明当前已经超过了核心线程数量 -> 已经超了的话 那就直接 timed=true -> 走 queue.poll(***,***) 逻辑
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

            //条件一：(wc > maximumPoolSize || (timed && timedOut))
            //1.1 ：wc > maximumPoolSize 为什么会成立？ 可能外部线程 将线程池 最大线程数的值 设置的比初始化时小
            //1.2：(timed && timedOut)  条件成立：当前线程使用 poll() 获取方式获取 task ; 在上一轮循环时 使用 poll() 方式获取 task 时，获取到的任务为 null , 从而设置  timedOut = true
            //条件一: 为 true  不代表线程一定返回 null；并且 线程可以被回收
            //条件二： (wc > 1 || workQueue.isEmpty())
            //2.1：wc > 1：说明当前还有其他线程 可以被回收   返回null
            //2.2：wc == 1 前置条件 ：workQueue.isEmpty()： 说明队列已经空了 线程可以被退出了
            if ((wc > maximumPoolSize || (timed && timedOut)) && (wc > 1 || workQueue.isEmpty())) {
                //使用 cas 减一  , 减一成功的 线程会返回 null
                //cas为什么会失败？
                //1. 其他线程先你退出了
                //2. 线程池状态发生变换了
                if (compareAndDecrementWorkerCount(c)) {
                    return null;
                }
                //再次自旋时：timed 可能是false ，因为当前线程cas 失败，很有可能是因为其他线程成功退出导致的，再次自旋时
                continue;
            }

            //获取任务的逻辑
            try {
                //timed==true  走超时逻辑 -> 就是 继续再等待线程的存活时间段 , 倘若在线程的空闲存活时间内都没获取到任务,那就执行线程销毁逻辑
                //否则的话就  take() 阻塞的等待 任务
                Runnable r = timed ?
                        //双双 底层还是加锁了
                        workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                        workQueue.take();
                //直接返回任务
                if (r != null) {
                    return r;
                }
                //走到这里说明 获取到了 空任务 null (可能是在超时机制下  or  阻塞机制下 )
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
        }//for over
    }

    /**
     * Main worker run loop.  Repeatedly gets tasks from queue and
     * executes them, while coping with a number of issues:
     * <p>
     * 1. We may start out with an initial task, in which case we
     * don't need to get the first one. Otherwise, as long as pool is
     * running, we get tasks from getTask. If it returns null then the
     * worker exits due to changed pool state or configuration
     * parameters.  Other exits result from exception throws in
     * external code, in which case completedAbruptly holds, which
     * usually leads processWorkerExit to replace this thread.
     * <p>
     * 2. Before running any task, the lock is acquired to prevent
     * other pool interrupts while the task is executing, and then we
     * ensure that unless pool is stopping, this thread does not have
     * its interrupt set.
     * <p>
     * 3. Each task run is preceded by a call to beforeExecute, which
     * might throw an exception, in which case we cause thread to die
     * (breaking loop with completedAbruptly true) without processing
     * the task.
     * <p>
     * 4. Assuming beforeExecute completes normally, we run the task,
     * gathering any of its thrown exceptions to send to afterExecute.
     * We separately handle RuntimeException, Error (both of which the
     * specs guarantee that we trap) and arbitrary Throwables.
     * Because we cannot rethrow Throwables within Runnable.run, we
     * wrap them within Errors on the way out (to the thread's
     * UncaughtExceptionHandler).  Any thrown exception also
     * conservatively causes thread to die.
     * <p>
     * 5. After task.run completes, we call afterExecute, which may
     * also throw an exception, which will also cause thread to
     * die. According to JLS Sec 14.20, this exception is the one that
     * will be in effect even if task.run throws.
     * <p>
     * The net effect of the exception mechanics is that afterExecute
     * and the thread's UncaughtExceptionHandler have as accurate
     * information as we can provide about any problems encountered by
     * user code.
     * 1.while循环不断地通过getTask()方法获取任务
     * 2.getTask()方法从阻塞队列中取任务
     * 3.如果线程池正在停止，那么要保证当前线程是中断状态，否则要保证当前线程不是中断状态
     * 4.执行任务
     * 5.如果getTask结果为null则跳出循环，执行processWorkerExit()方法，销毁线程
     * 拓展 为什么这里会加锁 ->   w.lock(); ?
     * AQS is used only for this lock and unlock of runWorker() method. Can't we take a ReentrantLock here and keep Worker class simple?
     * https://www.jianshu.com/p/bc2c5224cacf
     */
    final void runWorker(Worker w) {
        //获取当前线程
        Thread wt = Thread.currentThread();
        //赋值 firstTask
        Runnable task = w.firstTask;
        //清空引用
        w.firstTask = null;
        //为什么会先释放锁？ 相当于 初始化线程池状态的操作
        w.unlock(); // allow interrupts
        //突然退出   发生异常 当前线程 需要做一些处理
        boolean completedAbruptly = true;

        try {
            //条件一：task != null  firstTask 是不是空   不是null的话 直接执行循环体
            //条件二： (task = getTask()) != null  说明在当前队列中获取任务成功， getTask() 是阻塞方法: 加锁了
            //getTask() 返回 null ：说明 当前线程需要执行退出逻辑
            //getTask() 支持超时，当超过线程池初始化时指定的线程最大存活时间后，就会返回null，从而导致worker线程退出while循环，最终线程销毁
            while (task != null || (task = getTask()) != null) {
                //worker 加锁 独占锁 为当前线程使用
                //为什么需要加锁？ 就是怕 shutdown() 时会判断 当前 worker 的状态  , 根据独占锁 是否空闲来判断是否在工作
                //保证worker串行的执行线程
                w.lock();
                //条件一：(runStateAtLeast(ctl.get(), STOP)  如果大于 stop 说明线程池状态 处于 stop 以上状态 ，此时一定要给他一个中断信号
                //条件二：如果条件(runStateAtLeast(ctl.get(), STOP)成立 那就直接看   !wt.isInterrupted() 且 当前线程未设置中断状态的； 都成立的话 此时需要进入if 条件中 强制进行中断操作
                //假设(runStateAtLeast(ctl.get(), STOP)不成立： 就会走(Thread.interrupted() && runStateAtLeast(ctl.get(), STOP))这个判断：
                //Thread.interrupted() 获取当前中断状态且设置为false ，runStateAtLeast(ctl.get(), STOP) 大概率 还是 false
                if ((runStateAtLeast(ctl.get(), STOP) || (Thread.interrupted() && runStateAtLeast(ctl.get(), STOP))) && !wt.isInterrupted()) {
                    wt.interrupt();
                }

                try {
                    // beforeExecute()是空方法，由子类具体实现
                    // 该方法的目的是为了让任务执行前做一些其他操作
                    beforeExecute(wt, task);
                    //表示异常状态
                    Throwable thrown = null;
                    try {
                        //如果线程池中的线程在执行任务的时候，抛异常了，会怎么样？
                        //task 可能是  FutureTask 或者 普通的 runnable 接口
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x;
                        throw x;
                    } catch (Error x) {
                        thrown = x;
                        throw x;
                    } catch (Throwable x) {
                        thrown = x;
                        throw new Error(x);
                    } finally {
                        // afterExecute()空方法，由子类具体实现
                        // 该方法的目的是为了让任务执行后做一些其他操作
                        afterExecute(task, thrown);
                    }
                } finally {
                    //将局部变量 值为null
                    task = null;
                    //当前线程完成数量 ++
                    w.completedTasks++;
                    //释放独占锁
                    //1. 正常状态下：会再次回到 getTask() 方法那里获得任务 while()
                    //2. task.run() 内部出现异常了
                    w.unlock();
                }
            }//while over

            //什么情况下 会来到这个???
            //1. getTask() 返回 null 时 说明线程 应该执行退出逻辑了
            //2. task 为 null
            completedAbruptly = false;
        } finally {
            //当Worker无法获取到任务，也就是获取的任务为空时，循环会结束，Worker会主动消除自身在线程池内的引用
            // task.run() 内部出现异常 直接从 w.lock() 那里跳到这一行
            //正常退出   completedAbruptly = false
            //非正常退出   completedAbruptly = true
            processWorkerExit(w, completedAbruptly);//获取不到任务时，主动回收自己
        }
    }

    // Public constructors and methods

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default thread factory and rejected execution handler.
     * It may be more convenient to use one of the {@link Executors} factory
     * methods instead of this general purpose constructor.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                Executors.defaultThreadFactory(), defaultHandler);
    }

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default rejected execution handler.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @param threadFactory   the factory to use when the executor
     *                        creates a new thread
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue}
     *                                  or {@code threadFactory} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, defaultHandler);
    }

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default thread factory.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @param handler         the handler to use when execution is blocked
     *                        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue}
     *                                  or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                Executors.defaultThreadFactory(), handler);
    }

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @param threadFactory   the factory to use when the executor
     *                        creates a new thread
     * @param handler         the handler to use when execution is blocked
     *                        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue}
     *                                  or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        //
        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize || keepAliveTime < 0) {
            throw new IllegalArgumentException();
        }
        //都不可为空
        if (workQueue == null || threadFactory == null || handler == null) {
            throw new NullPointerException();
        }
        //
        this.acc = System.getSecurityManager() == null ? null : AccessController.getContext();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    /**
     * Executes the given task sometime in the future.  The task
     * may execute in a new thread or in an existing pooled thread.
     * <p>
     * If the task cannot be submitted for execution, either because this
     * executor has been shutdown or because its capacity has been reached,
     * the task is handled by the current {@code RejectedExecutionHandler}.
     *
     * @param command the task to execute
     * @throws RejectedExecutionException at discretion of
     *                                    {@code RejectedExecutionHandler}, if the task
     *                                    cannot be accepted for execution
     * @throws NullPointerException       if {@code command} is null
     *                                    入参  可以是 FutureTask
     */
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }
        //获取 高3位是线程池状态 低29位是线程池数量
        int c = ctl.get();
        //如果线程池中运⾏的 线程数量<corePoolSize，则创建新线程来处理请求，即使其他辅助线程是空闲的
        //1.首先判断当前线程池中之行的 任务数量 是否小于 corePoolSize
        // 如果小于的话，通过 addWorker(command, true) 新建一个线程，并将任务(command)添加到该线程中；
        // 然后，启动该线程从而执行任务
        if (workerCountOf(c) < corePoolSize) {
            //core  true 表示采用核心线程限制  否则 表示用最大线程数量
            if (addWorker(command, true)) {
                //add 成功的话 就直接返回了
                return;
            }

            //执行到这时说明 add worker 线程 失败了
            //1. 存在并发现象：当多个线程调用时 execute 方法时 ，当 workerCountOf(c) < corePoolSize 成立后，其他线程也成立了 并且向线程池中添加任务
            //这时 addWorker 时 可能线程池核心线程数量已经达到，因此就失败了
            //2. 当前线程池状态发生变化了，变为非 RUNNING 的话也会失败
            // 任务执行失败后，重新获取ctl的值，供下面的逻辑计算
            c = ctl.get();
        }

        // 1.如果当前之行的任务数量大于等于 corePoolSize 的时候就会走到这里
        //2. add 失败

        // 当工作线程数大于等于核心线程数时，线程池是运行状态，且任务添加到队列成功
        // 通过 isRunning 方法判断线程池状态，线程池处于 RUNNING 状态才会被并且队列可以加入任务
        //拓展: 如何修改原生线程池，使得可以先拉满线程数再入任务队列排队？
        //增加: && workerCountOf(c)=maximumPoolSize
        if (isRunning(c) && workQueue.offer(command)) {
            //执行到这里 说明 提交任务加入成功了
            //再次获取 ctl
            int recheck = ctl.get();
            // 1.如果线程池不是RUNNING状态，原因：当前线程池状态发生了变化 被外部线程修改了
            // 2. 需要删除任务了
            // 删除任务：可能成功、失败
            //失败：提交之后  在被 shutdown() 之前被线程池中的线程 拿去消费了 不用管了
            // 成功：提交之后 还未消费 ；从阻塞队列中删除任务，则该任务由当前 RejectedExecutionHandler 处理
            if (!isRunning(recheck) && remove(command)) {
                //非 running 且 移除任务失败  就会执行拒绝策略
                reject(command);
            }

            //什么时候会走到这里？
            //1. 当前线程池的状态为 running
            //2. 当前线程池状态是非 running 但是 remove 提交的任务失败
            //原因：前提是已经加入到队列中去了，担心池的状态是 running 但是线程池中可运⾏的线程数量为0，得保证有一个线程在工作；担保机制；
            else if (workerCountOf(recheck) == 0) {
                //  则通过 addWorker(null, false) 尝试新建⼀个线程，新建线程对应的任务为null
                addWorker(null, false);
            }

            //1. workQueue.offer(command) 提交失败
            //2. 当前线程池状态不是 RUNNING 状态
            // workQueue.offer() 失败：说明 队列满了，这个时候如果 线程数量尚未达到最大的线程数量 的话，就尝试创建新的 worker 线程 直接执行 任务
            // 非RUNNING ：因为 command ！=null 那么 add 就一定失败 最后执行拒绝策略；表示不能再创建新的线程了;
        } else if (!addWorker(command, false)) {
            // 如果创建新的 worker 线程失败，就执行拒绝策略
            reject(command);
        }
    }

    /**
     * Initiates发起 an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     *
     * <p>This method does not wait for previously submitted tasks to
     * complete execution.  Use {@link #awaitTermination awaitTermination}
     * to do that.
     *
     * @throws SecurityException {@inheritDoc}
     *                           要求任务必须执行完成，那么就是用shutdown()方法
     */
    @Override
    public void shutdown() {
        //获取全局锁
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            //判断是否有权限
            checkShutdownAccess();
            //设置线程池状态为 SHUTDOWN
            advanceRunState(SHUTDOWN);
            //中断空闲线程
            interruptIdleWorkers();
            // 空方法，由子类具体去实现，例如 ScheduledThreadPoolExecutor
            onShutdown(); // hook for ScheduledThreadPoolExecutor
        } finally {
            mainLock.unlock();
        }
        // 尝试将线程池的状态设置为TERMINATED
        tryTerminate();
    }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks, and returns a list of the tasks
     * that were awaiting execution. These tasks are drained (removed)
     * from the task queue upon return from this method.
     * <p>This method does not wait for actively executing tasks to
     * terminate.  Use {@link #awaitTermination awaitTermination} to
     * do that.
     *
     * <p>There are no guarantees beyond best-effort attempts to stop
     * processing actively executing tasks.  This implementation
     * cancels tasks via {@link Thread#interrupt}, so any task that
     * fails to respond to interrupts may never terminate.
     *
     * @throws SecurityException {@inheritDoc}
     *                           设置STOP 状态 + 把未处理的任务 导出来  进行返回
     */
    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            //鉴权
            checkShutdownAccess();
            //尝试设置状态
            advanceRunState(STOP);
            //中断所有线程
            interruptWorkers();
            //进行导出未处理的 任务
            // 清除任务队列中的所有任务
            tasks = drainQueue();
        } finally {
            mainLock.unlock();
        }
        //尝试把线程池状态设置为 TERMINATED 状态
        tryTerminate();
        return tasks;
    }

    /*
    调⽤shutdown()后，线程池状态⽴刻变为SHUTDOWN，⽽调⽤shutdownNow()，线程池状态⽴刻变为STOP。
    shutdown()等待任务执⾏完才中断线程，⽽shutdownNow()不等任务执⾏完就中断了线程。
     */

    @Override
    public boolean isShutdown() {
        return !isRunning(ctl.get());
    }

    /**
     * Returns true if this executor is in the process of terminating
     * after {@link #shutdown} or {@link #shutdownNow} but has not
     * completely terminated.  This method may be useful for
     * debugging. A return of {@code true} reported a sufficient
     * period after shutdown may indicate that submitted tasks have
     * ignored or suppressed interruption, causing this executor not
     * to properly terminate.
     *
     * @return {@code true} if terminating but not yet terminated
     */
    public boolean isTerminating() {
        int c = ctl.get();
        return !isRunning(c) && runStateLessThan(c, TERMINATED);
    }

    public boolean isTerminated() {
        return runStateAtLeast(ctl.get(), TERMINATED);
    }

    //调用了线程池 shutdown与 shutdownNow方法之后，用户程序都不会主动等待线程池关闭完
    //成，如果需要等到线程池完成关闭，需要调用awaitTermination进行主动等待
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (; ; ) {
                if (runStateAtLeast(ctl.get(), TERMINATED))
                    return true;
                if (nanos <= 0)
                    return false;
                nanos = termination.awaitNanos(nanos);
            }
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Invokes {@code shutdown} when this executor is no longer
     * referenced and it has no threads.
     */
    protected void finalize() {
        SecurityManager sm = System.getSecurityManager();
        if (sm == null || acc == null) {
            shutdown();
        } else {
            PrivilegedAction<Void> pa = () -> {
                shutdown();
                return null;
            };
            AccessController.doPrivileged(pa, acc);
        }
    }

    /**
     * Sets the thread factory used to create new threads.
     *
     * @param threadFactory the new thread factory
     * @throws NullPointerException if threadFactory is null
     * @see #getThreadFactory
     */
    public void setThreadFactory(ThreadFactory threadFactory) {
        if (threadFactory == null)
            throw new NullPointerException();
        this.threadFactory = threadFactory;
    }

    /**
     * Returns the thread factory used to create new threads.
     *
     * @return the current thread factory
     * @see #setThreadFactory(ThreadFactory)
     */
    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    /**
     * Sets a new handler for unexecutable tasks.
     *
     * @param handler the new handler
     * @throws NullPointerException if handler is null
     * @see #getRejectedExecutionHandler
     */
    public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
        if (handler == null)
            throw new NullPointerException();
        this.handler = handler;
    }

    /**
     * Returns the current handler for unexecutable tasks.
     *
     * @return the current handler
     * @see #setRejectedExecutionHandler(RejectedExecutionHandler)
     */
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return handler;
    }

    /**
     * Sets the core number of threads.  This overrides any value set
     * in the constructor.  If the new value is smaller than the
     * current value, excess existing threads will be terminated when
     * they next become idle.  If larger, new threads will, if needed,
     * be started to execute any queued tasks.
     *
     * @param corePoolSize the new core size
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @see #getCorePoolSize
     * 而且，你再品一品 JDK 的源码，其实源码也体现出了有修改的含义的，两个值去做差值，只是第一次设置的时候原来的值为 0 而已。
     */
    public void setCorePoolSize(int corePoolSize) {
        if (corePoolSize < 0) throw new IllegalArgumentException();
        //差值 可正可小
        int delta = corePoolSize - this.corePoolSize;
        this.corePoolSize = corePoolSize;
        //如果现在 工作的线程数量 大于 当前设置的数量
        //多余的worker线程，此时会向当前 idle 闲置的worker线程发起中断请求以实现回收，多余的worker在下次 idle 的时候也会被回收；
        if (workerCountOf(ctl.get()) > corePoolSize) {
            //执行中断请求 回收多余的线程
            interruptIdleWorkers();
        } else if (delta > 0) {
            // We don't really know how many new threads are "needed".
            // As a heuristic, prestart enough new workers (up to new
            // core size) to handle the current number of tasks in
            // queue, but stop if queue becomes empty while doing so.
            int k = Math.min(delta, workQueue.size());
            //对于当前corePoolSize值大于原始corePoolSize值且当前队列中有待执行任务，则线程池会创建新的worker线程来执行队列任务
            while (k-- > 0 && addWorker(null, true)) {
                if (workQueue.isEmpty())
                    break;
            }
        }
    }

    /**
     * Returns the core number of threads.
     *
     * @return the core number of threads
     * @see #setCorePoolSize
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * Starts a core thread, causing it to idly wait for work. This
     * overrides the default policy of starting core threads only when
     * new tasks are executed. This method will return {@code false}
     * if all core threads have already been started.
     *
     * @return {@code true} if a thread was started
     * 原生线程池的核心线程一定伴随着任务慢慢创建的吗？
     * 可以提前创建等于核心线程数的线程数量，这种方式被称为预热，在抢购系统中就经常被用到
     */
    public boolean prestartCoreThread() {
        return workerCountOf(ctl.get()) < corePoolSize && addWorker(null, true);
    }

    /**
     * Same as prestartCoreThread except arranges that at least one thread is started even if corePoolSize is 0.
     * 尽管 corePoolSize 可能为0 但是还是尽量启动一个线程
     */
    void ensurePrestart() {
        //当前工作线程
        int wc = workerCountOf(ctl.get());
        //如果当前线程小于 核心数 新建线程
        if (wc < corePoolSize) {
            addWorker(null, true);
        }
        //如果当前线程等于0 新建线程
        else if (wc == 0) {
            addWorker(null, false);
        }
        //如果当前线程 等于 核心线程数 什么也不干

    }

    /**
     * Starts all core threads, causing them to idly wait for work. This
     * overrides the default policy of starting core threads only when
     * new tasks are executed.
     *
     * @return the number of threads started
     * 原生线程池的核心线程一定伴随着任务慢慢创建的吗？
     * 可以提前创建等于核心线程数的线程数量，这种方式被称为预热，在抢购系统中就经常被用到
     */
    public int prestartAllCoreThreads() {
        int n = 0;
        while (addWorker(null, true))
            ++n;
        return n;
    }

    /**
     * Returns true if this pool allows core threads to time out and
     * terminate if no tasks arrive within the keepAlive time, being
     * replaced if needed when new tasks arrive. When true, the same
     * keep-alive policy applying to non-core threads applies also to
     * core threads. When false (the default), core threads are never
     * terminated due to lack of incoming tasks.
     *
     * @return {@code true} if core threads are allowed to time out,
     * else {@code false}
     * @since 1.6
     */
    public boolean allowsCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    /**
     * Sets the policy governing whether core threads may time out and
     * terminate if no tasks arrive within the keep-alive time, being
     * replaced if needed when new tasks arrive. When false, core
     * threads are never terminated due to lack of incoming
     * tasks. When true, the same keep-alive policy applying to
     * non-core threads applies also to core threads. To avoid
     * continual thread replacement, the keep-alive time must be
     * greater than zero when setting {@code true}. This method
     * should in general be called before the pool is actively used.
     *
     * @param value {@code true} if should time out, else {@code false}
     * @throws IllegalArgumentException if value is {@code true}
     *                                  and the current keep-alive time is not greater than zero
     * @since 1.6
     */
    public void allowCoreThreadTimeOut(boolean value) {
        if (value && keepAliveTime <= 0)
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        if (value != allowCoreThreadTimeOut) {
            allowCoreThreadTimeOut = value;
            if (value)
                interruptIdleWorkers();
        }
    }

    /**
     * Sets the maximum allowed number of threads. This overrides any
     * value set in the constructor. If the new value is smaller than
     * the current value, excess existing threads will be
     * terminated when they next become idle.
     *
     * @param maximumPoolSize the new maximum
     * @throws IllegalArgumentException if the new maximum is
     *                                  less than or equal to zero, or
     *                                  less than the {@linkplain #getCorePoolSize core pool size}
     * @see #getMaximumPoolSize
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize <= 0 || maximumPoolSize < corePoolSize)
            throw new IllegalArgumentException();
        this.maximumPoolSize = maximumPoolSize;
        if (workerCountOf(ctl.get()) > maximumPoolSize)
            interruptIdleWorkers();
    }

    /**
     * Returns the maximum allowed number of threads.
     *
     * @return the maximum allowed number of threads
     * @see #setMaximumPoolSize
     */
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * Sets the time limit for which threads may remain idle before
     * being terminated.  If there are more than the core number of
     * threads currently in the pool, after waiting this amount of
     * time without processing a task, excess threads will be
     * terminated.  This overrides any value set in the constructor.
     *
     * @param time the time to wait.  A time value of zero will cause
     *             excess threads to terminate immediately after executing tasks.
     * @param unit the time unit of the {@code time} argument
     * @throws IllegalArgumentException if {@code time} less than zero or
     *                                  if {@code time} is zero and {@code allowsCoreThreadTimeOut}
     * @see #getKeepAliveTime(TimeUnit)
     */
    public void setKeepAliveTime(long time, TimeUnit unit) {
        if (time < 0)
            throw new IllegalArgumentException();
        if (time == 0 && allowsCoreThreadTimeOut())
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        long keepAliveTime = unit.toNanos(time);
        long delta = keepAliveTime - this.keepAliveTime;
        this.keepAliveTime = keepAliveTime;
        if (delta < 0)
            interruptIdleWorkers();
    }

    /**
     * Returns the thread keep-alive time, which is the amount of time
     * that threads in excess of the core pool size may remain
     * idle before being terminated.
     *
     * @param unit the desired time unit of the result
     * @return the time limit
     * @see #setKeepAliveTime(long, TimeUnit)
     */
    public long getKeepAliveTime(TimeUnit unit) {
        return unit.convert(keepAliveTime, TimeUnit.NANOSECONDS);
    }

    /* User-level queue utilities */

    /**
     * Returns the task queue used by this executor. Access to the
     * task queue is intended primarily for debugging and monitoring.
     * This queue may be in active use.  Retrieving the task queue
     * does not prevent queued tasks from executing.
     *
     * @return the task queue
     */
    public BlockingQueue<Runnable> getQueue() {
        return workQueue;
    }

    /**
     * Removes this task from the executor's internal queue if it is
     * present, thus causing it not to be run if it has not already
     * started.
     *
     * <p>This method may be useful as one part of a cancellation
     * scheme.  It may fail to remove tasks that have been converted
     * into other forms before being placed on the internal queue. For
     * example, a task entered using {@code submit} might be
     * converted into a form that maintains {@code Future} status.
     * However, in such cases, method {@link #purge} may be used to
     * remove those Futures that have been cancelled.
     *
     * @param task the task to remove
     * @return {@code true} if the task was removed
     */
    public boolean remove(Runnable task) {
        boolean removed = workQueue.remove(task);
        tryTerminate(); // In case SHUTDOWN and now empty
        return removed;
    }

    /**
     * Tries to remove from the work queue all {@link Future}
     * tasks that have been cancelled. This method can be useful as a
     * storage reclamation operation, that has no other impact on
     * functionality. Cancelled tasks are never executed, but may
     * accumulate in work queues until worker threads can actively
     * remove them. Invoking this method instead tries to remove them now.
     * However, this method may fail to remove tasks in
     * the presence of interference by other threads.
     */
    public void purge() {
        final BlockingQueue<Runnable> q = workQueue;
        try {
            Iterator<Runnable> it = q.iterator();
            while (it.hasNext()) {
                Runnable r = it.next();
                if (r instanceof Future<?> && ((Future<?>) r).isCancelled())
                    it.remove();
            }
        } catch (ConcurrentModificationException fallThrough) {
            // Take slow path if we encounter interference during traversal.
            // Make copy for traversal and call remove for cancelled entries.
            // The slow path is more likely to be O(N*N).
            for (Object r : q.toArray())
                if (r instanceof Future<?> && ((Future<?>) r).isCancelled())
                    q.remove(r);
        }

        tryTerminate(); // In case SHUTDOWN and now empty
    }

    /* Statistics */

    /**
     * Returns the current number of threads in the pool.
     *
     * @return the number of threads
     */
    public int getPoolSize() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            // Remove rare and surprising possibility of
            // isTerminated() && getPoolSize() > 0
            return runStateAtLeast(ctl.get(), TIDYING) ? 0
                    : workers.size();
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Returns the approximate number of threads that are actively
     * executing tasks.
     *
     * @return the number of threads
     */
    public int getActiveCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            int n = 0;
            for (Worker w : workers)
                if (w.isLocked())
                    ++n;
            return n;
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Returns the largest number of threads that have ever
     * simultaneously been in the pool.
     *
     * @return the number of threads
     */
    public int getLargestPoolSize() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            return largestPoolSize;
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Returns the approximate total number of tasks that have ever been
     * scheduled for execution. Because the states of tasks and
     * threads may change dynamically during computation, the returned
     * value is only an approximation.
     *
     * @return the number of tasks
     */
    public long getTaskCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            long n = completedTaskCount;
            for (Worker w : workers) {
                n += w.completedTasks;
                if (w.isLocked())
                    ++n;
            }
            return n + workQueue.size();
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Returns the approximate total number of tasks that have
     * completed execution. Because the states of tasks and threads
     * may change dynamically during computation, the returned value
     * is only an approximation, but one that does not ever decrease
     * across successive calls.
     *
     * @return the number of tasks
     */
    public long getCompletedTaskCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            long n = completedTaskCount;
            for (Worker w : workers)
                n += w.completedTasks;
            return n;
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Returns a string identifying this pool, as well as its state,
     * including indications of run state and estimated worker and
     * task counts.
     *
     * @return a string identifying this pool, as well as its state
     */
    public String toString() {
        long ncompleted;
        int nworkers, nactive;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            ncompleted = completedTaskCount;
            nactive = 0;
            nworkers = workers.size();
            for (Worker w : workers) {
                ncompleted += w.completedTasks;
                if (w.isLocked())
                    ++nactive;
            }
        } finally {
            mainLock.unlock();
        }
        int c = ctl.get();
        String rs = (runStateLessThan(c, SHUTDOWN) ? "Running" :
                (runStateAtLeast(c, TERMINATED) ? "Terminated" :
                        "Shutting down"));
        return super.toString() +
                "[" + rs +
                ", pool size = " + nworkers +
                ", active threads = " + nactive +
                ", queued tasks = " + workQueue.size() +
                ", completed tasks = " + ncompleted +
                "]";
    }

    /* Extension hooks */

    /**
     * Method invoked prior to executing the given Runnable in the
     * given thread.  This method is invoked by thread {@code t} that
     * will execute task {@code r}, and may be used to re-initialize
     * ThreadLocals, or to perform logging.
     *
     * <p>This implementation does nothing, but may be customized in
     * subclasses. Note: To properly nest multiple overridings, subclasses
     * should generally invoke {@code super.beforeExecute} at the end of
     * this method.
     *
     * @param t the thread that will run task {@code r}
     * @param r the task that will be executed
     */
    protected void beforeExecute(Thread t, Runnable r) {
    }

    /**
     * Method invoked upon completion of execution of the given Runnable.
     * This method is invoked by the thread that executed the task. If
     * non-null, the Throwable is the uncaught {@code RuntimeException}
     * or {@code Error} that caused execution to terminate abruptly.
     *
     * <p>This implementation does nothing, but may be customized in
     * subclasses. Note: To properly nest multiple overridings, subclasses
     * should generally invoke {@code super.afterExecute} at the
     * beginning of this method.
     *
     * <p><b>Note:</b> When actions are enclosed in tasks (such as
     * {@link FutureTask}) either explicitly or via methods such as
     * {@code submit}, these task objects catch and maintain
     * computational exceptions, and so they do not cause abrupt
     * termination, and the internal exceptions are <em>not</em>
     * passed to this method. If you would like to trap both kinds of
     * failures in this method, you can further probe for such cases,
     * as in this sample subclass that prints either the direct cause
     * or the underlying exception if a task has been aborted:
     *
     * <pre> {@code
     * class ExtendedExecutor extends ThreadPoolExecutor {
     *   // ...
     *   protected void afterExecute(Runnable r, Throwable t) {
     *     super.afterExecute(r, t);
     *     if (t == null && r instanceof Future<?>) {
     *       try {
     *         Object result = ((Future<?>) r).get();
     *       } catch (CancellationException ce) {
     *           t = ce;
     *       } catch (ExecutionException ee) {
     *           t = ee.getCause();
     *       } catch (InterruptedException ie) {
     *           Thread.currentThread().interrupt(); // ignore/reset
     *       }
     *     }
     *     if (t != null)
     *       System.out.println(t);
     *   }
     * }}</pre>
     *
     * @param r the runnable that has completed
     * @param t the exception that caused termination, or null if
     *          execution completed normally
     */
    protected void afterExecute(Runnable r, Throwable t) {
    }

    /**
     * Method invoked when the Executor has terminated.  Default
     * implementation does nothing. Note: To properly nest multiple
     * overridings, subclasses should generally invoke
     * {@code super.terminated} within this method.
     */
    protected void terminated() {
    }

    /* Predefined RejectedExecutionHandlers */

    /**
     * A handler for rejected tasks that runs the rejected task
     * directly in the calling thread of the {@code execute} method,
     * unless the executor has been shut down, in which case the task
     * is discarded.
     * 让调用者线程 去执行这个任务，万一是 io 型任务 岂不是拉跨了？
     * 适用场景是：要让所有的任务都执行完毕，适合大量计算型任务执行，多线程仅仅是增大了吞吐量，最终还是需要任务都执行完毕
     */
    public static class CallerRunsPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code CallerRunsPolicy}.
         */
        public CallerRunsPolicy() {
        }

        /**
         * Executes task r in the caller's thread, unless the executor
         * has been shut down, in which case the task is discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //提交任务的线程自己处理
            //倘若自己还能执行
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }

    /**
     * A handler for rejected tasks that throws a
     * {@code RejectedExecutionException}.
     */
    public static class AbortPolicy implements RejectedExecutionHandler {
        /**
         * Creates an {@code AbortPolicy}.
         */
        public AbortPolicy() {
        }

        /**
         * Always throws RejectedExecutionException.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         * @throws RejectedExecutionException always
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            // 不再接收新任务，直接抛出异常
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
        }
    }

    /**
     * A handler for rejected tasks that silently discards the
     * rejected task.
     */
    public static class DiscardPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code DiscardPolicy}.
         */
        public DiscardPolicy() {
        }

        /**
         * Does nothing, which has the effect of discarding task r.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //不处理，直接丢弃

        }
    }

    /**
     * A handler for rejected tasks that discards the oldest unhandled
     * request and then retries {@code execute}, unless the executor
     * is shut down, in which case the task is discarded.
     */
    public static class DiscardOldestPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code DiscardOldestPolicy} for the given executor.
         */
        public DiscardOldestPolicy() {
        }

        /**
         * Obtains and ignores the next task that the executor
         * would otherwise execute, if one is immediately available,
         * and then retries execution of task r, unless the executor
         * is shut down, in which case task r is instead discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //丢弃任务队列中排在最前面的任务，并执行当前任务
            //( 排在队列最前面的任务并不一定是在队列中待的时间最长的任务，因为有可能是按照优先级排序的队列 )
            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        }
    }
}
