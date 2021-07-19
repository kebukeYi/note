package cn.gof.composite.file;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:19
 * @description: 单独分开  是文件就是文件  是目录就是目录
 * @question:
 * @link:
 **/
public abstract class FileSystemNode {

    protected String path;

    public FileSystemNode(String path) {
        this.path = path;
    }

    public abstract int countNumOfFiles();

    public abstract long countSizeOfFiles();

    public String getPath() {
        return path;
    }
}
 
