package cn.gof.flyweight.tree;

import java.awt.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-20 14:55
 * @description:
 * @question:
 * @link: https://refactoringguru.cn/design-patterns/flyweight/java/example
 **/
public class Tree {
    private int x;
    private int y;
    private TreeType type;

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void draw(Graphics g) {
        type.draw(g, x, y);
    }
}
 
