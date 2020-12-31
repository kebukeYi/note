package com.java.note.Jdk.thread.CompletableFuture;

import com.java.note.Jdk.thread.completableFuture.RemoteLoader;

public class LabelService implements RemoteLoader {

    @Override
    public String load() {
        this.delay();
        return "标签信息";
    }
}
