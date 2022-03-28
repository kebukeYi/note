package ThreadTwo;

public class ShareData {

    private static ShareData shareData = new ShareData();
    // 不安全的线程共享变量
    private int x = 0;
    private ShareData() {

    }
    public static ShareData getInstantce() {
        return shareData;
    }
    public synchronized void addX() {
        System.out.println(Thread.currentThread().getName() + "in");
        x++;
        System.out.println("加一之后的 x : " + x);
        System.out.println(Thread.currentThread().getName() + "out");
    }
    public synchronized void subX() {
        System.out.println(Thread.currentThread().getName() + "in");
        x--;
        System.out.println("减一之后的 x:" + x);
        System.out.println(Thread.currentThread().getName() + "in");
    }
}
