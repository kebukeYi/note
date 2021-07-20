package cn.gof.flyweight.board;

import lombok.Data;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 18:42
 * @description:
 * @question:
 * @link:
 **/
@Data
public class ChessPiece {

    private int id;
    private String text;
    private Color color;
    private int positionX;
    private int positionY;

    public ChessPiece(int id, String text, Color color, int positionX, int positionY) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionX;
    }

    public static enum Color {RED, BLACK}


}
 
