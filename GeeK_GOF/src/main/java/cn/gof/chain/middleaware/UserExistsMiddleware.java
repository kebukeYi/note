package cn.gof.chain.middleaware;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 16:06
 * @description: 检查用户登录信息
 * @question:
 * @link:
 **/
public class UserExistsMiddleware extends Middleware {

    private Server server;

    public UserExistsMiddleware(Server server) {
        this.server = server;
    }

    @Override
    public boolean check(String email, String password) {
        if (!server.hasEmail(email)) {
            System.out.println("This email is not registered!");
            return false;
        }
        if (!server.isValidPassword(email, password)) {
            System.out.println("Wrong password!");
            return false;
        }
        return checkNext(email, password);
    }
}
 
