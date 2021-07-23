package cn.gof.command.mands;

import java.util.Stack;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 17:43
 * @description:
 * @question:
 * @link:
 **/
public class CommandHistory {

    private Stack<Command> history = new Stack<>();

    public void push(Command c) {
        history.push(c);
    }

    public Command pop() {
        return history.pop();
    }

    public boolean isEmpty() { return history.isEmpty(); }
}
 
