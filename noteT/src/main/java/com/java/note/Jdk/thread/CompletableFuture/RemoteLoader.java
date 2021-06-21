package com.java.note.Jdk.thread.CompletableFuture;

/**
 * @author fang.com
 */
public interface RemoteLoader {

    Strings load();

    default void delay() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
