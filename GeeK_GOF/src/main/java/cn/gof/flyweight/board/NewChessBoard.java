package cn.gof.flyweight.board;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 19:16
 * @description:
 * @question:
 * @link:
 **/
public class NewChessBoard {

    private Map<Integer, NewChessPiece> chessPieces = new HashMap<>();

    public NewChessBoard() {
        init();
    }

    //通过工厂类获取到的 ChessPieceUnit 就是享元。所有的 ChessBoard 对象共享这 30 个 ChessPieceUnit 对象（因为象棋中只有 30 个棋子）
    private void init() {
        chessPieces.put(1, new NewChessPiece(ChessPieceUnitFactory.getChessPiece(1), 0, 0));
        chessPieces.put(1, new NewChessPiece(ChessPieceUnitFactory.getChessPiece(2), 1, 0));
        //...省略摆放其他棋子的代码... } public void move(int chessPieceId, int toPositionX, int toPositionY) { //...省略... }
    }
}
