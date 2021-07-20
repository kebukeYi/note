package cn.gof.flyweight.board;

import lombok.Data;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 19:12
 * @description: 抽出的享元类
 * @question:
 * @link:
 **/
@Data
public class ChessPieceUnit {
    private int id;
    private String text;
    private Color color;

    public ChessPieceUnit(int id, String text, Color color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public static enum Color {RED, BLACK} // ...省略其他属性和getter方法...
}
 
