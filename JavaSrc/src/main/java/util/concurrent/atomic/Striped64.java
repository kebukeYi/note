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
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.atomic;

import java.util.function.LongBinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A package-local class holding common representation and mechanics
 * for classes supporting dynamic striping on 64bit values. The class
 * extends Number so that concrete subclasses must publicly do so.
 */
@SuppressWarnings("serial")
abstract class Striped64 extends Number {
    /*
     * This class maintains a lazily-initialized table of atomically
     * updated variables, plus an extra "base" field. The table size
     * is a power of two. Indexing uses masked per-thread hash codes.
     * Nearly all declarations in this class are package-private,
     * accessed directly by subclasses.
     *
     * Table entries are of class Cell; a variant of AtomicLong padded
     * (via @sun.misc.Contended) to reduce cache contention. Padding
     * is overkill for most Atomics because they are usually
     * irregularly scattered in memory and thus don't interfere much
     * with each other. But Atomic objects residing in arrays will
     * tend to be placed adjacent to each other, and so will most
     * often share cache lines (with a huge negative performance
     * impact) without this precaution.
     *
     * In part because Cells are relatively large, we avoid creating
     * them until they are needed.  When there is no contention, all
     * updates are made to the base field.  Upon first contention (a
     * failed CAS on base update), the table is initialized to size 2.
     * The table size is doubled upon further contention until
     * reaching the nearest power of two greater than or equal to the
     * number of CPUS. Table slots remain empty (null) until they are
     * needed.
     *
     * A single spinlock ("cellsBusy") is used for initializing and
     * resizing the table, as well as populating slots with new Cells.
     * There is no need for a blocking lock; when the lock is not
     * available, threads try other slots (or the base).  During these
     * retries, there is increased contention and reduced locality,
     * which is still better than alternatives.
     *
     * The Thread probe fields maintained via ThreadLocalRandom serve
     * as per-thread hash codes. We let them remain uninitialized as
     * zero (if they come in this way) until they contend at slot
     * 0. They are then initialized to values that typically do not
     * often conflict with others.  Contention and/or table collisions
     * are indicated by failed CASes when performing an update
     * operation. Upon a collision, if the table size is less than
     * the capacity, it is doubled in size unless some other thread
     * holds the lock. If a hashed slot is empty, and lock is
     * available, a new Cell is created. Otherwise, if the slot
     * exists, a CAS is tried.  Retries proceed by "double hashing",
     * using a secondary hash (Marsaglia XorShift) to try to find a
     * free slot.
     *
     * The table size is capped because, when there are more threads
     * than CPUs, supposing that each thread were bound to a CPU,
     * there would exist a perfect hash function mapping threads to
     * slots that eliminates collisions. When we reach capacity, we
     * search for this mapping by randomly varying the hash codes of
     * colliding threads.  Because search is random, and collisions
     * only become known via CAS failures, convergence can be slow,
     * and because threads are typically not bound to CPUS forever,
     * may not occur at all. However, despite these limitations,
     * observed contention rates are typically low in these cases.
     *
     * It is possible for a Cell to become unused when threads that
     * once hashed to it terminate, as well as in the case where
     * doubling the table causes no thread to hash to it under
     * expanded mask.  We do not try to detect or remove such cells,
     * under the assumption that for long-running instances, observed
     * contention levels will recur, so the cells will eventually be
     * needed again; and for short-lived ones, it does not matter.
     */

    /**
     * Padded variant of AtomicLong supporting only raw accesses plus CAS.
     * <p>
     * JVM intrinsics note: It would be possible to use a release-only form of CAS here, if it were provided.
     * 因为Cell数组元素的内存地址是连续的，所以数组内的多个元素能经常共享缓存行，
     * 因此这里使用@sun.misc.Contended注解对Cell类进行字节填充，
     * 防止数组中多个元素共享一个缓存行，提升性能
     */
    @sun.misc.Contended
    static final class Cell {
        //
        volatile long value;

        Cell(long x) {
            value = x;
        }

        final boolean cas(long cmp, long val) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
        }

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        //当前对象的偏移位置
        private static final long valueOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> ak = Cell.class;
                valueOffset = UNSAFE.objectFieldOffset
                        (ak.getDeclaredField("value"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * Number of CPUS, to place bound on table size
     * 控制cell数组长度的一个关键条件
     */
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    /**
     * Table of cells. When non-null, size is a power of 2.
     * 一直是2的次幂方
     */
    transient volatile Cell[] cells;

    /**
     * Base value, used mainly when there is no contention, but also as
     * a fallback during table initialization races. Updated via CAS.
     * 1. 在没有竞争时会更新这个值
     * 2. 在 cells 初始化时 cells 不可用，也会尝试将通过 cas 操作值累加到 base
     */
    transient volatile long base;

    /**
     * Spinlock (locked via CAS) used when resizing and/or creating Cells.
     * 初始化 cell 或者扩容 cell 时需要获得 锁
     * 1 表示其他线程获得锁
     * 0 表示其他线程无锁
     */
    transient volatile int cellsBusy;

    /**
     * Package-private default constructor
     */
    Striped64() {
    }

    /**
     * CASes the base field.
     * 通过原子方式  修改值
     */
    final boolean casBase(long cmp, long val) {
        return UNSAFE.compareAndSwapLong(this, BASE, cmp, val);
    }

    /**
     * CASes the cellsBusy field from 0 to 1 to acquire lock.
     * 通过原子方式 获取锁
     */
    final boolean casCellsBusy() {
        return UNSAFE.compareAndSwapInt(this, CELLSBUSY, 0, 1);
    }

    /**
     * Returns the probe value for the current thread.
     * Duplicated from ThreadLocalRandom because of packaging restrictions.
     * 获取当前线程的hash值
     * getProbe()用于获取当前线程中变量threadLocalRandomProbe的值，它一开始为0，在代码5中会对其进行初始化
     */
    static final int getProbe() {
        return UNSAFE.getInt(Thread.currentThread(), PROBE);
    }

    /**
     * Pseudo-randomly advances and records the given probe value for the
     * given thread.
     * Duplicated from ThreadLocalRandom because of packaging restrictions.
     * 重置当前线程的hash值
     */
    static final int advanceProbe(int probe) {
        probe ^= probe << 13;
        probe ^= probe >>> 17;
        probe ^= probe << 5;
        UNSAFE.putInt(Thread.currentThread(), PROBE, probe);
        return probe;
    }

    /**
     * Handles cases of updates involving initialization, resizing,
     * creating new Cells, and/or contention. See above for
     * explanation. This method suffers the usual non-modularity
     * problems of optimistic retry code, relying on rechecked sets of
     * reads.
     *
     * @param x              the value
     * @param fn             the update function, or null for add (this convention avoids the need for an extra field or function in LongAdder).
     * @param wasUncontended false if CAS failed before call
     */
    //进来这里的情况？
    //1. 条件一  成立 说明 cell 未被初始化 也就是多线程写 base 出现竞争了 需要重试初始化 cells
    //2. 条件二  成立 说明 cell 已被初始化 当前线程对应的 cell 为 null，需要创建
    //3. 条件三  成立 说明 cell 已被初始化 当前线程对应的 cell 存在竞争，需要重试|扩容
    final void longAccumulate(long x, LongBinaryOperator fn, boolean wasUncontended) {
        //表示hash值
        int h;
        //条件成立：说明还未分配 hash 值 需要重新分配hash值
        // 这个if相当于给线程生成一个非0的hash值
        if ((h = getProbe()) == 0) {
            //给当前线程分配 hash 值
            ThreadLocalRandom.current(); // force initialization
            h = getProbe();
            //为什么？
            //默认 hash为 0 的线程肯定是写入到了 0 这个 槽位，不把它当作是真正的竞争
            //true 未发生竞争    false 发生了竞争
            wasUncontended = true;
        }

        //表示扩容 意向：false 一定不扩容  true 可能会扩容
        boolean collide = false;
        // 自旋
        for (; ; ) {
            //表示 cells 引用
            Cell[] as;
            //当前线程命中的cell
            Cell a;
            //表示cells 数组长度
            int n;
            //期望值
            long v;

            //CASE1 除非是 还未被初始化 转 CASE2
            //条件一：成立 : Cells 已经被初始化了 当前线程需要把数据写入到对应的 cell 中 ; 进入当前if分支
            //             不成立 : 转363行代码 进行第二个判断 目的是为了初始化 cells
            if ((as = cells) != null && (n = as.length) > 0) {
                //进来的前提条件：
                //2. 条件二  成立 说明 cell 已被初始化 当前线程对应的 cell 为 null，需要创建
                //3. 条件三  成立 说明 cell 已被初始化 当前线程对应的 cell 存在竞争，需要重试|扩容

                // 成立的话：表示当前线程对应的 cells 下标 cells 为空 需要创建 new Cell 实例
                if ((a = as[(n - 1) & h]) == null) {
                    // 成立的话 表示当前是无锁状态 锁未占用
                    if (cellsBusy == 0) {
                        //新的cell 元素
                        Cell r = new Cell(x);
                        //是无锁状态 且 能获取到锁  且 尝试加锁（防止线程并发操作）
                        if (cellsBusy == 0 && casCellsBusy()) {
                            //是否创建 成功 标记
                            boolean created = false;
                            try {               // Recheck under lock 在有锁的情况下再检测一遍之前的判断
                                //表示当前 cells 引用
                                Cell[] rs;
                                //表示长度
                                int m, j;
                                //考虑别的线程可能执行了扩容，这里重新赋值重新判断
                                if ((rs = cells) != null && (m = rs.length) > 0 &&
                                        //当 当前线程发生了退出cpu 又回到cpu 时
                                        // 为了防止其他线程在此期间初始化过该位置  然后当前线程再次初始化此位置  导致   数据覆盖（如果被覆盖了就不需要覆盖了 需要再次寻址）
                                        rs[j = (m - 1) & h] == null) {
                                    //对没有使用的Cell单元进行累积操作（第一次赋值相当于是累积上一个操作数，求和时再和base执行一次运算就得到实际的结果）
                                    rs[j] = r;
                                    //创建完毕
                                    created = true;
                                }
                            } finally {
                                //释放锁
                                cellsBusy = 0;
                            }
                            //如果原本为null的Cell单元是由自己进行第一次累积操作，那么任务已经完成了，所以可以退出循环
                            if (created) {
                                break;
                            }
                            //不是自己进行第一次累积操作，重头再来
                            continue;           // Slot is now non-empty
                        }
                    }
                    //强制设置为不扩容
                    //执行这一句是因为cells被加锁了，不能往下继续执行第一次的赋值操作（第一次累积），所以还不能考虑扩容
                    collide = false;
                }

                // 此时 线程对应的 cell 元素不为空 只是上一个add()函数调用cas 赋值时 失败 就进来了这个分支
                // wasUncontended ：只有cells 初始化后  并且 当前线程 是 竞争修改失败 才会 false,也就是只会执行一次
                else if (!wasUncontended) {      // CAS already known to fail add()函数执行CAS更新a.value（进行一次累积）的尝试已经失败了，说明已经发生了线程竞争
                    //情况失败标识，然后跳出此 if 去执行最后代码块的 重新算一遍线程的hash值 然后重新 循环
                    wasUncontended = true;
                }     // Continue after rehash

                //线程对应位置不为空 && 上一个if分支也执行过了 线程也重新执行rehash了  就来到了这里 再次尝试执行cas追加操作
                //这一步才是最常用到的操作，一般都是 fn == null 直接更新 v+x
                //当前线程 rehash 过 新命中的 cell 不为空
                //如果写入成功 就退出自旋  ；
                // 否则 表示 rehash  之后 命中的新cell 也存在竞争 重试1次
                else if (a.cas(v = a.value, ((fn == null) ? v + x : fn.applyAsLong(v, x)))) {
                    //执行成功 完事了 要是没有成功就再继续rehash线程 继续循环
                    break;
                }

                //线程对应位置不为空 && 上一个if分支也执行过了 线程也重新执行rehash了 && 相应的新位置 cell 执行cas追加操作失败
                //条件一：如果数组长度大于cpu数量 成立就表示扩容意向为 false；如果没有大于 就走下一个判断条件
                //条件二：如果 cells != as 表示当前有其他线程已经 扩容过了，当前线程 rehash 过重试即可
                else if (n >= NCPU || cells != as) {
                    //表示不扩容了
                    collide = false; //长度n是递增的，执行到了这个分支，说明n >= NCPU会永远为true，下面两个else if就永远不会被执行了，也就永远不会再进行扩容
                }    // At max size or stale

                //collide 取反成立后 设置扩容意向  并不一定 真的 发生扩容
                else if (!collide) {
                    //把扩容意向设置为true，只有这里才会给collide赋值为true，也只有执行了这一句，才可能执行后面一个else if进行扩容
                    collide = true;
                }

                //线程对应位置不为空 && 上一个if分支也执行过了 线程也重新执行rehash了 && 相应的新位置 cell 执行cas追加操作失败 && 执行扩容

                //真正扩容的代码
                //条件一：cellsBusy == 0 表示无锁状态，当前线程可以去竞争这把锁
                //                    成立：走下一个判断条件
                //                 不成立： 直接 走到代码最后 再次给当前线程重新 rehash 机会
                //条件二：casCellsBusy()   并且 通过原子方式 获取锁
                //                    成立：走进代码内部
                //                 不成立：不会走到这一步
                else if (cellsBusy == 0 && casCellsBusy()) {
                    try {
                        //检查下是否被别的线程扩容了（CAS更新锁标识，处理不了ABA问题，这里再检查一遍）
                        if (cells == as) {
                            //执行2倍扩容
                            Cell[] rs = new Cell[n << 1];
                            for (int i = 0; i < n; ++i) {
                                rs[i] = as[i];
                            }
                            //扩容完毕 内存可见性也设置了
                            cells = rs;
                        }
                    } finally {
                        //释放锁
                        cellsBusy = 0;
                    }
                    //回归扩容意向
                    collide = false;
                    continue;                   // Retry with expanded table 扩容后重头再来
                }

                //重新给线程生成一个hash值，降低hash冲突，减少映射到同一个Cell导致CAS竞争的情况
                h = advanceProbe(h);
            }

            // Cells 没有被初始化，并且也没有被加锁（被别的线程正在初始化）那么就尝试对它进行加锁，加锁成功进入这个else if
            // CASE2 前置条件：cells 还未被初始化，需要被初始化
            // 条件一：cellsBusy == 0  成立：说明 当前未加锁；
            // 条件二： cells == as 再次对比 因为 害怕之前 多线程之前已经执行初始化了；
            // 条件二： casCellsBusy()  表示获取锁成功  ；失败 表示其他锁持有这个锁；
            else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
                //线程在拿锁状态下
                boolean init = false;
                // Initialize table
                try {
                    // 又来对比 防止其他线程已经初始化了 当前线程又来进行初始化 造成数据丢失
                    if (cells == as) {//CAS避免不了ABA问题，这里再检测一次，如果还是null，或者空数组，那么就执行初始化
                        Cell[] rs = new Cell[2];//初始化时只创建两个单元
                        // h为线程的hash值， 对其中一个单元进行累积操作，另一个不管，继续为 null
                        rs[h & 1] = new Cell(x);
                        cells = rs;
                        //初始化结束
                        init = true;
                    }
                } finally {
                    // 清空自旋标识，释放锁
                    cellsBusy = 0;
                }
                // 如果某个原本为null的Cell单元是由自己进行第一次累积操作，那么任务已经完成了，所以可以退出循环
                if (init) {
                    //初始化完毕了 + 线程对应的 cell 值也 赋值好了 那就完事了  直接退出代码块
                    break;
                }
            }

            //CASE3 ：前提 上面 CASE1 把cells初始化过的处理了; CASE2 把cells初始化处理了; CASE3就是处理 初始化cells失败的兜底方案;
            // 1. casCellsBusy() false -> 当前 CellsBusy 加锁状态 or casCellsBusy() 方法执行失败：表示其他线程正在初始化 cells  所以当前线程需要把数据累计到 base 中
            // 2. cells == as false ->Cells 被其他线程初始化后   所以当前线程需要把数据累计到 base 中
            else if (casBase(v = base, ((fn == null) ? v + x : fn.applyAsLong(v, x)))) {
                //成功加到base后 就完事了 ; 没有加到的话 继续for循环
                break;
            }  // Fall back on using base
        }//for(;;) over
    }//longAccumulate() over

    /**
     * Same as longAccumulate, but injecting long/double conversions
     * in too many places to sensibly merge with long version, given
     * the low-overhead requirements of this class. So must instead be
     * maintained by copy/paste/adapt.
     */
    final void doubleAccumulate(double x, DoubleBinaryOperator fn, boolean wasUncontended) {
        int h;
        if ((h = getProbe()) == 0) {
            ThreadLocalRandom.current(); // force initialization
            h = getProbe();
            wasUncontended = true;
        }
        boolean collide = false;                // True if last slot nonempty
        for (; ; ) {
            Cell[] as;
            Cell a;
            int n;
            long v;
            if ((as = cells) != null && (n = as.length) > 0) {
                if ((a = as[(n - 1) & h]) == null) {
                    if (cellsBusy == 0) {       // Try to attach new Cell
                        Cell r = new Cell(Double.doubleToRawLongBits(x));
                        if (cellsBusy == 0 && casCellsBusy()) {
                            boolean created = false;
                            try {               // Recheck under lock
                                Cell[] rs;
                                int m, j;
                                if ((rs = cells) != null &&
                                        (m = rs.length) > 0 &&
                                        rs[j = (m - 1) & h] == null) {
                                    rs[j] = r;
                                    created = true;
                                }
                            } finally {
                                cellsBusy = 0;
                            }
                            if (created)
                                break;
                            continue;           // Slot is now non-empty
                        }
                    }
                    collide = false;
                } else if (!wasUncontended)       // CAS already known to fail
                    wasUncontended = true;      // Continue after rehash
                else if (a.cas(v = a.value,
                        ((fn == null) ?
                                Double.doubleToRawLongBits
                                        (Double.longBitsToDouble(v) + x) :
                                Double.doubleToRawLongBits
                                        (fn.applyAsDouble
                                                (Double.longBitsToDouble(v), x)))))
                    break;
                else if (n >= NCPU || cells != as)
                    collide = false;            // At max size or stale
                else if (!collide)
                    collide = true;
                else if (cellsBusy == 0 && casCellsBusy()) {
                    try {
                        if (cells == as) {      // Expand table unless stale
                            Cell[] rs = new Cell[n << 1];
                            for (int i = 0; i < n; ++i)
                                rs[i] = as[i];
                            cells = rs;
                        }
                    } finally {
                        cellsBusy = 0;
                    }
                    collide = false;
                    continue;                   // Retry with expanded table
                }
                h = advanceProbe(h);
            } else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
                boolean init = false;
                try {                           // Initialize table
                    if (cells == as) {
                        Cell[] rs = new Cell[2];
                        rs[h & 1] = new Cell(Double.doubleToRawLongBits(x));
                        cells = rs;
                        init = true;
                    }
                } finally {
                    cellsBusy = 0;
                }
                if (init)
                    break;
            } else if (casBase(v = base,
                    ((fn == null) ?
                            Double.doubleToRawLongBits
                                    (Double.longBitsToDouble(v) + x) :
                            Double.doubleToRawLongBits
                                    (fn.applyAsDouble
                                            (Double.longBitsToDouble(v), x)))))
                break;                          // Fall back on using base
        }
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long BASE;
    private static final long CELLSBUSY;
    private static final long PROBE;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> sk = Striped64.class;
            BASE = UNSAFE.objectFieldOffset
                    (sk.getDeclaredField("base"));
            CELLSBUSY = UNSAFE.objectFieldOffset
                    (sk.getDeclaredField("cellsBusy"));
            Class<?> tk = Thread.class;
            PROBE = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomProbe"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
