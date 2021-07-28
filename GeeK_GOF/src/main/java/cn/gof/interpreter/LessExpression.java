package cn.gof.interpreter;

import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 18:37
 * @description:
 * @question:
 * @link:
 **/
public class LessExpression implements Expression {

    private String key;
    private long value;

    public LessExpression(String key, long value) {
        this.key = key;
        this.value = value;
    }

    public LessExpression(String strExpression) {
        //\\s表示   空格,回车,换行等空白符
        String[] elements = strExpression.trim().split("\\s+");
        if (elements.length != 3 || !elements[1].trim().equals("<")) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        this.key = elements[0].trim();
        this.value = Long.parseLong(elements[2].trim());
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        return false;
    }
}
 
