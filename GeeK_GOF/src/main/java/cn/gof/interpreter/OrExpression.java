package cn.gof.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 18:35
 * @description:
 * @question:
 * @link:
 **/
public class OrExpression implements Expression {

    private List<Expression> expressions = new ArrayList<>();

    public OrExpression(String strOrExpression) {
        String[] andExpressions = strOrExpression.split("\\|\\|");
        for (String andExpr : andExpressions) {
            expressions.add(new AndExpression(andExpr));
        }
    }

    public OrExpression(List expressions) {
        this.expressions.addAll(expressions);
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        for (Expression expr : expressions) {
            if (expr.interpret(stats)) {
                return true;
            }
        }
        return false;
    }
}
 
