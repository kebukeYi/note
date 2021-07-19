package cn.gof.decorator.dataSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 13:41
 * @description: 简单数据读写器 最初的业务逻辑类仅能读取和写入纯文本的数据
 * @question:
 * @link:
 **/
public class FileDataSource implements DataSource {

    private String name;

    public FileDataSource(String name) {
        this.name = name;
    }

    @Override
    public void writeData(String data) {
        final File file = new File(name);
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data.getBytes(), 0, data.length());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String readData() {
        char[] buffer = null;
        final File file = new File(name);
        try (final FileReader reader = new FileReader(file)) {
            buffer = new char[(int) file.length()];
            final int read = reader.read(buffer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new String(buffer);
    }
}
 
