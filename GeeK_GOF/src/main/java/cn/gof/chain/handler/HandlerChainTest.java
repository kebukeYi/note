package cn.gof.chain.handler;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 13:03
 * @description:
 * @question:
 * @link:
 **/
public class HandlerChainTest {


    public static void main(String[] args) {
        final HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(new HandlerA());
        handlerChain.addHandler(new HandlerB());
        handlerChain.handle();
    }
}
 
