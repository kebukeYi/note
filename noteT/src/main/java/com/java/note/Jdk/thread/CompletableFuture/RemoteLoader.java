package com.java.note.Jdk.thread.CompletableFuture;

public interface RemoteLoader {

    String load();

    default void delay() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
