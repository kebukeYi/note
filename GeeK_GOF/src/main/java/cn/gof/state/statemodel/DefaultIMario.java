package cn.gof.state.statemodel;

import cn.gof.state.mla.State;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 18:00
 * @description:  自定义实现需要实现的方法
 * @question:
 * @link:
 **/
public class DefaultIMario extends AbstractIMario {

    @Override
    public State getName() {
        return super.getName();
    }

    @Override
    public void obtainMushRoom(MarioStateMachine stateMachine) {
        super.obtainMushRoom(stateMachine);
    }

    @Override
    public void obtainCape(MarioStateMachine stateMachine) {
        super.obtainCape(stateMachine);
    }
}
 
