package cn.gof.decorator.io;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 13:08
 * @description: 公共装饰器
 * @question:
 * @link:
 **/
public class FilterInputStream extends InputStream {

    protected volatile InputStream in;

    public FilterInputStream(InputStream in) {
        this.in = in;
    }


    @Override
    public int read() {
        in.read();
        return 9;
    }
}
 
