package cn.gof.visitor.file;

/**
 * @author : kebukeyi
 * @date :  2021-07-22 17:32
 * @description :
 * @question :
 * @usinglink :
 **/
public interface Visitor {

    void visitor(PdfFile pdfFile);

    void visitor(WordFile pdfFile);

    void visitor(TxtFile pdfFile);

}
