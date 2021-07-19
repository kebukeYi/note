package cn.gof.decorator.io;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 12:42
 * @description:
 * @question:
 * @link:
 **/
public class FileInputStream extends InputStream {

    @Override
    int read() {
        System.out.println("FileInputStream 执行读取");
        return 1;
    }
}
 
