package com.java.note.Jdk.thread.aba;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 12:22
 * @description:
 * @question:
 * @link:
 **/
public class ConcurrentStack {

    AtomicReference<Node> top = new AtomicReference<>();

    public void push(String item) {
        final Node newNode = new Node(item);
        Node oldNode;
        do {
            oldNode = top.get();
            newNode.next = oldNode;
        } while (!top.compareAndSet(oldNode, newNode));
    }

    public void push(Node newNode) {
        Node oldNode;
        do {
            oldNode = top.get();
            newNode.next = oldNode;
        } while (!top.compareAndSet(oldNode, newNode));
    }

    public String pop() {
        Node newNode;
        Node oldNode;
        do {
            oldNode = top.get();
            if (oldNode == null) {
                return null;
            }
            newNode = oldNode.next;
        } while (!top.compareAndSet(oldNode, newNode));
        return oldNode.item;
    }

    public String pop(int time) throws InterruptedException {
        Node newNode;
        Node oldNode;
        do {
            oldNode = top.get();
            if (oldNode == null) {
                return null;
            }
            newNode = oldNode.next;
            TimeUnit.SECONDS.sleep(time);
        } while (!top.compareAndSet(oldNode, newNode));
        return oldNode.item;
    }

    public static void no_ABA() throws InterruptedException {
        final ConcurrentStack concurrentStack = new ConcurrentStack();
        final Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                concurrentStack.push(i + "");
                System.out.println("push: " + i);
            }
        });

        final Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("pop : " + concurrentStack.pop());
            }
        });
        thread.start();
        Thread.sleep(10);
        System.out.println("==============");
        thread1.start();
        thread.join();
        thread1.join();
    }

    public static void yes_ABA() throws InterruptedException {
        ConcurrentStack stack = new ConcurrentStack();
        stack.push(new Node("B"));
        stack.push(new Node("A"));
        System.out.println(stack.top);
        final Thread thread = new Thread(() -> {
            try {
                final String pop = stack.pop(3);
                System.out.println(pop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread thread1 = new Thread(() -> {
            String A = null;
            try {
                A = stack.pop(0);
                stack.pop(0);
                stack.push(new Node("D"));
                stack.push(new Node("C"));
                stack.push(A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread1.start();
        thread.join();
        thread1.join();
        System.out.println(stack.top);
    }

    public static void main(String[] args) throws InterruptedException {
//        no_ABA();
        yes_ABA();
    }
}
 
