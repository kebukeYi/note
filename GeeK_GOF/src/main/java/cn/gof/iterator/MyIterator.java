package cn.gof.iterator;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 13:57
 * @description: 简易迭代器
 * @question:
 * @link:
 **/
public class MyIterator {

    private ArrayList arrayList;
    private int cursor;

    public MyIterator(ArrayList arrayList) {
        this.arrayList = arrayList;
        this.cursor = 0;
    }

    public boolean haveNext() {
        return cursor < arrayList.size();
    }

    public Object next() {
        if (cursor > arrayList.size()) return null;
        cursor++;
        return arrayList.get(cursor);
    }


    public Object currentItem() {
        if (cursor >= arrayList.size()) {
            throw new NoSuchElementException();
        }
        return arrayList.get(cursor);
    }

}
 
