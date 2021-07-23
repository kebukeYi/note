package cn.gof.visitor.file;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 16:51
 * @description:
 * @question:
 * @link:
 **/
public class TxtFile extends ResourceFile {
    public TxtFile(String filePath) {
        super(filePath);
    }


    public void accept(Extractor extractor) {
        extractor.extract2txt(this);
    }


    public void accept(Compressor compressor) {
        compressor.extract2txt(this);
    }

    public void accept(Visitor visitor) {
        visitor.visitor(this);
    }
}
 
