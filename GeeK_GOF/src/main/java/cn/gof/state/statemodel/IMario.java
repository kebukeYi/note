package cn.gof.state.statemodel;

import cn.gof.state.mla.State;

/**
 * @author : kebukeyi
 * @date :  2021-07-21 17:33
 * @description :
 * @question :
 * @usinglink :
 **/
public interface IMario {

    State getName(); //以下是定义的事件

    void obtainMushRoom(MarioStateMachine stateMachine);

    void obtainCape(MarioStateMachine stateMachine);

    void obtainFireFlower(MarioStateMachine stateMachine);

    void meetMonster(MarioStateMachine stateMachine);
}
