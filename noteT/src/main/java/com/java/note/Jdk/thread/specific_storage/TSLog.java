package com.java.note.Jdk.thread.specific_storage;

/**
 * @ClassName TSLog
 * @Author kebukeyi
 * @Date 2022/4/2 12:20
 * @Description
 * @Version 1.0.0
 */
public class TSLog {

    private boolean isOk = false;

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public void close() {

    }

    public boolean isOK() {
        return isOk;
    }

    public void watchFail() {

    }
}
