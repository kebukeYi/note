package Thread;

import java.util.concurrent.CountDownLatch;

public class RedInt {

	//发枪器
	final CountDownLatch latch=new CountDownLatch(20);

//	public void RedInit() throws InterruptedException {
//		JedisUtils jedis=new JedisUtils();
//		 JedisPool pool = null;
//		for(int i=0;i<20;i++) {
//			int page=i;
//	     Thread thread=new Thread(){
//	        	public void run() {
//	    		int per=10;//每个线程要处理的 红宝数
//	    		JSONObject object=new JSONObject();
//	    		for(int j=page*per;j<(page+1)*per;j++) {
//	    			object.put("id","rid_"+j);//红包id
//	    			object.put("money", (j+3)*5/2);
//	    			jedis.lpush("RedPoolKey", object.toJSONString());
//	    		}
//	    		latch.countDown();//递减 并且 通知机制
//	    	}
//	    	 };
//			thread.start();
//		}
//			latch.await();//主线程处于等待状态在count为0之前
//
//	}


}
