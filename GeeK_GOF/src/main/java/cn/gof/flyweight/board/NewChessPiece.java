package cn.gof.flyweight.board;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 19:16
 * @description:
 * @question:
 * @link:
 **/
public class NewChessPiece {
    private ChessPieceUnit chessPieceUnit;
    private int positionX;
    private int positionY;

    public NewChessPiece(ChessPieceUnit unit, int positionX, int positionY) {
        this.chessPieceUnit = unit;
        this.positionX = positionX;
        this.positionY = positionY;
    } // 省略getter、setter方法


}
 
