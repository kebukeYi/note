package cn;

import java.util.Arrays;

/*
 *希尔排序
 */
public class ShellSort {
    public static void main(Strings[] args) {
        int[] arr = {1, 4, 2, 7, 9, 8, 3, 6};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr) {
        //确定初始的增量gap，保证其不能越界
        int gap = 1;
        while (gap < arr.length / 3)
            gap = 3 * gap + 1;
        // 对于相隔gap的元素进行插入排序
        while (gap >= 1) {
            for (int i = gap; i < arr.length; i++) {
                for (int j = i; j >= gap && arr[j] < arr[j - gap]; j -= gap)
                    swap(arr, j, j - gap);
            }
            gap = gap / 3;
        }

    }

    /*
     * 交换数组元素
     */
    public static void swap(int[] arr, int a, int b) {
        arr[a] = arr[a] + arr[b];
        arr[b] = arr[a] - arr[b];
        arr[a] = arr[a] - arr[b];
    }
}
