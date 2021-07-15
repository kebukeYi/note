package cn.gof.auth_14;

import cn.gof.auth_14.util.SHAUtil;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 15:18
 * @description: 负责token的生成  负责token的过期时间检查  负责token的对比 负责拼接url
 * @question:
 * @link:
 **/
public class AuthToken {

    private static final long DEFAULT_EXPIRED_TIME_INTERVAL = 1 * 60 * 1000;
    String token;
    private long createTime;
    private long expiredTimeInterval = DEFAULT_EXPIRED_TIME_INTERVAL;
    public static final String SPLIT = "&";


    public AuthToken(String token, long createTime, long expiredTimeInterval) {
        this(token, createTime);
        this.expiredTimeInterval = expiredTimeInterval;
    }

    public AuthToken(String token, long createTime) {
        this.token = token;
        this.createTime = createTime;
    }


    public static AuthToken create(String originalUrl, String appId, String password, long timestamp) {
        String token = generateTokenString(originalUrl, appId, timestamp, password);
        AuthToken authToken = new AuthToken(token, timestamp);
        return authToken;
    }

    public static String generateTokenString(String originalUrl, String appId, long timestamp, String password) {
        StringBuffer sb = new StringBuffer(originalUrl);
        sb.append(appId);
        sb.append(timestamp);
        sb.append(password);
        return SHAUtil.md5(sb.toString());
    }


    public String getToken() {
        return token;
    }

    public boolean isExpiredTimeInterval() {
        return createTime + expiredTimeInterval < System.currentTimeMillis();

    }

    public boolean match(AuthToken authToken) {
        return this.token.equals(authToken.getToken());
    }


}
 
