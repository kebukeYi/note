package cn.gof.template.networks;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 19:23
 * @description: 基础社交网络类
 * @question:
 * @link:
 **/
public abstract class Network {
    String userName;
    String password;

    Network() {
    }

    /**
     * Publish the data to whatever network.
     */
    public boolean post(String message) {
        // Authenticate before posting. Every network uses a different
        // authentication method.
        if (logIn(this.userName, this.password)) {
            // Send the post data.
            boolean result = sendData(message.getBytes());
            logOut();
            return result;
        }
        return false;
    }

    abstract boolean logIn(String userName, String password);

    abstract boolean sendData(byte[] data);

    abstract void logOut();
}
 
