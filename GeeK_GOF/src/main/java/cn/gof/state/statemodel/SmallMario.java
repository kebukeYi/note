package cn.gof.state.statemodel;

import cn.gof.state.mla.State;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 17:34
 * @description:
 * @question:
 * @link:
 **/
public class SmallMario implements IMario {

    //各个状态类为什么要依赖 MarioStateMachine 呢？
    // 这是因为，各个状态类需要更新 MarioStateMachine 中的两个变量，score 和 currentState
    private MarioStateMachine stateMachine;

    private static final SmallMario instance = new SmallMario();
    private SmallMario() {} public static SmallMario getInstance() { return instance; }

    public SmallMario(MarioStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getName() {
        return State.SMALL;
    }

    @Override
    public void obtainMushRoom(MarioStateMachine stateMachine) {
        stateMachine.setCurrentState(new SuperMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 100);
    }

    @Override
    public void obtainCape(MarioStateMachine stateMachine) {

    }

    @Override
    public void obtainFireFlower(MarioStateMachine stateMachine) {

    }

    @Override
    public void meetMonster(MarioStateMachine stateMachine) {

    }
}
 
