package com.java.note.Algorithm.mt;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/17  22:38
 * @Description 如何从 5 亿个数中找出中位数？
 */
public class MedianFinder {
    /*
    方法一：双堆法
维护两个堆，一个大顶堆，一个小顶堆。大顶堆中最大的数小于等于小顶堆中最小的数；保证这两个堆中的元素个数的差不超过 1。
若数据总数为偶数，当这两个堆建好之后，中位数就是这两个堆顶元素的平均值。当数据总数为奇数时，根据两个堆的大小，中位数一定在数据多的堆的堆顶。
    需要把所有数据都加载到内存中。当数据量很大时，就不能这样了，因此，这种方法适用于数据量较小的情况。
 5 亿个数，每个数字占用 4B，总共需要 2G 内存。如果可用内存不足 2G，就不能使用这种方法了，下面介绍另一种方法。
     */

    private PriorityQueue<Integer> maxHeap;
    private PriorityQueue<Integer> minHeap;

    public MedianFinder() {
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        minHeap = new PriorityQueue<>(Integer::compareTo);
    }

    public void addNum(int num) {
        if (maxHeap.isEmpty() || maxHeap.peek() > num) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }
        int size1 = maxHeap.size();
        int size2 = minHeap.size();
        if (size1 - size2 > 1) {
            minHeap.offer(maxHeap.poll());
        } else if (size2 - size1 > 1) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        int size1 = maxHeap.size();
        int size2 = minHeap.size();
        return size1 == size2 ? (maxHeap.peek() + minHeap.peek()) * 1.0 / 2 : (size1 > size2 ? maxHeap.peek() : minHeap.peek());
    }

    /*
    方法二：分治法
     */

}
