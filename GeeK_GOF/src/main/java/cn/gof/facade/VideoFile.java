package cn.gof.facade;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 16:09
 * @description:
 * @question:
 * @link:
 **/
public class VideoFile {

    private String name;
    private String codecType;

    public VideoFile(String name) {
        this.name = name;
        this.codecType = name.substring(name.indexOf(".") + 1);
    }

    public String getCodecType() {
        return codecType;
    }

    public String getName() {
        return name;
    }
}
 
