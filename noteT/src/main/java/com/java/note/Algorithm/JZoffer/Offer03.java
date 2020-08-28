package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/16  12:55
 * @Description 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了
 * ，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
 */
public class Offer03 {
    public static void main(String[] args) {
//        int[] nums = new int[]{2, 3, 5, 0, 2, 5, 5};
        int[] nums = new int[]{2, 3, 5, 0, 1, 4, 6};
        // 5 3 2 0 2 5 5
        System.out.println(findRepeatNumber(nums));
    }

    public static int findRepeatNumber(int[] nums) {
        //HashMap
//        HashMap<Integer, Integer> map = new HashMap<>();
//        for (Integer integer : nums) {
//            if (map.containsKey(integer)) {
//                return integer;
////                int value = map.get(integer);
////                map.put(integer, ++value);
//            } else {
//                map.put(integer, 1);
//            }
//        }
//        List<Map.Entry<Integer, Integer>> asMapList = new ArrayList<>(map.entrySet());
//        for (Map.Entry<Integer, Integer> entry : asMapList) {
//            System.out.println(entry.getKey() + "     " + entry.getValue());
//            if (entry.getValue() > 1) {
//                return entry.getKey();
//            }
//        }
        //HashSet
//        Set<Integer> set = new HashSet<Integer>();
//        for (Integer integer : nums) {
//            int re = -1;
//            if (!set.add(integer)) {
//                re = integer;
//                return re;
//            }
//        }

        //长度为n  [0,n-1]  所以对应的都应该在位置上；排序的时间复杂度：
        //如果没有重复的元素 那么排序后每个元素都应该在 i 上
        int temp;
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i) {
                if (nums[nums[i]] == nums[i]) {
                    return nums[i];
                }

                temp = nums[i];
                nums[i] = nums[temp];
                nums[temp] = temp;
            }
        }
        return -1;
    }

}
