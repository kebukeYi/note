package cn.gof.composite.file;

import java.io.File;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:24
 * @description: 具体文件节点 | 最终的叶子节点
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
        File file = new java.io.File(path);
        if (!file.exists()) return 0;
        return file.length();
    }
}
 
