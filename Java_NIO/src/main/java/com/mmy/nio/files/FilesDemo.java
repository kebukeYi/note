package com.mmy.nio.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 01:00
 * @description:
 * @question:
 * @link:
 **/
public class FilesDemo {

    public static void main(String[] args) {
        //createDirectory
        Path path = Paths.get("d:\\atguigutest");
        try {
            Path directory = Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //copy
        //这可以让源路径引用的文件被复制到目标路径引用的文件中
        Path path1 = Paths.get("d:\\atguigu\\01.txt");
        Path path2 = Paths.get("d:\\atguigutest\\001.txt");
        try {
            Path copy = Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //move
        Path sourcePath = Paths.get("d:\\atguigu\\01.txt");
        Path destinationPath = Paths.get("d:\\atguigu\\01test.txt");

        try {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            //移动文件失败
            e.printStackTrace();
        }


        //delete
        Path d = Paths.get("d:\\atguigu\\001.txt");
        try {
            Files.delete(path);
        } catch (IOException e) {
            // 删除文件失败
            e.printStackTrace();
        }

        //walkFileTree
        //查找一个名为 002.txt 的文件示例
        Path rootPath = Paths.get("d:\\atguigu");
        String fileToFind = File.separator + "002.txt";
        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileString = file.toAbsolutePath().toString();
                    //System.out.println("pathString = " + fileString);

                    if (fileString.endsWith(fileToFind)) {
                        System.out.println("file found at path: " + file.toAbsolutePath());
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
 
