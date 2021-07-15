package cn.gof.oo.Interface;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 12:44
 * @description :
 * @question :
 * @usinglink :
 **/
public class FileLogger extends Logger {

    private Writer fileWriter;

    public FileLogger(String name, boolean enabled, Level minPermittedLevel, String filepath) throws IOException {
        super(name, enabled, minPermittedLevel);
        this.fileWriter = new FileWriter(filepath);
    }

    @Override
    protected void doLog(Level level, String message) throws IOException {
        // 格式化level和message,输出到日志文件
        fileWriter.write(message);
    }
}
 
