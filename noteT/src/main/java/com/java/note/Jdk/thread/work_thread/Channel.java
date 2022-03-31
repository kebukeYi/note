package com.java.note.Jdk.thread.work_thread;

/**
 * @ClassName Channel
 * @Author kebukeyi
 * @Date 2022/3/31 14:11
 * @Description
 * @Version 1.0.0
 */
public class Channel {

    private static final int MAX_QUEUE = 100;

    private WorkThread[] workThreads;
    private Request[] requests;
    private int putIndex;
    private int getIndex;
    private int count;

    public Channel(int size) {
        putIndex = 0;
        getIndex = 0;
        count = 0;
        requests = new Request[MAX_QUEUE];
        workThreads = new WorkThread[size];
        for (int i = 0; i < workThreads.length; i++) {
            workThreads[i] = new WorkThread("Worker-" + i, this);
        }
    }

    public void startWorkers() {
        for (WorkThread workThread : workThreads) {
            workThread.start();
        }
    }

    public void stopAllWorkers() {
        for (WorkThread workThread : workThreads) {
            workThread.stopThread();
        }
    }


    public synchronized void putRequest(Request request) {
        while (count >= requests.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        requests[putIndex] = request;
        putIndex = (putIndex + 1) % requests.length;
        count++;
        notifyAll();
    }

    public synchronized Request getRequest() throws InterruptedException {
        while (count <= 0) {
            wait();
        }
        Request request = requests[getIndex];
        getIndex = (getIndex + 1) % requests.length;
        count--;
        notifyAll();
        return request;
    }

}
