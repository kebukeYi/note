package cn.gof.visitor.file;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 17:04
 * @description:
 * @question:
 * @link:
 **/
public class Compressor implements Visitor {

    public void extract2txt(TxtFile pptFile) {
        System.out.println("Compressor PPT.");
    }

    public void extract2txt(PdfFile pdfFile) {
        System.out.println("Compressor PDF.");
    }

    public void extract2txt(WordFile wordFile) {
        System.out.println("Compressor WORD.");
    }


    @Override
    public void visitor(PdfFile pdfFile) {
        System.out.println("Compressor PPT.");
    }

    @Override
    public void visitor(WordFile pdfFile) {
        System.out.println("Compressor PPT.");
    }

    @Override
    public void visitor(TxtFile pdfFile) {
        System.out.println("Compressor PPT.");
    }
}
 
