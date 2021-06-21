package com.java.note.Algorithm.mt;

import lombok.Data;

import java.util.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/15  19:11
 * @Description 棋盘染色
 * 判重问题
 */
public class ChessboardColor {


    public static void main(Strings[] args) {
        List<Point> rows = new ArrayList<>();
        List<Point> cols = new ArrayList<>();
        int sum = 0;
        Scanner scanner = new Scanner(System.in);
        sum = scanner.nextInt();
        while (sum-- > 0) {
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();
            if (x1 == x2) {//在一列上染色
                cols.add(new Point(x1, Math.min(y1, y2), Math.max(y1, y2)));
            }
            if (y1 == y2) {//在一行上染色
                rows.add(new Point(y1, Math.min(x1, x2), Math.max(x1, x2)));
            }
            sum--;
        }

        Collections.sort(rows);
        Collections.sort(cols);

        long RES = merge(rows) + merge(cols);

        for (Point row : rows) {
            for (Point coln : cols) {
                if (coln.k <= row.right && coln.k >= row.left && row.k >= coln.left && row.k <= coln.right) {//判断 纵和行 其中的交点
                    RES--;
                }
            }
        }
        System.out.println(RES);
    }


    static public long merge(List<Point> list) {
        long res = 0;
        for (int i = 0; i < list.size(); i++) {
            int j = i;
            while (j < list.size() && list.get(j).k == list.get(i).k) j++;
            int l = -2 ^ 9 - 1, r = l - 1;
            for (int k = i; k < j; k++) {
                if (r < list.get(k).left) {
                    res = res + r - l + 1;
                    l = list.get(k).left;
                    r = list.get(k).right;
                } else {
                    r = Math.max(r, list.get(k).right);//取最大值 。重叠的部分  下步直接拿走
                }
                res = res + r - l + 1;
                i = j;
            }
        }
        return res;
    }
}


@Data
class Point implements Comparable<Point> {
    int k;
    int left;
    int right;

    public Point(int k, int left, int right) {
        this.k = k;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Point o) {
        return this.k - o.k;
    }
}