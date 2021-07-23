package cn.gof.visitor.file;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 16:51
 * @description:
 * @question:
 * @link:
 **/
public class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }



//    @Override
//    public void accept(Extractor extractor) {
//        extractor.extract2txt(this);
//    }
//
//    @Override
//    public void accept(Compressor compressor) {
//        compressor.extract2txt(this);
//    }

    public void accept(Visitor visitor) {
        visitor.visitor(this);
    }
}
 
