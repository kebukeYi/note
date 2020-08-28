package com.java.note.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  17:10
 * @Description
 * 下⾯的算法中同⼀个任务可能会被多个进程取到之后再使⽤ zrem
 * 进⾏争抢，那些没抢到的进程都是⽩取了⼀次任务，这是浪费。可以
 * 考虑使⽤ Lua scripting 来优化⼀下这个逻辑，将 zrangebyscore
 * 和 zrem ⼀同挪到服务器端进⾏原⼦化操作，这样多个进程之间争
 * 抢任务时就不会出现这种浪费了。
 *
 * 1. Redis 作为消息队列为什么不能保证 100% 的可靠性？
 * 2. 使⽤ Lua Scripting 来优化延时队列的逻辑。
 */
public class RedisDelayingQueue<T> {

    static class TaskItem<T> {
        public String id;
        public T msg;
    }

    // fastjson 序列化对象中存在 generic 类型时，需要使⽤TypeReference
    private Type TaskType = new TypeReference<TaskItem<T>>() {
    }.getType();

    private Jedis jedis;
    private String queueKey;

    public RedisDelayingQueue(Jedis jedis, String queueKey) {
        this.jedis = jedis;
        this.queueKey = queueKey;
    }

    public void delay(T msg) {
        TaskItem<T> task = new TaskItem<T>();
        task.id = UUID.randomUUID().toString(); // 分配唯⼀的 uuid
        task.msg = msg;
        String s = JSON.toJSONString(task); //fastjson 序列化
        jedis.zadd(queueKey, System.currentTimeMillis() + 5000, s); // 塞⼊延时队列, 5 s 后再试
    }

    public void loop() {
        while (!Thread.interrupted()) {
            // 只取⼀条
            Set<String> values = jedis.zrangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
            if (values.isEmpty()) {
                try {
                    Thread.sleep(500); // 歇会继续
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }
            String s = values.iterator().next();
            if (jedis.zrem(queueKey, s) > 0) { // 抢到了
                TaskItem<T> task = JSON.parseObject(s, TaskType); // fastjson 反序列化
                this.handleMsg(task.msg);
            }
        }
    }

    public void handleMsg(T msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        RedisDelayingQueue<String> queue = new RedisDelayingQueue<String>(jedis, "q-demo");

        Thread producer = new Thread() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    queue.delay("codehole" + i);
                }
            }
        };

        Thread consumer = new Thread() {
            public void run() {
                queue.loop();
            }
        };
        producer.start();
        consumer.start();
        try {
            producer.join();
            Thread.sleep(6000);
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
        }
    }

}
