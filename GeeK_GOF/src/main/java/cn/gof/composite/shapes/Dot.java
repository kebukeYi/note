package cn.gof.composite.shapes;

import java.awt.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 18:09
 * @description: 点儿
 * @question:
 * @link:
 **/
public class Dot extends BaseShape {

    private final int DOT_SIZE = 3;

    Dot(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public int getWidth() {
        return DOT_SIZE;
    }

    @Override
    public int getHeight() {
        return DOT_SIZE;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.fillRect(x - 1, y - 1, getWidth(), getHeight());
    }
}
 
