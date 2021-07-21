package cn.gof.state.statemodel;

import cn.gof.state.mla.State;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 17:35
 * @description:
 * @question:
 * @link:
 **/
public class MarioStateMachine {

    private int score;
    //MarioStateMachine 依赖各个状态类是理所当然的
    private IMario currentState; // 不再使用枚举来表示状态

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = new SmallMario(this);
    }

    public void obtainMushRoom() {
        this.currentState.obtainMushRoom(this);
    }

    public void obtainCape() {
        this.currentState.obtainCape(this);
    }

    public void obtainFireFlower() {
        this.currentState.obtainFireFlower(this);
    }

    public void meetMonster() {
        this.currentState.meetMonster(this);
    }

    public int getScore() {
        return this.score;
    }

    public State getCurrentState() {
        return this.currentState.getName();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCurrentState(IMario currentState) {
        this.currentState = currentState;
    }
}

 
