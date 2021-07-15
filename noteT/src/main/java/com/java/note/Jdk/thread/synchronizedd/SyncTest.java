package com.java.note.Jdk.thread.synchronizedd;

/**
 * @author : kebukeyi
 * @date :  2021-07-13 11:52
 * @description :
 * @question :
 * @usinglink :
 **/
public class SyncTest {

    public static void main(String[] args) {
        final Foo foo = new Foo("1");
        foo.start();
        final Foo foo1 = new Foo("3");
        foo1.start();
    }

}
 
