package cn.gof.chain.middleaware;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 16:07
 * @description: 检查用户角色
 * @question:
 * @link:
 **/
public class RoleCheckMiddleware extends Middleware {

    public boolean check(String email, String password) {
        if (email.equals("admin@example.com")) {
            System.out.println("Hello, admin!");
            return true;
        }
        System.out.println("Hello, user!");
        return checkNext(email, password);
    }
}
 
