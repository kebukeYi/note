package cn.gof.interpreter;

import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 18:29
 * @description:
 * @question:
 * @link:
 **/
public class GreaterExpression implements Expression {
    private String key;
    private long value;

    public GreaterExpression(String key, long value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        if (!stats.containsKey(key)) {
            return false;
        }
        long statValue = stats.get(key);
        return statValue > value;
    }

    public GreaterExpression(String strExpression) {
        //\\s表示   空格,回车,换行等空白符
        String[] elements = strExpression.trim().split("\\s+");
        if (elements.length != 3 || !elements[1].trim().equals(">")) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        this.key = elements[0].trim();
        this.value = Long.parseLong(elements[2].trim());
    }
}
 
