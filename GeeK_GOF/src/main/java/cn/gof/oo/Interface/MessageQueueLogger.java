package cn.gof.oo.Interface;

import java.util.logging.Level;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 12:44
 * @description :
 * @question :
 * @usinglink :
 **/
public class MessageQueueLogger extends Logger {

    private MessageQueueClient msgQueueClient;

    public MessageQueueLogger(String name, boolean enabled, Level minPermittedLevel, MessageQueueClient msgQueueClient) {
        super(name, enabled, minPermittedLevel);
        this.msgQueueClient = msgQueueClient;
    }


    @Override
    protected void doLog(Level level, String message) {
        // 格式化level和message,输出到消息中间件
        msgQueueClient.send(new Object());
    }
}
 
