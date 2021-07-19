package cn.gof.composite.shapes;

import java.awt.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 18:13
 * @description: 还是算了吧
 * @question:
 * @link: https://refactoringguru.cn/design-patterns/composite/java/example
 **/
public class ShapeDemo {

    public static void main(String[] args) {
        ImageEditor editor = new ImageEditor();

        editor.loadShapes(
                new Circle(10, 10, 10, Color.BLUE),

                new CompoundShape(
                        new Circle(110, 110, 50, Color.RED),
                        new Dot(160, 160, Color.RED)
                ),

                new CompoundShape(
                        new Rectangle(250, 250, 100, 100, Color.GREEN),
                        new Dot(240, 240, Color.GREEN),
                        new Dot(240, 360, Color.GREEN),
                        new Dot(360, 360, Color.GREEN),
                        new Dot(360, 240, Color.GREEN)
                )
        );
    }
}
 
