package com.java.note.Algorithm.backsearch;

/**
 * @author : kebukeyi
 * @date :  2021-05-28 10:20
 * @description : 八皇后问题 -> 利用回溯思想 -> 递归
 * @question :
 * @usinglink :
 **/
public class EightQueen {

    // 用一个数组存放每一行的皇后的位置，数组的下标表示的是行，元素的值表示的是该行的皇后摆放在哪一列
    public int rowS[] = new int[8];
    public int count = 0;

    /**
     * 往第row行中放入皇后，row最开始从0开始
     *
     * @param row 第几行
     */
    public void putQueen(int row) {
        // 如果等于8表示8个皇后都摆放完了，直接返回即可
        if (row == 8) {
            count++;
            printQueen();
            return;
        }
        // 尝试分别将皇后放在第0-7列中
        for (int column = 0; column < 8; column++) {
            // 在真正将皇后放进棋盘之前，先判断这个位置能不能摆放皇后
            if (isAccessible(row, column)) {
                // 放入皇后
                rowS[row] = column;
                // 在下一行中放入皇后
                putQueen(row + 1);
            }
        }
        return;
    }

    /**
     * 判断第row行，第column列能不能摆放皇后
     * 就是从当前行依次向上遍历，判断前面几行的皇后的攻击范围是不是会覆盖到第 row 行第 column 列
     *
     * @return
     */
    private boolean isAccessible(int row, int column) {
        int left = column - 1;  // 对角线左上
        int right = column + 1; // 对角线右上
        // 从当前行的上一行开始，向上遍历 （没有必要判断当前行的下面几行了，因为下面几行肯定没有放皇后啊）
        for (int i = row - 1; i >= 0; i--) {
            // 当前列上不能有皇后
            if (rowS[i] == column) return false;
            // 左上对角线上不能有皇后 (当前行的上一行的皇后摆在当前行的左上角处于对角线上由此不能放)
            if (left >= 0 && rowS[i] == left) return false;
            // 右上对角线上不能有皇后 (当前行的上一行的皇后摆在当前行的右上角处于对角线上由此不能放)
            if (right < 8 && rowS[i] == right) return false;
            left--;
            right++;
        }
        return true;
    }


    private void printQueen() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (rowS[row] == column) System.out.print("Q ");
                else System.out.print("* ");
            }
            System.out.println();
        }
        System.out.println("=========================");
    }

    public static void main(String[] args) {
        EightQueen eightQueen = new EightQueen();
        eightQueen.putQueen(0);
        System.out.println(eightQueen.count);
    }

}
 
