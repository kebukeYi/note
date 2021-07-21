package cn.gof.state.statemodel;

import cn.gof.state.mla.State;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 17:59
 * @description: 做一个中间状态
 * @question:
 * @link:
 **/
public abstract class AbstractIMario implements IMario {

    @Override
    public State getName() {
        return null;
    }

    @Override
    public void obtainMushRoom(MarioStateMachine stateMachine) {

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
 
