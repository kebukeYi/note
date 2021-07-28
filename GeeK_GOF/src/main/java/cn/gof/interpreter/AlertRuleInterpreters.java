package cn.gof.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 18:19
 * @description: 如果每分钟接口出错数超过 100，监控系统就通过短信、微信、邮件等方式发送告警给开发者
 * @question:
 * @link:
 **/
public class AlertRuleInterpreters {

    public AlertRuleInterpreters(String ruleExpression) {
        //TODO:由你来完善
    }

    // apiStat = new HashMap<>();
    // apiStat.put("key1", 103);
    // apiStat.put("key2", 987);
    public boolean interpret(Map stats) {
        //TODO:由你来完善
        return false;
    }
}

class DemoTest {

    public static void main(String[] args) {
        //可以自定义规则
        String rule = "key1 > 100 && key2 < 30 || key3 < 100 || key4 == 88";
        AlertRuleInterpreter interpreter = new AlertRuleInterpreter(rule);
        //提供各个参数
        Map stats = new HashMap<>();
        stats.put("key1", 101l);
        stats.put("key3", 121l);
        stats.put("key4", 88l);
        boolean alert = interpreter.interpret(stats);
        System.out.println(alert);
    }
}

 
