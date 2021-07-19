package cn.gof.composite.shapes;

import java.awt.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 18:07
 * @description: 通用形状接口
 * @question :
 * @usinglink :
 **/
public interface Shape {
    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void move(int x, int y);

    boolean isInsideBounds(int x, int y);

    void select();

    void unSelect();

    boolean isSelected();

    void paint(Graphics graphics);
}
