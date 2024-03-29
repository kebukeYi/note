package com.java.note.Jdk.Collection;

import java.util.*;
import java.util.concurrent.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/6  17:23
 * @Description
 */
public class CollectionDemo {

    public static void main(String[] args) throws InterruptedException {

        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();

        ArrayList<Integer> arrayList = new ArrayList();
        Iterator iterator = arrayList.iterator();

        LinkedList linkedList = new LinkedList();

        List<Integer> synchronizedList = Collections.synchronizedList(linkedList);

        HashSet hashSet = new HashSet();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        TreeSet treeSet = new TreeSet();

        Hashtable hashtable = new Hashtable();


        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap();
        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();

        TreeMap treeMap = new TreeMap();

        Vector vector = new Vector();

        PriorityQueue priorityQueue = new PriorityQueue();
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();


        // setNoSafe();

        removeBatchArrayList();
    }


    /**
     * java.util.ConcurrentModificationException  并发修改异常
     */
    public static void setNoSafe() {
//        HashSet<String> set = new HashSet<>();
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, i + "").start();
        }
    }


    /**
     * java.util.ConcurrentModificationException  并发修改异常 ArrayList(1.2)
     * 出现原因:
     * 解决方案:   Vector(1.0)    Collections.synchronizedList(new ArrayList<>())    new CopyOnWriteArrayList<>()
     * Collection 接口
     * Collections 类
     */
    public static void arrayListDemo() throws InterruptedException {
//        ArrayList<String> arrayList = new ArrayList();
//        List<String> arrayList = Collections.synchronizedList(new ArrayList<>());
//        List<String> arrayList = new Vector<String>();
        List<String> arrayList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                arrayList.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(arrayList);
            }, i + "").start();
        }
    }


    public static void removeBatchArrayList() {
        final ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(4);
        final ArrayList<Object> objects = new ArrayList<>();
        objects.add(1);
        objects.add(3);
        objects.add(4);
        objects.add(5);
        objects.add(2);

        final boolean removeAll = objects.removeAll(integers);
        System.out.println(objects);

        test(String.class);
    }

    public static void likedList() {
        final LinkedList<Object> linkedList = new LinkedList<>();
    }


    public FutureTask getFutureTask() {
        Callable<String> callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return "OK";
            }
        };
        ConcurrentHashMap<String, FutureTask<?>> concurrentHashMap = new ConcurrentHashMap();
        FutureTask<String> futureTask = new FutureTask(callable);
        Future future = concurrentHashMap.putIfAbsent("key", futureTask);
        return futureTask;
    }

    public static void delHashMap() {
        HashMap<String, Object> HashMap = new HashMap();
        HashMap.put("1", "Object");
        HashMap.put("2", "Object");
        HashMap.put("3", "Object");
        HashMap.put("4", "Object");
        HashMap.put("5", "Object");
        HashMap.put("6", "Object");
        HashMap.put("7", "Object");

        try {
            for (Map.Entry<String, Object> entry : HashMap.entrySet()) {
                String key = entry.getKey();
                if (key.equals("3")) {
                    HashMap.remove(entry.getKey());
                }
                System.out.println("1-当前HashMap是" + HashMap + " 当前entry是" + entry);
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
        HashMap.put("3", "Object");
        Iterator<Map.Entry<String, Object>> iterator = HashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getKey().equals("3")) {
                iterator.remove();
            }
            System.out.println("2-当前HashMap是" + HashMap + " 当前entry是" + entry);
        }
    }


    public static <T> T test(T tClass) {

        return tClass;
    }
}
