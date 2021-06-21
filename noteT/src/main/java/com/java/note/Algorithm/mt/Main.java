package com.java.note.Algorithm.mt;


import java.util.*;

public class Main {

    static class Point {
        int x;
        int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int[] dx = {1, 0, 0, -1};
    static int[] dy = {0, -1, 1, 0};
    static final int N = 1000;

    public static void main(Strings[] args) {
        HashMap<Integer, List<Integer>> cow = new HashMap<>();// 行 列list
        HashMap<Integer, List<Integer>> col = new HashMap<>();//列 行list
        List<Integer> cols = null;//每行 的列list
        List<Integer> cows = null;//每列的 行 list
        List<Point> points = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int x = scanner.nextInt();//行
            Strings s = scanner.nextLine().trim();
            int y = Integer.parseInt(s);//列
            points.add(new Point(x, y));//点

            cows = col.get(y);
            if (cows == null) {
                cows = new ArrayList<>();
                cows.add(x);//加入当前行
                col.put(y, cows);//key 为列
            } else {
                cows.add(x);
                col.put(y, cows);
            }

            cols = cow.get(x);
            if (cols == null) {
                cols = new ArrayList<>();
                cols.add(y);
                cow.put(x, cols);
            } else {
                cols.add(y);
                cow.put(x, cols);
            }
        }

        int count = 0;

        for (Point point : points) {//遍历点
            int nx = point.getX();
            int ny = point.getY();
            List<Integer> cowCol = cow.get(nx);//每行的 列数list
            if (cowCol == null) {
                continue;
            }
            for (int c : cowCol) {
                if (c == ny) {//命中列
                    List<Integer> colCow = col.get(ny);//每列的 行list
                    Collections.sort(colCow);//升序
                    if (ny > colCow.get(0) && ny < colCow.get(colCow.size() - 1)) {
                        count++;
                    }
                }
            }
        }

        System.out.println(count);
    }
}
