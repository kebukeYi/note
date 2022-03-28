package Thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;


public class UserInt {

	static String lua=
			//查询用户是否已经抢过红包,如果抢过 直接返回nil（空）类型
			"if redis.call('hexists',KEYS[3],KEYS[4]) ~= 0 then\n"
		         	+ "return nil\n"+
			"else\n"+
		         	//从红包池中拿取后红包
                  "local redo=redis.call('rpop',KEYS[1]);\n"+
			       "if redo then\n"+//判断红包是否为空
                         "local x=cjson.decode(redo);\n"+//转json
			             "x['userld']=KEYS[4];\n"+//将红包信息与用户ID 绑定 
                         "local re=cjson.encode(x);\n"+
			             "redis.call('hset',KEYS[3],KEYS[4],'1');\n"+//设置用户已经抢过红包
                         "redis.call('lpush',KEYS[2],re);\n"+//红包消耗详情
			             "return re;\n"+//返回详情
                    "end\n"+
			"end\n"+
           "return nil";

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String id;
    volatile static int  size=0;
	
	final static CountDownLatch latch=new CountDownLatch(1);

	public static void getRed() throws InterruptedException {
		for(int i=0;i<20;i++) {
			Thread thread=new Thread( () -> {
                    try {
                        System.out.println("二");
                        latch.await();
                        System.out.println("模拟用户： " + (size++) + " 模拟请求开始  at " + sdf.format(new Date()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            });
            System.out.println("一");
			thread.start();
            System.out.println("三");
            Thread.sleep(200);
            System.out.println("四");
            System.out.println("--------------");

        }
	}
	
		public static void main(String[] args) throws InterruptedException {
			//一共200个红包
           //初始化用20个线程    抢红包20个线程个
			getRed();
            latch.countDown();
				
		}
}
