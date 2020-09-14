/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 */

package java.lang;

import java.lang.ref.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * This class provides thread-local variables.  These variables differ from
 * their normal counterparts in that each thread that accesses one (via its
 * {@code get} or {@code set} method) has its own, independently initialized
 * copy of the variable.  {@code ThreadLocal} instances are typically private
 * static fields in classes that wish to associate state with a thread (e.g.,
 * a user ID or Transaction ID).
 *
 * <p>For example, the class below generates unique identifiers local to each
 * thread.
 * A thread's id is assigned the first time it invokes {@code ThreadId.get()}
 * and remains unchanged on subsequent calls.
 * <pre>
 * import java.util.concurrent.atomic.AtomicInteger;
 *
 * public class ThreadId {
 *     // Atomic integer containing the next thread ID to be assigned
 *     private static final AtomicInteger nextId = new AtomicInteger(0);
 *
 *     // Thread local variable containing each thread's ID
 *     private static final ThreadLocal&lt;Integer&gt; threadId =
 *         new ThreadLocal&lt;Integer&gt;() {
 *             &#64;Override protected Integer initialValue() {
 *                 return nextId.getAndIncrement();
 *         }
 *     };
 *
 *     // Returns the current thread's unique ID, assigning it if necessary
 *     public static int get() {
 *         return threadId.get();
 *     }
 * }
 * </pre>
 * <p>Each thread holds an implicit reference to its copy of a thread-local
 * variable as long as the thread is alive and the {@code ThreadLocal}
 * instance is accessible; after a thread goes away, all of its copies of
 * thread-local instances are subject to garbage collection (unless other
 * references to these copies exist).
 *
 * @author Josh Bloch and Doug Lea
 * @since 1.2
 * <p>
 * ThreadLocal提供了线程的局部变量，每个线程都可以通过 set() 和
 * get() 来对这个局部变量进⾏操作，但不会和其他线程的局部变量进⾏冲突，实现了线程的数据隔离
 * <p>
 * ThreadLocal 能够实现当前线程的操作都是⽤同⼀个 Connection，保证了事务 ！
 */
public class ThreadLocal<T> {
    /**
     * ThreadLocals rely on per-thread linear-probe hash maps attached
     * to each thread (Thread.threadLocals and
     * inheritableThreadLocals).  The ThreadLocal objects act as keys,
     * searched via threadLocalHashCode.  This is a custom hash code
     * (useful only within ThreadLocalMaps) that eliminates collisions
     * in the common case where consecutively constructed ThreadLocals
     * are used by the same threads, while remaining well-behaved in
     * less common cases.
     * 当存在第一个线程过来get操作时，会给当前第一个线程分配一个value
     * 这个value 和 当前的 threadlocal 对象 被包装成为一个entry 其中key是threadlocal对象 value是给当前对象的value
     * 这个entry 放到map的哪个桶位？ 使用此方法获取位置就是当前 entry 的位置
     */
    private final int threadLocalHashCode = nextHashCode();

    /**
     * The next hash code to be given out. Updated atomically. Starts at
     * zero.
     * 创建thradlocal
     */
    private static AtomicInteger nextHashCode = new AtomicInteger();

    /**
     * The difference between successively generated hash codes - turns
     * implicit sequential thread-local IDs into near-optimally spread
     * multiplicative hash values for power-of-two-sized tables.
     * 每创建一个threadlocal d对象，这个threadloacal.next 就会增加多少  是斐波那契数 是一个黄金分割数 使得hash分布十分均匀；
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * Returns the next hash code.
     */
    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**
     * Returns the current thread's "initial value" for this
     * thread-local variable.  This method will be invoked the first
     * time a thread accesses the variable with the {@link #get}
     * method, unless the thread previously invoked the {@link #set}
     * method, in which case the {@code initialValue} method will not
     * be invoked for the thread.  Normally, this method is invoked at
     * most once per thread, but it may be invoked again in case of
     * subsequent invocations of {@link #remove} followed by {@link #get}.
     *
     * <p>This implementation simply returns {@code null}; if the
     * programmer desires thread-local variables to have an initial
     * value other than {@code null}, {@code ThreadLocal} must be
     * subclassed, and this method overridden.  Typically, an
     * anonymous inner class will be used.
     *
     * @return the initial value for this thread-local
     * 默认返回null 一般需要重写此方法
     */
    protected T initialValue() {
        return null;
    }

    /**
     * Creates a thread local variable. The initial value of the variable is
     * determined by invoking the {@code get} method on the {@code Supplier}.
     *
     * @param <S>      the type of the thread local's value
     * @param supplier the supplier to be used to determine the initial value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     * @since 1.8
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    /**
     * Creates a thread local variable.
     *
     * @see #withInitial(java.util.function.Supplier)
     */
    public ThreadLocal() {
    }

    /**
     * Returns the value in the current thread's copy of this
     * thread-local variable.  If the variable has no value for the
     * current thread, it is first initialized to the value returned
     * by an invocation of the {@link #initialValue} method.
     *
     * @return the current thread's value of this thread-local
     * 返回当前线程与当前threadLocal 对相关联的线程局部变量 这个变量只能在x线程内部访问到；
     * 如果没有被分配就使用初始化 initialValue() 方法进行赋值；
     */
    public T get() {
        //获取当前线程
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T) e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    /**
     * Variant of set() to establish initialValue. Used instead
     * of set() in case user has overridden the set() method.
     *
     * @return the initial value
     */
    private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            map.set(this, value);
        } else {
            createMap(t, value);
        }
        return value;
    }

    /**
     * Sets the current thread's copy of this thread-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of thread-locals.
     *
     * @param value the value to be stored in the current thread's copy of this thread-local.
     */
    public void set(T value) {
        Thread t = Thread.currentThread();
        //获取散列表
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            map.set(this, value);
        } else {
            createMap(t, value);
        }
    }

    /**
     * Removes the current thread's value for this thread-local
     * variable.  If this thread-local variable is subsequently
     * {@linkplain #get read} by the current thread, its value will be
     * reinitialized by invoking its {@link #initialValue} method,
     * unless its value is {@linkplain #set set} by the current thread
     * in the interim.  This may result in multiple invocations of the
     * {@code initialValue} method in the current thread.
     *
     * @since 1.5
     */
    public void remove() {
        ThreadLocalMap m = getMap(Thread.currentThread());
        if (m != null)
            m.remove(this);
    }

    /**
     * Get the map associated with a ThreadLocal. Overridden in
     * InheritableThreadLocal.
     *
     * @param t the current thread
     * @return the map
     */
    ThreadLocalMap getMap(Thread t) {
        //返回当前线程的 threadlocal 对象
        return t.threadLocals;
    }

    /**
     * Create the map associated with a ThreadLocal. Overridden in
     * InheritableThreadLocal.
     *
     * @param t          the current thread
     * @param firstValue value for the initial entry of the map
     */
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

    /**
     * Factory method to create map of inherited thread locals.
     * Designed to be called only from Thread constructor.
     *
     * @param parentMap the map associated with parent thread
     * @return a map containing the parent's inheritable bindings
     */
    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap);
    }

    /**
     * Method childValue is visibly defined in subclass
     * InheritableThreadLocal, but is internally defined here for the
     * sake of providing createInheritedMap factory method without
     * needing to subclass the map class in InheritableThreadLocal.
     * This technique is preferable to the alternative of embedding
     * instanceof tests in methods.
     */
    T childValue(T parentValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * An extension of ThreadLocal that obtains its initial value from
     * the specified {@code Supplier}.
     */
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }

    /**
     * ThreadLocalMap is a customized hash map suitable only for
     * maintaining thread local values. No operations are exported
     * outside of the ThreadLocal class. The class is package private to
     * allow declaration of fields in class Thread.  To help deal with
     * very large and long-lived usages, the hash table entries use
     * WeakReferences for keys. However, since reference queues are not
     * used, stale entries are guaranteed to be removed only when
     * the table starts running out of space.
     */
    static class ThreadLocalMap {

        /**
         * The entries in this hash map extend WeakReference, using
         * its main ref field as the key (which is always a
         * ThreadLocal object).  Note that null keys (i.e. entry.get()
         * == null) mean that the key is no longer referenced, so the
         * entry can be expunged from table.  Such entries are referred to
         * as "stale entries" in the code that follows.
         * 散列表
         * 弱引用
         */
        static class Entry extends WeakReference<ThreadLocal<?>> {
            /**
             * The value associated with this ThreadLocal.
             */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        /**
         * The initial capacity -- MUST be a power of two.
         * 散列表长度=16
         */
        private static final int INITIAL_CAPACITY = 16;

        /**
         * The table, resized as necessary.
         * table.length MUST always be a power of two.
         * 散列表引用 数组的长度
         */
        private Entry[] table;

        /**
         * The number of entries in the table.
         * 当前散列表占用的长度
         */
        private int size = 0;

        /**
         * The next size value at which to resize.
         * 扩容触发阈值 len *2/3
         * 调用rehash（）方法
         * 此方法先做一次全量检查过期数据，把散列表中过期的entry移除，如果移除之后散列表之后的长度仍然达到阈值（threshold-threshold/4）
         * 就进行扩容
         */
        private int threshold; // Default to 0

        /**
         * Set the resize threshold to maintain at worst a 2/3 load factor.
         */
        private void setThreshold(int len) {
            threshold = len * 2 / 3;
        }

        /**
         * Increment i modulo len.
         * 获取当前位置的下一个元素
         * 实际形成一个环绕式访问
         */
        private static int nextIndex(int i, int len) {
            return ((i + 1 < len) ? i + 1 : 0);
        }

        /**
         * Decrement i modulo len.
         * 获取当前位置的前一个元素
         * 实际形成一个环绕式访问
         */
        private static int prevIndex(int i, int len) {
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        /**
         * Construct a new map initially containing (firstKey, firstValue).
         * ThreadLocalMaps are constructed lazily, so we only create
         * one when we have at least one entry to put in it.
         */
        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            table = new Entry[INITIAL_CAPACITY];
            // 2的幂次方-1  方便各个位参与运算  均匀分布
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }

        /**
         * Construct a new map including all Inheritable ThreadLocals
         * from given parent map. Called only by createInheritedMap.
         *
         * @param parentMap the map associated with parent thread.
         */
        private ThreadLocalMap(ThreadLocalMap parentMap) {
            Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new Entry[len];

            for (int j = 0; j < len; j++) {
                Entry e = parentTable[j];
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                    if (key != null) {
                        Object value = key.childValue(e.value);
                        Entry c = new Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null)
                            h = nextIndex(h, len);
                        table[h] = c;
                        size++;
                    }
                }
            }
        }

        /**
         * Get the entry associated with key.  This method
         * itself handles only the fast path: a direct hit of existing
         * key. It otherwise relays to getEntryAfterMiss.  This is
         * designed to maximize performance for direct hits, in part
         * by making this method readily inlinable.
         *
         * @param key the thread local object
         * @return the entry associated with key, or null if no such
         */
        private Entry getEntry(ThreadLocal<?> key) {
            // 计算出下标
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key) {
                return e;
            } else {
                // 1. e== null,
                // 2. e.key != key
                // 会继续向当前桶位后面继续查找 e.key==key 的 entry
                //因为 存储时 没有解决哈希冲突；当发生哈希冲突后就会线性的向后找到一个可以使用的 slot ，并且存放进去；
                return getEntryAfterMiss(key, i, e);
            }
        }

        /**
         * Version of getEntry method for use when key is not found in
         * its direct hash slot.
         *
         * @param key the thread local object
         * @param i   the table index for key's hash code
         * @param e   the entry at table[i]
         * @return the entry associated with key, or null if no such
         */
        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                //往下寻找
                ThreadLocal<?> k = e.get();
                //条件成立 返回实例
                if (k == key) {
                    return e;
                }
                //发现当前 key 是空的 ：可能会被GC 回收了；
                if (k == null) {
                    //做一次 探测式过期数据回收
                    expungeStaleEntry(i);
                } else {
                    //更新坐标
                    i = nextIndex(i, len);
                }
                //获取下一个元素
                e = tab[i];
            }
            return null;
        }

        /**
         * Set the value associated with key.
         *
         * @param key   the thread local object
         * @param value the value to be set
         */
        private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);
            //以当前 key 对应的 slot 位置 开始向后查询，找到可以使用的 slot
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                //获取当前元素 key
                ThreadLocal<?> k = e.get();
                //说明是替换操作
                if (k == key) {
                    e.value = value;
                    return;
                }
                //如果是空 说明 key为 null 是过期数据， 走替换逻辑
                if (k == null) {
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }
            // 执行到这里 for 循环遇到了slot 为空的情况，说明是真正的新数据 不用替换
            tab[i] = new Entry(key, value);
            int sz = ++size;
            //做一次启发式清理 ：
            //条件一：!cleanSomeSlots(i, sz)  成立 说明 启发式清理工作 未清理到任何数据
            //条件二：sz >= threshold   当前 table 内的 entry 是否大于阈值
            if (!cleanSomeSlots(i, sz) && sz >= threshold) {
                //
                rehash();
            }
        }

        /**
         * Remove the entry for key.
         */
        private void remove(ThreadLocal<?> key) {
            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                if (e.get() == key) {
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
            }
        }

        /**
         * Replace a stale entry encountered during a set operation
         * with an entry for the specified key.  The value passed in
         * the value parameter is stored in the entry, whether or not
         * an entry already exists for the specified key.
         * <p>
         * As a side effect, this method expunges all stale entries in the
         * "run" containing the stale entry.  (A run is a sequence of entries
         * between two null slots.)
         *
         * @param key       the key
         * @param value     the value to be associated with key
         * @param staleSlot index of the first stale entry encountered while
         *                  searching for key. 替换过期的entry
         */
        private void replaceStaleEntry(ThreadLocal<?> key, Object value, int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;
            Entry e;

            // Back up to check for prior stale entry in current run.
            // We clean out whole runs at a time to avoid continual
            // incremental rehashing due to garbage collector freeing
            // up refs in bunches (i.e., whenever the collector runs).
            //表示 开始探测式清理过期数据 的 开始下标，默认是从当前 staleSlot 开始；
            int slotToExpunge = staleSlot;
            //以当前的索引向前找过期的数据; for 循环一直 碰到 null 结束
            for (int i = prevIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = prevIndex(i, len)) {
                //向前找到了过期数据，更新 探测清理数据下标为 i；prevIndex() 函数 并且还会找到数组的最后一位元素 进行判断
                if (e.get() == null) {
                    //假如 为 0 ，更新为0
                    slotToExpunge = i;
                }
            }

            // Find either the key or trailing null slot of run, whichever
            // occurs first
            //当前的 staleSlot 索引 向后查找，直到碰到 null 为止；
            for (int i = nextIndex(staleSlot, len); (e = tab[i]) != null; i = nextIndex(i, len)) {
                //获取当前元素 K
                ThreadLocal<?> k = e.get();

                // If we find key, then we need to swap it
                // with the stale entry to maintain hash table order.
                // The newly stale slot, or any other stale slot
                // encountered above it, can then be sent to expungeStaleEntry
                // to remove or rehash all of the other entries in run.
                //条件成立：说明是一个 替换逻辑
                if (k == key) {
                    e.value = value;
                    //交换位置 优化
                    //将过期tab[staleSlot]数据 放到tab[i] 的正确位置
                    tab[i] = tab[staleSlot];
                    //再将当前元素 放到 tab[staleSlot]  ，优化完毕
                    tab[staleSlot] = e;

                    // Start expunge at preceding stale entry if it exists
                    //条件成立：1. 说明 replaceStaleEntry 一开始时的向前查找数据国企数据，并未找到过期数据；
                    //                  2. 向后检查过程中也未发现过期数据
                    if (slotToExpunge == staleSlot) {
                        //开始探测清理过期数据的下标 修改为当前的index
                        slotToExpunge = i;
                    }
                    //启发式清理：
                    //
                    cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                    return;
                }

                // If we didn't find stale entry on backward scan, the
                // first stale entry seen while scanning for key is the
                // first still present in the run.
                //条件一：当前遍历的entry是一个过期数据
                //条件二： 成立：一开始的前驱查找结点 并未找到过期数据；
                if (k == null && slotToExpunge == staleSlot) {
                    //因为向后查找过程中 发现一个过期数据了，更新slotToExpunge 为当前位置
                    //前提条件是 前驱查找并未找到过期数据
                    slotToExpunge = i;
                }

            }
            // If key not found, put new entry in stale slot
            //向后查找过程中也并未发现 k==key 的entry 说明是一个新数据，直接添加到
            tab[staleSlot].value = null;
            tab[staleSlot] = new Entry(key, value);

            // If there are any other stale entries in run, expunge them
            //除了当前 staleSlot 以外 还发现了其他过期的 slot  所以要开启清理数据的逻辑
            if (slotToExpunge != staleSlot) {
                //
                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
            }
        }

        /**
         * Expunge a stale entry by rehashing any possibly colliding entries
         * lying between staleSlot and the next null slot.  This also expunges
         * any other stale entries encountered before the trailing null.  See
         * Knuth, Section 6.4
         *
         * @param staleSlot index of slot known to have null key
         * @return the index of the next null slot after staleSlot
         * (all between staleSlot and this slot will have been checked
         * for expunging).
         * 清除
         */
        private int expungeStaleEntry(int staleSlot) {
            //获取基本数据
            Entry[] tab = table;
            int len = tab.length;

            // 清除 expunge entry at staleSlot
            //help gc
            tab[staleSlot].value = null;
            //entry gc
            tab[staleSlot] = null;
            size--;

            // Rehash until we encounter null
            Entry e;
            //当前遍历的 index 下标
            int i;
            //下一个位置开始
            for (i = nextIndex(staleSlot, len);
                //循环条件是：当前的 entry 不为空，直到 entry ==null 结束
                 (e = tab[i]) != null;
                //继续下一个元素
                 i = nextIndex(i, len)) {
                //获取当前 key
                ThreadLocal<?> k = e.get();
                //表示 k (ThreadLocal) 对象 已经被 GC 回收了；当前 entry  属于垃圾数据了
                if (k == null) {
                    //help gc
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else {
                    //执行到这里说明 当前遍历的 slot 中对应得entry  不是非过期数据
                    //尝试开始优化之前的哈希冲突：因为之前可能清除掉几个过期数据  或者 之前存储过程中发生了 hash 冲突 没有放在正确的位置上 应该被优化
                    // 1. 直接获取当前元素的新位置
                    int h = k.threadLocalHashCode & (len - 1);
                    //2. 成立：就是发生了hash冲突 ，向后偏移过，因为并没有在正确的位置上；
                    if (h != i) {
                        //先设置为null
                        tab[i] = null;
                        // Unlike Knuth 6.4 Algorithm R, we must scan until
                        // null because multiple entries could have been stale.
                        //就从正确位置 h 开始找起，因为正确位置 未必是空的
                        while (tab[h] != null) {
                            h = nextIndex(h, len);
                        }
                        //将 当前元素放入到 距离正确位置 更近的位置 （有可能就是正确位置）
                        tab[h] = e;
                    }
                }
            }
            return i;
        }

        /**
         * Heuristically scan some cells looking for stale entries.
         * This is invoked when either a new element is added, or
         * another stale one has been expunged. It performs a
         * logarithmic number of scans, as a balance between no
         * scanning (fast but retains garbage) and a number of scans
         * proportional to number of elements, that would find all
         * garbage but would cause some insertions to take O(n) time.
         *
         * @param i a position known NOT to hold a stale entry. The
         *          scan starts at the element after i.
         * @param n scan control: {@code log2(n)} cells are scanned,
         *          unless a stale entry is found, in which case
         *          {@code log2(table.length)-1} additional cells are scanned.
         *          When called from insertions, this parameter is the number
         *          of elements, but when from replaceStaleEntry, it is the
         *          table length. (Note: all this could be changed to be either
         *          more or less aggressive by weighting n instead of just
         *          using straight log n. But this version is simple, fast, and
         *          seems to work well.)
         * @return true if any stale entries have been removed.
         * i: 开始位置
         * n: 数组大小  也表示结束条件
         */
        private boolean cleanSomeSlots(int i, int n) {
            //启发式清理工作是否清除过过期数据
            boolean removed = false;
            Entry[] tab = table;
            int len = tab.length;

            do {
                //为什么不是从 i 开始的？ 因为expungeStaleEntry()返回的书记一一定为 null
                //当前下标的下一个坐标
                i = nextIndex(i, len);
                Entry e = tab[i];
                //条件一：e != null 成立
                //条件二：e.get() == null 说明当前 slot 保存的是一个过期数据
                if (e != null && e.get() == null) {
                    //重新跟新为 table 数组长度
                    n = len;
                    removed = true;
                    //以当前位置开始的做一次探测式清理工作
                    i = expungeStaleEntry(i);
                }
                //无符号右移一位 赋值给n ;假设为16  会启发式5次
                // 16 >>> 8
                //8 >>>4
                //4>>> 2
                //2>>> 1
                //1>>>0
            } while ((n >>>= 1) != 0);
            return removed;
        }

        /**
         * Re-pack and/or re-size the table. First scan the entire
         * table removing stale entries. If this doesn't sufficiently
         * shrink the size of the table, double the table size.
         */
        private void rehash() {
            //执行完毕后 散列表后 所有过期后的数据 都会被干掉
            expungeStaleEntries();

            // Use lower threshold for doubling to avoid hysteresis
            // 条件成立 说明清理完过期数据后  当前 entry 数量任然需要扩容
            if (size >= threshold - threshold / 4) {
                resize();
            }
        }

        /**
         * Double the capacity of the table.
         */
        private void resize() {
            Entry[] oldTab = table;
            int oldLen = oldTab.length;
            int newLen = oldLen * 2;
            Entry[] newTab = new Entry[newLen];
            //表示新表中的数量
            int count = 0;
            //
            for (int j = 0; j < oldLen; ++j) {
                Entry e = oldTab[j];
                if (e != null) {
                    ThreadLocal<?> k = e.get();
                    //需要gc
                    if (k == null) {
                        e.value = null; // Help the GC
                    } else {
                        //说明是老表的数据是非过期数据 需要迁移到新表
                        //计算出新表的位置
                        int h = k.threadLocalHashCode & (newLen - 1);
                        //可能需要被人占用了
                        while (newTab[h] != null) {
                            h = nextIndex(h, newLen);
                        }
                        //赋值
                        newTab[h] = e;
                        count++;
                    }
                }
            }
            //下次触发扩容的指标
            setThreshold(newLen);
            size = count;
            table = newTab;
        }

        /**
         * Expunge all stale entries in the table.
         */
        private void expungeStaleEntries() {
            Entry[] tab = table;
            int len = tab.length;
            for (int j = 0; j < len; j++) {
                Entry e = tab[j];
                if (e != null && e.get() == null) {
                    expungeStaleEntry(j);
                }
            }
        }
    }
}
