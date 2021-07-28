package cn.gof.interpreter;

import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 18:36
 * @description:
 * @question:
 * @link:
 **/
public class AlertRuleInterpreter {

    private Expression expression;

    public AlertRuleInterpreter(String ruleExpression) {
        this.expression = new OrExpression(ruleExpression);
    }

    public boolean interpret(Map<String, Long> stats) {
        return expression.interpret(stats);
    }
}
 
