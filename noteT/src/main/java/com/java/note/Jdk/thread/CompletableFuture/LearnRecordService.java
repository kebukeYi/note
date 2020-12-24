package com.java.note.Jdk.thread.CompletableFuture;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-24 09:25
 * @Description :
 * @Version :  0.0.1
 */
public class LearnRecordService implements RemoteLoader {
    @Override
    public String load() {
        this.delay();
        return "成绩记录";
    }
}
