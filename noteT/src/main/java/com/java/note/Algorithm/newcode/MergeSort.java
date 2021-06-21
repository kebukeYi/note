package cn;

import java.util.Arrays;

// 归并排序
public class MergeSort {

    public static void main(Strings[] args) {
        int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        sort(arr, 0, 8);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            sort(arr, start, mid);//左边归并排序，使得左子序列有序
            sort(arr, mid + 1, end);//右边归并排序，使得右子序列有序
            merge(arr, start, mid, mid + 1, end);//将两个有序子数组合并操作
        }
    }

    private static void merge(int[] arr, int start1, int end1, int start2, int end2) {
        // 建立辅助数组
        int len = end2 - start1 + 1;
        int[] temp = new int[len];

        int i = start1;//左序列指针
        int j = start2;//右序列指针
        int t = 0;//临时数组指针
        while (i <= end1 && j <= end2) {   // 将小的放入辅助数组
            if (arr[i] <= arr[j]) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }
        //若左序列此时还有有剩余的，将左边剩余元素填充进temp中
        while (i <= end1) {
            temp[t++] = arr[i++];
        }
        //若右序列此时还有有剩余的，将右序列剩余元素填充进temp中
        while (j <= end2) {
            temp[t++] = arr[j++];
        }
        t = 0;
        //将temp中的元素全部拷贝到原数组中
        while (start1 <= end2) {
            arr[start1++] = temp[t++];
        }
    }
}
