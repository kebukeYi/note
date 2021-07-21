package cn.gof.chain.handler;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 12:58
 * @description:
 * @question:
 * @link:
 **/
public abstract class Handler {

    protected Handler next = null;

    public void setNext(Handler next) {
        this.next = next;
    }

    //抽离出公共逻辑
    public final void handler() {
        //自己处理不了了 扔给下一个处理
//        if (!doHandler() && next != null) {
//            next.handler();
//        }
        //不管 链上的直接全部处理
        doHandler();
        if (next != null) {
            next.handler();
        }
    }

    //子类只关注业务逻辑
    protected abstract boolean doHandler();


}
 
