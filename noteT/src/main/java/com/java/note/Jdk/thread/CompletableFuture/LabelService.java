package com.java.note.Jdk.thread.CompletableFuture;


public class LabelService implements RemoteLoader {

    @Override
    public Strings load() {
        this.delay();
        return "标签信息";
    }
}
