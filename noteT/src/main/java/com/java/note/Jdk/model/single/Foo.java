package com.java.note.Jdk.model.single;

/**
 * @author : kebukeyi
 * @date :  2021-07-13 17:21
 * @description :
 * @question :
 * @usinglink : http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
 **/
public class Foo {

    /**
     * If perThreadInstance.get() returns a non-null value, this thread
     * has done synchronization needed to see initialization of helper
     */
    private final ThreadLocal perThreadInstance = new ThreadLocal();
    private Helper helper = null;

    public Helper getHelper() {
        if (perThreadInstance.get() == null) createHelper();
        return helper;
    }

    private final void createHelper() {
        synchronized (this) {
            if (helper == null)
                helper = new Helper();
        }
        // Any non-null value would do as the argument here
        perThreadInstance.set(perThreadInstance);
    }

    class Helper {

    }
}
 
