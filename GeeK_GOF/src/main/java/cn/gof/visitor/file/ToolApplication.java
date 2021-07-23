package cn.gof.visitor.file;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 16:55
 * @description: 访问者模式
 * @question:
 * @link:
 **/
public class ToolApplication {

    public static void main(String[] args) {
        //转换器业务类
        Extractor extractor = new Extractor();
        //不同的处理方法
        List<ResourceFile> resourceFiles = listAllResourceFiles("");
        for (ResourceFile resourceFile : resourceFiles) {
            //编译不通过
            // extractor.extract2txt((PdfFile) resourceFile);
            //每个处理方法 自己引入 转换类
            resourceFile.accept(extractor);
        }
        //不太优雅
        final Compressor compressor = new Compressor();
        for (ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(compressor);
        }
    }

    private static List listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //根据后缀(pdf / ppt / word) 由工厂方法创建不同的类对象(PdfFile / PPTFile / WordFile)
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new TxtFile("c.ppt"));
        return resourceFiles;
    }
}
 
