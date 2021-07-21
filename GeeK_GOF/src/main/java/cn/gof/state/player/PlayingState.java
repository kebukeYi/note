package cn.gof.state.player;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 18:08
 * @description:
 * @question:
 * @link:
 **/
public class PlayingState extends State {
    /**
     * Context passes itself through the state constructor. This may help a
     * state to fetch some useful context data if needed.
     *
     * @param player
     */
    PlayingState(Player player) {
        super(player);
    }

    @Override
    public String onLock() {
        player.changeState(new LockedState(player));
        player.setCurrentTrackAfterStop();
        return "Stop playing";
    }

    @Override
    public String onPlay() {
        player.changeState(new ReadyState(player));
        return "Paused...";
    }

    @Override
    public String onNext() {
        return player.nextTrack();
    }

    @Override
    public String onPrevious() {
        return player.previousTrack();
    }
}
 
