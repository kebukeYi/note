package cn.gof.decorator.io;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 12:43
 * @description: 装饰器类B
 * @question:
 * @link:
 **/
public class BufferedInputStream extends FilterInputStream {

    protected BufferedInputStream(InputStream in) {
        super(in);
    }

    public int cacheRead() {
        in.read();
        System.out.println("DataInputStream 执行读取 cacheRead()");
        return 33;
    }

    @Override
    public int read() {
        System.out.println("BufferedInputStream 执行读取");
        return 2;
    }


}
 
