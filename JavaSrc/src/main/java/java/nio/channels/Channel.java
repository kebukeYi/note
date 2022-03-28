package java.nio.channels;

import java.io.IOException;
import java.io.Closeable;

/**
 * A nexus for I/O operations.
 * IO 操作的纽带
 * 与缓冲区不同，通道 API 主要由接口指定。不同的操作系统上通道实现（Channel
 * Implementation）会有根本性的差异，所以通道 API 仅仅描述了可以做什么，
 * 通道实现经常使用操作系统的本地代码
 * 通道只能在字节缓冲区上操作
 * <p> A channel represents an open connection to an entity such as a hardware
 * device, a file, a network socket, or a program component that is capable of
 * performing one or more distinct I/O operations, for example reading or
 * writing.
 *
 * <p> A channel is either open or closed.  A channel is open upon creation,
 * and once closed it remains closed.  Once a channel is closed, any attempt to
 * invoke an I/O operation upon it will cause a {@link ClosedChannelException}
 * to be thrown.  Whether or not a channel is open may be tested by invoking
 * its {@link #isOpen isOpen} method.
 *
 * <p> Channels are, in general, intended to be safe for multithreaded access
 * as described in the specifications of the interfaces and classes that extend
 * and implement this interface.
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 */
public interface Channel extends Closeable {

    /**
     * Tells whether or not this channel is open.
     *
     * @return <tt>true</tt> if, and only if, this channel is open
     */
    boolean isOpen();

    /**
     * Closes this channel.
     *
     * <p> After a channel is closed, any further attempt to invoke I/O
     * operations upon it will cause a {@link ClosedChannelException} to be
     * thrown.
     *
     * <p> If this channel is already closed then invoking this method has no effect.
     *
     * <p> This method may be invoked at any time.  If some other thread has
     * already invoked it, however, then another invocation will block until
     * the first invocation is complete, after which it will return without
     * effect. </p>
     * 在一个通道上多次调用close( )方法是没有坏处的，但是如果第一个线程在close( )方法中阻
     * 塞，那么在它完成关闭通道之前，任何其他调用close( )方法都会阻塞
     *
     * @throws IOException If an I/O error occurs
     */
    void close() throws IOException;

}
