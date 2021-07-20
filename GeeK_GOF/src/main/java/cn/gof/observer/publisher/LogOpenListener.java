package cn.gof.observer.publisher;

import java.io.File;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:34
 * @description: 收到通知后在日志中记录一条消息
 * @question:
 * @link:
 **/
public class LogOpenListener implements EventListener {
    private File log;

    public LogOpenListener(String fileName) {
        this.log = new File(fileName);
    }

    @Override
    public void update(String eventType, File file) {
        System.out.println("Save to log " + log + ": Someone has performed " + eventType + " operation with the following file: " + file.getName());
    }
}
 
