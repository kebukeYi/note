package cn.gof.chain.middleaware;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 15:56
 * @description: 基础验证接口
 * @question:
 * @link:
 **/
public abstract class Middleware {

    private Middleware next;


    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }


    public abstract boolean check(String email, String password);

    public boolean checkNext(String email, String password) {
        if (next == null) {
            return true;
        }
        return next.check(email, password);
    }


}
 
