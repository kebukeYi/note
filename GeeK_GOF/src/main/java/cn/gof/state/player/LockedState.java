package cn.gof.state.player;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 18:07
 * @description:
 * @question:
 * @link:
 **/
public class LockedState extends State {

    /**
     * Context passes itself through the state constructor. This may help a
     * state to fetch some useful context data if needed.
     *
     * @param player
     */
    LockedState(Player player) {
        super(player);
    }

    @Override
    public String onLock() {
        if (player.isPlaying()) {
            player.changeState(new ReadyState(player));
            return "Stop playing";
        } else {
            return "Locked...";
        }
    }

    @Override
    public String onPlay() {
        player.changeState(new ReadyState(player));
        return "Ready";
    }

    @Override
    public String onNext() {
        return "Locked...";
    }

    @Override
    public String onPrevious() {
        return "Locked...";
    }
}
 
