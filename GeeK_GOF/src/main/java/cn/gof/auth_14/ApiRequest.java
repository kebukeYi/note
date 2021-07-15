package cn.gof.auth_14;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 14:40
 * @description: 将 token、AppID、时间戳拼接到 URL 中，形成新的 URL； 解析 URL，得到 token、AppID、时间戳等信息
 * @question:
 * @link:
 **/
public class ApiRequest {

    String baseUrl;
    String appId;
    String token;
    private long timestamp;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getToken() {
        return token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ApiRequest() {
    }

    public ApiRequest(String baseUrl, String appId, String token, long timestamp) {
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.token = token;
        this.timestamp = timestamp;
    }

    /**
     * 解析url
     *
     * @param url
     * @return ApiRequest
     */
    //https://time.geekbang.org/column/article/171767?appId=1001&timestamp=1307788865&token=a78cdef998
    public static ApiRequest buildFromUrl(String url) {
        final String[] split = url.split("\\?");
        if (split.length == 1) {
            return null;
        }
        String baseUrl = split[0];
        final String[] strings = split[1].split("&");
        final String appId = strings[0].split("=")[1];
        long timestamp = Long.parseLong(strings[1].split("=")[1]);
        final String token = strings[2].split("=")[1];
        return new ApiRequest(baseUrl, appId, token, timestamp);
    }


    @Override
    public String toString() {
        return "ApiRequest{" +
                "baseUrl='" + baseUrl + '\'' +
                ", appId='" + appId + '\'' +
                ", token='" + token + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }


    //测试
    public static void main(String[] args) {
        final ApiRequest request = new ApiRequest();
        String url = "https://time.geekbang.org/column/article/171767?appId=1001&timestamp=1307788865&token=a78cdef998";
        System.out.println(request.buildFromUrl(url));
    }
}
 
