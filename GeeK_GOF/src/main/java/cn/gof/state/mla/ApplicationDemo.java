package cn.gof.state.mla;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 16:53
 * @description:
 * @question:
 * @link:
 **/
public class ApplicationDemo {

    public static void main(String[] args) {
        MarioStateMachine mario = new MarioStateMachine();
        mario.obtainMushRoom();
        int score = mario.getScore();
        State state = mario.getCurrentState();
        System.out.println("mario score: " + score + "; state: " + state);
    }
}
 
