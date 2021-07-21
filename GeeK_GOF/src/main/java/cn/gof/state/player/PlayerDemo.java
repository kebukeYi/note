package cn.gof.state.player;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 18:09
 * @description: 没咋看懂
 * @question:
 * @link:
 **/
public class PlayerDemo {

    public static void main(String[] args) {
        Player player = new Player();
        UI ui = new UI(player);
        ui.init();
    }

}
 
