package cn.gof.state.doumla;

/**
 * @author : kebukeyi
 * @date :  2021-07-21 16:52
 * @description :
 * @question :
 * @usinglink :
 **/
public enum State {

    SMALL(0), SUPER(1), FIRE(2), CAPE(3);

    private int value;

    private State(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
