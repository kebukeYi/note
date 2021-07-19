package cn.gof.facade;

import java.io.File;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 16:13
 * @description:
 * @question:
 * @link:
 **/
public class Main {

    public static void main(String[] args) {
        VideoConversionFacade converter = new VideoConversionFacade();
        File mp4Video = converter.convertVideo("youtubevideo.ogg", "mp4");
    }

}
 
