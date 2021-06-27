package com.java.note.Algorithm.backsearch;

import lombok.ToString;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author : kebukeyi
 * @date :  2021-05-28 10:44
 * @description : 最短路径算法 -> 通常表示图的方法有两种：第一种是邻接矩阵（也就是二维数组），第二种是邻接表（也就是数组+链表）
 * @question :
 * @usinglink : https://juejin.cn/post/6844904151596400648
 **/
public class Dijkstra {

    @ToString
    static class Edge {
        // 起始定点
        public int s;
        // 终止定点
        public int t;
        // 边的权重
        public int weight;

        Edge(int s, int t, int weight) {
            this.s = s;
            this.t = t;
            this.weight = weight;
        }


    }

    static class Graph {

        // 顶点个数（顶点编号从0开始，在本文例子中，编号为0的顶点不存在）
        private int v;

        // 记录每个顶点的边
        private LinkedList<Edge>[] adj;

        public Graph(int v) {
            this.v = v;
            // 初始化
            this.adj = new LinkedList[v];
            for (int i = 0; i < v; i++) {
                //每个顶点的边
                adj[i] = new LinkedList();
            }
        }

        // 添加一条边，从s到达t
        public void addEdge(int s, int t, int weight) {
            Edge edge = new Edge(s, t, weight);
            adj[s].add(edge);
        }

        // 采用迪杰斯特拉算法找出从s到t的最短路径
        public void dijkstra(int s, int t) {
            // 记录起点 s 到每个顶点的最小距离，数组下标表示顶点编号，值表示最小距离
            int[] dist = new int[v];
            // 记录遍历过的顶点，数组下标表示顶点编号，值表示是否遍历过该顶点
            boolean[] flag = new boolean[v];
            for (int i = 0; i < v; i++) {
                // 初始状态下，将顶点s到其他顶点的距离都设置为无穷大
                dist[i] = Integer.MAX_VALUE;
            }
            // 记录路径，索引表示顶点编号，值表示到达当前顶点的顶点是哪一个 : predecessor[3]=1 表示的是通过顶点 1 到达的顶点 3
            int[] predecessor = new int[v];
            //记录起点
            Queue<Integer> queue = new LinkedList<>();
            //先从 开始点 开始遍历
            queue.add(s);
            // s->s的路径为0
            dist[s] = 0;
            //循环遍历 各个有关系的点
            while (!queue.isEmpty()) {
                Integer current = queue.poll();
                // 已经遍历过该顶点，就不再遍历
                if (flag[current]) continue;
                flag[current] = true;
                //拿出 当前点 的各个边
                int count = adj[current].size();
                for (int i = 0; i < count; i++) {
                    //取出各个边
                    Edge edge = adj[current].get(i);
                    // 如果 s->current + current -> t  <  s -> t
                    // 出现了比当前路径小的方式，就更新为更小路径
                    if (dist[current] + edge.weight < dist[edge.t]) {
                        //更新路径
                        dist[edge.t] = dist[current] + edge.weight;
                        //记录最小路径
                        predecessor[edge.t] = current;
                    }
                    //放入下一个 跟 当前点 有关的点
                    queue.add(edge.t);
                }
            }
            // 打印路径
            System.out.println("最短距离：" + dist[t]);
            System.out.print(s);
            //{0, 0, 1, 1, 3, 3, 4 }
            print(s, t, predecessor);
        }

        // 打印路径
        private void print(int s, int t, int[] predecessor) {
            //s 是 起点  t 是 能到达t的上一个点
            if (s == t) {
                return;
            }
            //
            print(s, predecessor[t], predecessor);
            System.out.print(" -> " + t);
        }
    }


    public static void main(String[] args) {
        // 构建图
        Graph graph = new Graph(7);
        graph.addEdge(1, 2, 60);
        graph.addEdge(1, 3, 10);
        graph.addEdge(1, 5, 50);
        graph.addEdge(2, 4, 35);
        graph.addEdge(3, 4, 30);
        graph.addEdge(3, 5, 25);
        graph.addEdge(4, 6, 15);
        graph.addEdge(5, 2, 30);
        graph.addEdge(5, 6, 105);
        // 计算最短距离
        graph.dijkstra(1, 6);
    }

}
 
