package cn.gof.state.doumla;

import static cn.gof.state.doumla.State.*;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 17:10
 * @description:
 * @question:
 * @link:
 **/
public class MarioStateMachine {

    private int score;
    private State currentState;
    private static final State[][] transitionTable = {{SUPER, CAPE, FIRE, SMALL}, {SUPER, CAPE, FIRE, SMALL}, {CAPE, CAPE, CAPE, SMALL}, {FIRE, FIRE, FIRE, SMALL}};
    private static final int[][] actionTable = {{+100, +200, +300, +0}, {+0, +200, +300, -100}, {+0, +0, +0, -200}, {+0, +0, +0, -300}};

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = State.SMALL;
    }


    //如果要执行的动作并非这么简单，而是一系列复杂的逻辑操作（比如加减积分、写数据库，还有可能发送消息通知等等），
    // 我们就没法用如此简单的二维数组来表示了。这也就是说，查表法的实现方式有一定局限性
    public void obtainMushRoom() {
        executeEvent(Event.GOT_MUSHROOM);
    }

    public void obtainCape() {
        executeEvent(Event.GOT_CAPE);
    }

    public void obtainFireFlower() {
        executeEvent(Event.GOT_FIRE);
    }

    public void meetMonster() {
        executeEvent(Event.MET_MONSTER);
    }

    private void executeEvent(Event event) {
        int stateValue = currentState.getValue();
        int eventValue = event.getValue();
        //当前状态 -> 遇见事件 -> 状态变化 以及 分数变化
        this.currentState = transitionTable[stateValue][eventValue];
        this.score += actionTable[stateValue][eventValue];
    }

    public int getScore() {
        return this.score;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
 
