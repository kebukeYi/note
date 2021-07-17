package cn.gof.geek_16;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 17:20
 * @description: 重构之前的代码
 * @question:
 * @link:
 **/
//IdGenerator 设计成了实现类而非接口，调用者直接依赖实现而非接口，违反基于接口而非实现编程的设计思想
public class IdGenerator {

    //这个logger没有未决行为，不影响可测试性。我之前的一篇文章里提到过，将logger对象设置成static的原因是确保日志对象每个类一份、
    // 设置成final的原因是避免日志对象在运行时被修改
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    //定义为静态函数，会影响使用该函数的代码的可测试性
    public static String generate() {
        String id = "";
        try {
            //并未处理“hostName 为空”的情况
            String hostName = InetAddress.getLocalHost().getHostName();
            String[] tokens = hostName.split("\\.");
            if (tokens.length > 0) {
                hostName = tokens[tokens.length - 1];
            }
            char[] randomChars = new char[8];
            int count = 0;
            Random random = new Random();
            while (count < 8) {
                //代码里有很多魔法数，严重影响代码的可读性
                //极端情况下会随机生成很多三段区间之外的无效数字，需要循环很多次才能生成随机字符串
                int randomAscii = random.nextInt(122);
                if (randomAscii >= 48 && randomAscii <= 57) {
                    randomChars[count] = (char) ('0' + (randomAscii - 48));
                    count++;
                } else if (randomAscii >= 65 && randomAscii <= 90) {
                    randomChars[count] = (char) ('A' + (randomAscii - 65));
                    count++;
                } else if (randomAscii >= 97 && randomAscii <= 122) {
                    randomChars[count] = (char) ('a' + (randomAscii - 97));
                    count++;
                }
            }

            id = String.format("%s-%d-%s", hostName, System.currentTimeMillis(), new String(randomChars));
        } catch (UnknownHostException e) {
            logger.warn("Failed to get the host name.", e);
        }
        return id;
    }

    public static void main(String[] args) {
        System.out.println(generate());
    }

}
 
