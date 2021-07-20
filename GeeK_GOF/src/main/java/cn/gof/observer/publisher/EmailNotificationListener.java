package cn.gof.observer.publisher;

import java.io.File;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:33
 * @description: 收到通知后发送邮件
 * @question:
 * @link:
 **/
public class EmailNotificationListener implements EventListener {

    private String email;

    public EmailNotificationListener(String email) {
        this.email = email;
    }

    @Override
    public void update(String eventType, File file) {
        System.out.println("Email to " + email + ": Someone has performed " + eventType + " operation with the following file: " + file.getName());

    }
}
 
