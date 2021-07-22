package cn.gof.iterator.snapshot;

import java.util.Iterator;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 14:32
 * @description: 版本一
 * @question:
 * @link:
 **/
public class SnapshotArrayIterator<E> implements Iterator<E> {

    private int cursor;
    private ArrayList<E> snapshot;

    public SnapshotArrayIterator(int cursor, ArrayList<E> arrayList) {
        this.cursor = 0;
        this.snapshot = new ArrayList<>();
        this.snapshot.addAll(arrayList);
    }

    @Override
    public boolean hasNext() {
        return cursor < snapshot.size();
    }

    @Override
    public E next() {
        E currentItem = snapshot.get(cursor);
        cursor++;
        return currentItem;
    }

    @Override
    public void remove() {

    }
}
 
