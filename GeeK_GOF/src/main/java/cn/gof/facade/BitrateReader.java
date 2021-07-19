package cn.gof.facade;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 16:11
 * @description:
 * @question:
 * @link:
 **/
public class BitrateReader {

    public static VideoFile read(VideoFile file, Codec codec) {
        System.out.println("BitrateReader: reading file...");
        return file;
    }

    public static VideoFile convert(VideoFile buffer, Codec codec) {
        System.out.println("BitrateReader: writing file...");
        return buffer;
    }

}
 
