package cn.gof.interpreter;

import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-07-23 18:26
 * @description :
 * @question :
 * @usinglink :
 **/
public interface Expression {
    boolean interpret(Map<String, Long> stats);
}
