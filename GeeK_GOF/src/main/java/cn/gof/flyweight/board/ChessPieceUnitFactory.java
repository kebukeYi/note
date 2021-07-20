package cn.gof.flyweight.board;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 19:12
 * @description:
 * @question:
 * @link:
 **/
public class ChessPieceUnitFactory {

    private static final Map<Integer, ChessPieceUnit> pieces = new HashMap<>();

    static {
        pieces.put(1, new ChessPieceUnit(1, "車", ChessPieceUnit.Color.BLACK));
        pieces.put(2, new ChessPieceUnit(2, "馬", ChessPieceUnit.Color.BLACK));
        pieces.put(2, new ChessPieceUnit(3, "象", ChessPieceUnit.Color.BLACK));
        //...省略摆放其他棋子的代码...
    }

    public static ChessPieceUnit getChessPiece(int chessPieceId) {
        return pieces.get(chessPieceId);
    }
}
 
