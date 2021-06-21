package com.java.note.rocketmq.six;

import com.java.note.rocketmq.setting.Remote;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  17:14
 * @Description 使用 TransactionMQProducer类创建生产者，并指定唯一的 ProducerGroup，
 * 就可以设置自定义线程池来处理这些检查请求。执行本地事务后、需要根据执行结果对消息队列进行回复。回传的事务状态在请参考前一节。
 */
public class TransactionProducer {

    public static void main(Strings[] args) throws MQClientException, InterruptedException {

        TransactionListener transactionListener = new TransactionListenerImpl();

        TransactionMQProducer producer = new TransactionMQProducer("TransactionProducer_group_name");
        producer.setNamesrvAddr(Remote.IP + ":" + Remote.PORT);

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        producer.setExecutorService(executorService);

        producer.setTransactionListener(transactionListener);

        producer.start();

        Strings[] tags = new Strings[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < Remote.COUNT; i++) {
            try {
                Message msg =
                        new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.sendMessageInTransaction(msg, "hello");

                System.out.printf("%s%n", sendResult);
                Thread.sleep(10);

            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        Thread.sleep(1000);


        producer.shutdown();
    }
}
