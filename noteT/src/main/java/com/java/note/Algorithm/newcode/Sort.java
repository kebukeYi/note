package com.java.note.Algorithm.newcode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/23  18:53
 * @Description
 */
public class Sort {

    // 选择排序
    public int[] selectsort(int[] arr) {
        for (int x = 0; x < arr.length - 1; x++)  //最后一个数不用在自己和自己进行比较了，n-1轮
        {
            for (int y = x + 1; y < arr.length; y++) {
                if (arr[x] > arr[y]) {
                    int temp = arr[x];
                    arr[x] = arr[y];
                    arr[y] = temp;
                }
            }
        }
        return arr;
    }

    // 插入排序
    public int[] insertionSort(int[] arr) {
        int len = arr.length;
        for (int i = 1; i < len; i++) {
            // j表示当前元素的位置，将其和左边的元素比较，若当前元素小，就交换，也就相当于插入
            // 这样当前元素位于j-1处，j--来更新当前元素,j一直左移不能越界，因此应该大于0
            for (int j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
                int temp = arr[j];        // 元素交换
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
            }
        }
        return arr;
    }


    // 冒泡排序
    public int[] bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {        // 相邻元素两两对比
                    int temp = arr[j + 1];        // 元素交换
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }


    /**
     * 快速排序
     */
    public static void quickSort(int[] a, int start, int end) {
        if (start > end) {
            //如果只有一个元素，就不用再排下去了
            return;
        } else {
            //如果不止一个元素，继续划分两边递归排序下去
            int partition = divide(a, start, end);
            quickSort(a, start, partition - 1);
            quickSort(a, partition + 1, end);
        }
    }

    /**
     * 将数组的某一段元素进行划分，小的在左边，大的在右边
     */
    public static int divide(int[] a, int start, int end) {
        //每次都以最右边的元素作为基准值
        int base = a[end];
        //start一旦等于end，就说明左右两个指针合并到了同一位置，可以结束此轮循环。
        while (start < end) {
            while (start < end && a[start] <= base)
                //从左边开始遍历，如果比基准值小，就继续向右走
                start++;
            //上面的while循环结束时，就说明当前的a[start]的值比基准值大，应与基准值进行交换
            if (start < end) {
                //交换
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值右边)，因此右边也要同时向前移动一位
                end--;
            }

            while (start < end && a[end] >= base)
                //从右边开始遍历，如果比基准值大，就继续向左走
                end--;
            //上面的while循环结束时，就说明当前的a[end]的值比基准值小，应与基准值进行交换
            if (start < end) {
                //交换
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值左边)，因此左边也要同时向后移动一位
                start++;
            }

        }
        //这里返回start或者end皆可，此时的start和end都为基准值所在的位置
        return end;
    }


}
