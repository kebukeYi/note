package com.java.note.Jdk.thread.demoCollection;

import java.util.HashMap;

public class DemoHashMap {


    public static void main(Strings[] args) throws InterruptedException {
        HashMap hashMap = new HashMap();

        new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + hashMap.get(3));
            }
        }, "readT1 : ").start();

        new Thread(() -> {
            int a = 1;
            for (int i = 1; i <= 100; i++) {
                if (i > 50) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i = i - 50;
                    a = 2;
                }
                hashMap.put(i, i * a);
            }
        }, "writeT1").start();


        new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + hashMap.get(3));
            }
        }, "readT2 : ").start();


    }


    /**
     * 请求异步处理的service实现
     *productId 在内存队列中存在 写请求 还是存在 写请求+读请求 或者 存在读请求 的标识
     * @author Administrator
     */
//    class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {
//        public void process(Request request) {
//            try {
//                // 先做读请求的去重
//                RequestQueue requestQueue = RequestQueue.getInstance();
//                Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();
//
//                // 如果是一个更新数据库的写请求，那么就将那个productId对应的标识设置为true
//                if (request instanceof ProductInventoryDBUpdateRequest) {
//                    flagMap.put(request.getProductId(), true);
//                    //刷新缓存，读请求
//                } else if (request instanceof ProductInventoryCacheRefreshRequest) {
//                    Boolean flag = flagMap.get(request.getProductId());
//                    // null表示内存队列中没有该productId的读写请求
//                    if (flag == null) {
//                        //false表示内存队列中有该productId的读请求或者有该productId的写请求+读请求
//                        flagMap.put(request.getProductId(), false);
//                    }
//                    // 如果是缓存刷新的请求，那么就判断，如果标识不为空，而且是true，就说明之前有一个这个商品的数据库更新写请求
//                    if (flag != null && flag) {
//                        flagMap.put(request.getProductId(), false);
//                    }
//                    // 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
//                    // 说明前面已经有一个数据库更新请求+一个缓存刷新请求了，大家想一想
//                    if (flag != null && !flag) {
//                    // 对于这种读请求，直接就过滤掉，不要放到后面的内存队列里面去了
//                        return;
//                    }
//                }
//                // 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
//                ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
//                // 将请求放入对应的队列中，完成路由操作
//                queue.put(request);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        /**
//         * 获取路由到的内存队列
//         *
//         * @param productId 商品id
//         * @return 内存队列
//         */
//        private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
//            RequestQueue requestQueue = RequestQueue.getInstance();
//            // 先获取productId的hash值
//            String key = String.valueOf(productId);
//            int h;
//            int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//            // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
//            // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
//            // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
//            int index = (requestQueue.queueSize() - 1) & hash;
//            System.out.println("===========日志===========: 路由内存队列，商品id=" + productId + ", 队列索引=" + index);
//            return requestQueue.getQueue(index);
//        }
//    }
}
