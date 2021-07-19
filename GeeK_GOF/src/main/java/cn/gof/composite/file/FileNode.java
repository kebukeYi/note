package cn.gof.composite.file;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:24
 * @description:
 * @question:
 * @link:
 **/
public class FileNode extends FileSystemNode {

    public FileNode(String path) {
        super(path);
    }

    @Override
    public int countNumOfFiles() {
        return 1;
    }

    @Override
    public long countSizeOfFiles() {
        java.io.File file = new java.io.File(path);
        if (!file.exists()) return 0;
        return file.length();
    }
}
 
