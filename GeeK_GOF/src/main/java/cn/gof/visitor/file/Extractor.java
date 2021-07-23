package cn.gof.visitor.file;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 16:52
 * @description:
 * @question:
 * @link:
 **/
public class Extractor implements Visitor {

    public void extract2txt(TxtFile pptFile) {
        System.out.println("Extract PPT.");
    }

    public void extract2txt(PdfFile pdfFile) {
        System.out.println("Extract PDF.");
    }

    public void extract2txt(WordFile wordFile) {
        System.out.println("Extract WORD.");
    }

    @Override
    public void visitor(PdfFile pdfFile) {
        System.out.println("Extract PPT.");
    }

    @Override
    public void visitor(WordFile pdfFile) {
        System.out.println("Extract PPT.");
    }

    @Override
    public void visitor(TxtFile pdfFile) {
        System.out.println("Extract PPT.");
    }
}
 
