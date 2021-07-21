package cn.gof.chain.handler;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 12:59
 * @description:
 * @question:
 * @link:
 **/
public class HandlerA extends Handler {

    @Override
    protected boolean doHandler() {
        System.out.println("HandlerA");
        return false;
    }
}
 
