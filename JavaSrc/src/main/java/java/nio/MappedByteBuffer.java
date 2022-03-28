package java.nio;

import java.io.FileDescriptor;

import sun.misc.Unsafe;

/**
 * A direct byte buffer whose content is a memory-mapped region of a file.
 * 当我们为一个文件建立虚拟内存映射之后，文件数据通常不会因此被从磁盘读取到内存（这取
 * 决于操作系统）。该过程类似打开一个文件：文件先被定位，然后一个文件句柄会被创建，当您准
 * 备好之后就可以通过这个句柄来访问文件数据。对于映射缓冲区，虚拟内存系统将根据您的需要来
 * 87
 * 把文件中相应区块的数据读进来。这个页验证或防错过程需要一定的时间，因为将文件数据读取到
 * 内存需要一次或多次的磁盘访问。某些场景下，您可能想先把所有的页都读进内存以实现最小的缓
 * 冲区访问延迟。如果文件的所有页都是常驻内存的，那么它的访问速度就和访问一个基于内存的缓
 * 冲区一样了
 * <p> Mapped byte buffers are created via the {@link
 * java.nio.channels.FileChannel#map FileChannel.map} method.  This class
 * extends the {@link ByteBuffer} class with operations that are specific to
 * memory-mapped file regions.
 *
 * <p> A mapped byte buffer and the file mapping that it represents remain
 * valid until the buffer itself is garbage-collected.
 *
 * <p> The content of a mapped byte buffer can change at any time, for example
 * if the content of the corresponding region of the mapped file is changed by
 * this program or another.  Whether or not such changes occur, and when they
 * occur, is operating-system dependent and therefore unspecified.
 *
 * <a name="inaccess"></a><p> All or part of a mapped byte buffer may become
 * inaccessible at any time, for example if the mapped file is truncated.  An
 * attempt to access an inaccessible region of a mapped byte buffer will not
 * change the buffer's content and will cause an unspecified exception to be
 * thrown either at the time of the access or at some later time.  It is
 * therefore strongly recommended that appropriate precautions be taken to
 * avoid the manipulation of a mapped file by this program, or by a
 * concurrently running program, except to read or write the file's content.
 *
 * <p> Mapped byte buffers otherwise behave no differently than ordinary direct
 * byte buffers. </p>
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 */
public abstract class MappedByteBuffer extends ByteBuffer {

    // This is a little bit backwards: By rights MappedByteBuffer should be a
    // subclass of DirectByteBuffer, but to keep the spec clear and simple, and
    // for optimization purposes, it's easier to do it the other way around.
    // This works because DirectByteBuffer is a package-private class.

    // For mapped buffers, a FileDescriptor that may be used for mapping
    // operations if valid; null if the buffer is not mapped.
    private final FileDescriptor fd;

    // This should only be invoked by the DirectByteBuffer constructors
    //
    MappedByteBuffer(int mark, int pos, int lim, int cap, // package-private
                     FileDescriptor fd) {
        super(mark, pos, lim, cap);
        this.fd = fd;
    }

    MappedByteBuffer(int mark, int pos, int lim, int cap) { // package-private
        super(mark, pos, lim, cap);
        this.fd = null;
    }

    private void checkMapped() {
        if (fd == null)
            // Can only happen if a luser explicitly casts a direct byte buffer
            throw new UnsupportedOperationException();
    }

    // Returns the distance (in bytes) of the buffer from the page aligned address
    // of the mapping. Computed each time to avoid storing in every direct buffer.
    private long mappingOffset() {
        int ps = Bits.pageSize();
        long offset = address % ps;
        return (offset >= 0) ? offset : (ps + offset);
    }

    private long mappingAddress(long mappingOffset) {
        return address - mappingOffset;
    }

    private long mappingLength(long mappingOffset) {
        return (long) capacity() + mappingOffset;
    }

    /**
     * Tells whether or not this buffer's content is resident in physical
     * memory.
     *
     * <p> A return value of <tt>true</tt> implies that it is highly likely
     * that all of the data in this buffer is resident in physical memory and
     * may therefore be accessed without incurring any virtual-memory page
     * faults or I/O operations.  A return value of <tt>false</tt> does not
     * necessarily imply that the buffer's content is not resident in physical
     * memory.
     *
     * <p> The returned value is a hint, rather than a guarantee, because the
     * underlying operating system may have paged out some of the buffer's data
     * by the time that an invocation of this method returns.  </p>
     *
     * @return <tt>true</tt> if it is likely that this buffer's content
     * is resident in physical memory
     */
    public final boolean isLoaded() {
        checkMapped();
        if ((address == 0) || (capacity() == 0))
            return true;
        long offset = mappingOffset();
        long length = mappingLength(offset);
        return isLoaded0(mappingAddress(offset), length, Bits.pageCount(length));
    }

    // not used, but a potential target for a store, see load() for details.
    private static byte unused;

    /**
     * Loads this buffer's content into physical memory.
     * 会加载整个文件以使它常驻内存，相当于 预加载
     * <p> This method makes a best effort to ensure that, when it returns,
     * this buffer's content is resident in physical memory.  Invoking this
     * method may cause some number of page faults and I/O operations to
     * occur. </p>
     *
     * @return This buffer
     */
    public final MappedByteBuffer load() {
        checkMapped();
        if ((address == 0) || (capacity() == 0)) {
            return this;
        }
        long offset = mappingOffset();
        long length = mappingLength(offset);
        load0(mappingAddress(offset), length);

        // Read a byte from each page to bring it into memory.
        // 从每一页读取一个字节以将其放入内存
        // A checksum is computed as we go along to prevent the compiler from otherwise considering the loop as dead code.
        //在我们进行过程中会计算校验和，以防止编译器将循环视为死代码
        Unsafe unsafe = Unsafe.getUnsafe();
        int ps = Bits.pageSize();
        int count = Bits.pageCount(length);
        long a = mappingAddress(offset);
        byte x = 0;
        for (int i = 0; i < count; i++) {
            x ^= unsafe.getByte(a);
            a += ps;
        }
        if (unused != 0) {
            unused = x;
        }
        return this;
    }

    /**
     * Forces any changes made to this buffer's content to be written to the
     * storage device containing the mapped file.
     *
     * <p> If the file mapped into this buffer resides on a local storage
     * device then when this method returns it is guaranteed that all changes
     * made to the buffer since it was created, or since this method was last
     * invoked, will have been written to that device.
     *
     * <p> If the file does not reside on a local device then no such guarantee
     * is made.
     *
     * <p> If this buffer was not mapped in read/write mode ({@link
     * java.nio.channels.FileChannel.MapMode#READ_WRITE}) then invoking this
     * method has no effect. </p>
     *
     * @return This buffer
     */
    public final MappedByteBuffer force() {
        checkMapped();
        if ((address != 0) && (capacity() != 0)) {
            long offset = mappingOffset();
            force0(fd, mappingAddress(offset), mappingLength(offset));
        }
        return this;
    }

    /**
     * 调用 isLoaded( )方法来判断一个被映射的文件是否完全常驻内存了
     * 如果该方法
     * 返回 true 值，那么很大概率是映射缓冲区的访问延迟很少或者根本没有延迟。不过，这也是不能
     * 保证的。同样地，返回 false 值并不一定意味着访问缓冲区将很慢或者该文件并未完全常驻内
     * 存。isLoaded( )方法的返回值只是一个暗示，由于垃圾收集的异步性质、底层操作系统以及运行系
     * 统的动态性等因素，想要在任意时刻准确判断全部映射页的状态是不可能的
     *
     * @param address   缓冲区地址
     * @param length    长度
     * @param pageCount 页数
     * @return boolean
     */
    private native boolean isLoaded0(long address, long length, int pageCount);

    private native void load0(long address, long length);

    /**
     * 该方法会强制将映射缓冲区上的更改应用到永久磁盘存储器上。当用 MappedByteBuffer 对象来更新
     * 一个文件，您应该总是使用 MappedByteBuffer.force( )而非 FileChannel.force( )，因为通道对象可能
     * 不清楚通过映射缓冲区做出的文件的全部更改。MappedByteBuffer 没有不更新文件元数据的选项—
     * —元数据总是会同时被更新的。请注意，非本地文件系统也同样影响 MappedByteBuffer.force( )方
     * 法，正如它会对 FileChannel.force( )方法有影响
     * 如果映射是以 MapMode.READ_ONLY 或 MAP_MODE.PRIVATE 模式建立的，那么调用 force( )
     * 方法将不起任何作用，因为永远不会有更改需要应用到磁盘上（但是这样做也是没有害处的）。
     *
     * @param fd      本地文件描述符
     * @param address 物理地址
     * @param length  长度
     */
    private native void force0(FileDescriptor fd, long address, long length);
}
