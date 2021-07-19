package cn.gof.composite.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:04
 * @description: 还未用到 组合模式
 * @question:
 * @link: https://time.geekbang.org/column/article/207456
 **/
public class FileSimpleSystemNode {

    private String path;
    boolean isFile;
    List<FileSimpleSystemNode> subNodes = new ArrayList<>();


    public FileSimpleSystemNode(String path, boolean isFile) {
        this.isFile = isFile;
        this.path = path;
    }


    // 统计指定目录下的文件个数
    public int countNumOfFiles() {
        if (isFile) return 1;
        int numOfFiles = 0;
        for (FileSimpleSystemNode fileOrDir : subNodes) {
            numOfFiles += fileOrDir.countNumOfFiles();
        }
        return numOfFiles;
    }

    //统计指定目录下的文件总大小
    public long countSizeOfFiles() {
        if (isFile) {
            final File file = new File(path);
            if (!file.exists()) return 0;
            return file.length();
        }
        long sizeofFiles = 0;
        for (FileSimpleSystemNode subNode : subNodes) {
            sizeofFiles += subNode.countSizeOfFiles();
        }
        return sizeofFiles;
    }

    public String getPath() {
        return path;
    }

    //动态地添加、删除某个目录下的子目录或文件
    public void addSubNode(FileSimpleSystemNode fileOrDir) {
        subNodes.add(fileOrDir);
    }

    //动态地添加、删除某个目录下的子目录或文件
    public void removeSubNode(FileSimpleSystemNode fileOrDir) {
        int size = subNodes.size();
        int i = 0;
        for (; i < size; ++i) {
            if (subNodes.get(i).getPath().equals(fileOrDir.getPath())) {
                break;
            }
        }
        if (i < size) {
            subNodes.remove(i);
        }
    }


}
 
