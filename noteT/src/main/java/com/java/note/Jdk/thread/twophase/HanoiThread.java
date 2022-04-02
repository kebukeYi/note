package com.java.note.Jdk.thread.twophase;

/**
 * @ClassName HanoiThread
 * @Author kebukeyi
 * @Date 2022/3/31 19:15
 * @Description http://c.biancheng.net/view/604.html
 * @Version 1.0.0
 */
public class HanoiThread extends Thread {

    private int count = 0;
    private volatile boolean shutDownRequest = false;

    public void shutDownRequest() {
        shutDownRequest = true;
        interrupt();
    }

    public boolean isShutDownRequest() {
        return shutDownRequest;
    }

    @Override
    public void run() {
        try {
            for (int n = 0; !isShutDownRequest(); n++) {
                System.out.println(" ===  level  " + n + "  ===  ");
                //将塔座X上编号为1至n的n个圆盘按规则搬到塔座Z上， Y可用作辅助塔座
                doWork(n, "A", "B", "C");
                Thread.sleep(500);
                System.out.println("");
            }
        } catch (InterruptedException e) {
        } finally {
            doShutDown();
        }
    }

    private void doWork(int level, String a, String b, String c) throws InterruptedException {
        if (level > 0) {
            if (isShutDownRequest()) {
                throw new InterruptedException();
            }
            //将X上编号为1至n-1的圆盘移到Y,Z作辅助塔
            doWork(level - 1, a, c, b);
            //将编号为n的圆盘从X移到Z
            System.out.print(a + " -> " + c + "  ");
            //将Y上编号为1至n-1的圆盘移动到Z， X作辅助塔
            doWork(level - 1, b, a, c);
        }
    }

    public void doShutDown() {
        System.out.println("doShutDown：counter：" + count);
    }


}
