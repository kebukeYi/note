package cn.gof.oo.Polymorphism;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 10:26
 * @description :
 * @question :
 * @usinglink :
 **/
public class LinkedList implements Iterator {

    private LinkedListNode head;

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public String next() {
        return null;
    }

    @Override
    public String remove() {
        return null;
    }

    class LinkedListNode {

    }
}
 
