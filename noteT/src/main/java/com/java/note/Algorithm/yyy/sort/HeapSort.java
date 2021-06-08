package com.java.note.Algorithm.yyy.sort;

/**
 * @author : kebukeyi
 * @date :  2021-05-28 13:43
 * @description : 堆排序
 * @question :
 * @usinglink : https://juejin.cn/post/6844904162581282824
 **/
public class HeapSort {

    class Heap {

        private int[] heap; // 存放数据
        private int capacity;   // 堆的最大容量
        private int count;  // 堆中目前存放的数据个数


        public Heap(int capacity) {
            this.heap = new int[capacity + 1];    // 数组下标为0的地方不存放元素（这样计算左右子结点的位置的时候，方便表示）
            this.capacity = capacity;
            this.count = 0;
        }

        public void insert(int value) {
            // 堆满了
            if (count == capacity) return;
            count++;
            heap[count] = value;
            int i = count;
            // 如果子结点的数据比父结点大，就堆化，交换位置
            while (i / 2 > 0 && heap[1 / 2] < heap[i]) {
                // 交换数据
                swap(heap, i, i / 2);
                i = i / 2;
            }
        }

        // 删除最大元素
        public void deleteMax() {
            if (count == 0) return; // 空堆
            heap[1] = heap[count];
            count--;
            heapify(heap, 1, count);   // 从上往下堆化
        }

        // 堆化
        private void heapify(int[] heap, int i, int count) {
            //未越界
            while (2 * i <= count) {
                //保存最大值的索引
                int maxIndex = i;
                // 和左结点比较
                if (heap[i] < heap[2 * i]) maxIndex = 2 * i;
                // 如果右结点更大，则和右结点交换
                if ((2 * i + 1) <= count && heap[2 * i + 1] > heap[2 * i]) maxIndex = 2 * i + 1;
                // 如果左右子结点均小于当前结点，就表示不需要堆化了，直接返回
                if (maxIndex == i) return;
                // 交换数据
                swap(heap, i, maxIndex);
                i = maxIndex;
            }
        }

        // 建堆（从上往下建堆）
        public int[] buildHeap(int[] a) {
            int length = a.length;
            // 数组a的下标为0的地方不存放元素
            // 叶子结点不需要堆化，因为叶子结点下面是没有左右子结点的，无法向下堆化
            for (int i = length / 2; i >= 1; i--) {
                heapify(a, i, length - 1);
            }
            return a;
        }

        // 排序
        public void sort(int[] a) {
            // 先建(大/小)堆
            a = buildHeap(a);
            int i = a.length - 1;
            while (i > 1) {
                // 将第一个数(也就是最大数)放到数组后面
                swap(a, 1, i);
                i--;
                heapify(a, 1, i);  // 堆化
            }
        }


        // 交换下标a1和下标a2的数据
        private void swap(int[] heap, int a1, int a2) {
            // 不借助额外的内存空间，来交换两个数
            heap[a1] = heap[a1] ^ heap[a2];
            heap[a2] = heap[a1] ^ heap[a2];
            heap[a1] = heap[a1] ^ heap[a2];
            /**
             * 上面三行代码等价于这几行代码
             * int tmp = heap[a2];
             * heap[a2] = heap[a1];
             * heap[a1] = tmp
             */
        }

    }

}
 
