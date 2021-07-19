package cn.gof.decorator.io;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 12:44
 * @description: 装饰器类A
 * @question:
 * @link:
 **/
public class DataInputStream extends FilterInputStream {

    protected DataInputStream(InputStream in) {
        super(in);
    }

    //实现自己特有的 独立方法
    public int dataRead() {
        in.read();
        System.out.println("DataInputStream 执行读取 dataRead()");
        return 22;
    }


    //如果不重写的话 会默认调取 InputStream.read() 方法 而不是调用 传进来的 具体 InputStream 实现类的 read 方法
//    @Override
//    int read() {
//        in.read();
//        System.out.println("DataInputStream 执行读取");
//        return 3;
//    }


}
 
