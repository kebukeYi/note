package cn.gof.state.doumla;

/**
 * @author : kebukeyi
 * @date :  2021-07-21 17:09
 * @description :
 * @question :
 * @usinglink :
 **/
public enum Event {

    GOT_MUSHROOM(0), GOT_CAPE(1), GOT_FIRE(2), MET_MONSTER(3),
    ;
    private int value;

    private Event(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
