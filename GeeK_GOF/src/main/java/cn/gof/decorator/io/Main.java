package cn.gof.decorator.io;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 12:48
 * @description:
 * @question:
 * @link:
 **/
public class Main {
    public static void main(String[] args) {
        final FileInputStream fin = new FileInputStream();
        final BufferedInputStream bin = new BufferedInputStream(fin);
        DataInputStream din = new DataInputStream(bin);
        din.dataRead();

    }
}
 
