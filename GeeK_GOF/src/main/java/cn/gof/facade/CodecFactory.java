package cn.gof.facade;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 16:10
 * @description:
 * @question:
 * @link:
 **/
public class CodecFactory {

    public static Codec extract(VideoFile file) {
        String type = file.getCodecType();
        if (type.equals("mp4")) {
            System.out.println("CodecFactory: extracting mpeg audio...");
            return new MPEG4CompressionCodec();
        } else {
            System.out.println("CodecFactory: extracting ogg audio...");
            return new OggCompressionCodec();
        }
    }

}
 
