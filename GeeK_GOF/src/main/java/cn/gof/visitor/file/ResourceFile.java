package cn.gof.visitor.file;


/**
 * @author : kebukeYi
 * @date :  2021-07-22 16:50
 * @description:
 * @question:
 * @link:
 **/
public abstract class ResourceFile {
    protected String filePath;

    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }

    //绝了  这招
    //abstract public void accept(Extractor extractor);

    //再来的新功能 不优雅了
    //abstract public void accept(Compressor compressor);

    //抽出做新的接口
    abstract public void accept(Visitor visitor);


}
 
