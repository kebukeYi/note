package cn.gof.chain.handler;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 13:00
 * @description:
 * @question:
 * @link:
 **/
public class HandlerB extends Handler {
    @Override
    protected boolean doHandler() {
        System.out.println("HandlerB");
        return false;
    }
}
 
