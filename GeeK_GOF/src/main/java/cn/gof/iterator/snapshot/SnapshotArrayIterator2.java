package cn.gof.iterator.snapshot;

import java.util.Iterator;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 14:48
 * @description: 版本二
 * @question:
 * @link:
 **/
public class SnapshotArrayIterator2<E> implements Iterator<E> {

    //迭代器创建时机
    private long snapshotTimestamp;
    // 在整个容器中的下标，而非快照中的下标
    private int cursorInAll;
    // 快照中还有几个元素未被遍历
    private int leftCount;
    //源 list
    private ArrayList<E> arrayList;


    public SnapshotArrayIterator2(ArrayList arrayList) {
        this.snapshotTimestamp = System.currentTimeMillis();
        this.cursorInAll = 0;
        this.leftCount = arrayList.actualSize();
        this.arrayList = arrayList;
        // 先跳到这个迭代器快照的第一个元素
        justNext();
    }

    //仅仅是找到下一个能提供的元素
    private void justNext() {
        while (cursorInAll < arrayList.size()) {
            long addTimestamp = arrayList.getAddTimestamp(cursorInAll);
            long delTimestamp = arrayList.getDelTimestamp(cursorInAll);
            if (snapshotTimestamp > addTimestamp && snapshotTimestamp < delTimestamp) {
                leftCount--;
                break;
            }
            cursorInAll++;
        }

    }

    @Override
    public boolean hasNext() {
        return this.leftCount >= 0;
    }

    @Override
    public E next() {
        E o = arrayList.get(cursorInAll);
        justNext();
        return o;
    }
}
 
