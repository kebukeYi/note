package cn.gof.state.player;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 18:06
 * @description:
 * @question:
 * @link:
 **/
public abstract class State {

    Player player;

    /**
     * Context passes itself through the state constructor. This may help a
     * state to fetch some useful context data if needed.
     */
    State(Player player) {
        this.player = player;
    }

    public abstract String onLock();

    public abstract String onPlay();

    public abstract String onNext();

    public abstract String onPrevious();
}
 
