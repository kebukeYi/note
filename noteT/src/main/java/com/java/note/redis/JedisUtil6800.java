package com.java.note.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/1/19  13:45
 * @Description
 */
public class JedisUtil6800 {

    protected static Logger logger = LogManager.getLogger(JedisUtil6800.class);

    //Redis服务器IP
    private static Strings ADDR_ARRAY = "127.0.0.1";

    //Redis的端口号
    private static int PORT = 6491;

    //访问密码
    //private static String AUTH = "";
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 100;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 50;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 3000;

    //超时时间
    private static int TIMEOUT = 2000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = false;

    private static JedisPool jedisPool = initialPool();

    /**
     * redis过期时间,以秒为单位
     */
    public final static int EXRP_HOUR = 60 * 60;                         //一小时
    public final static int EXRP_DAY = 60 * 60 * 24;                    //一天
    public final static int EXRP_MONTH = 60 * 60 * 24 * 30;     //一个月

    /**
     * 初始化Redis连接池
     */
    private static JedisPool initialPool() {
        JedisPool jedisPool = null;
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[0], PORT, TIMEOUT);

        } catch (Exception e) {
            logger.error("First create JedisPool error : " + e);
            try {
                //如果第一个IP异常，则访问第二个IP
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(MAX_ACTIVE);
                config.setMaxIdle(MAX_IDLE);
                config.setMaxWaitMillis(MAX_WAIT);
                config.setTestOnBorrow(TEST_ON_BORROW);
                jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[1], PORT, TIMEOUT);
            } catch (Exception e2) {
                logger.error("Second create JedisPool error : " + e2);
            }
        }
        return jedisPool;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */

    public synchronized static void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedis.close();
        }
    }

    public synchronized static Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    /**
     * 设置 0 库
     *
     * @param key
     * @param value
     */
    public static void setString0(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(0);

            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static void setPipelineString0(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(0);
            Pipeline pipelined = jedis.pipelined();
            pipelined.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings getString0(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(0);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    //*是否存在此组
    public static boolean existsString6(Strings key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(6);
            result = jedis.exists(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        System.out.println(result);
        return result;
    }

    //*是否存在此目录
    public static boolean existsString7(Strings key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(7);
            result = jedis.exists(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return result;
    }

    //*是否存在此用户
    public static boolean existsString9(Strings key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(9);
            result = jedis.exists(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return result;
    }

    //*是否存在此车牌号
    public static boolean existsString13(Strings key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(13);
            result = jedis.exists(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return result;
    }


    /**
     * 设置 1库
     *
     * @param key
     * @param value
     */
    public static void setString1(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(1);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    /*
    增添时间
     */
    public static void setString1SetLoncationTime(Strings key, Strings field, LinkedList linkedList) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(1);
            jedis.hset(key, field, JSON.toJSONString(linkedList));
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static void flushDB1() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(1);
            jedis.flushDB();
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("flushDB error : " + e);
        }
    }

    public static Strings getString1(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(1);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 2库
     *
     * @param key
     * @param value
     */
    public static void setString2(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(2);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString2(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(2);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }


    /**
     * 设置 3库
     *
     * @param key
     * @param value
     */
    public static void setString3(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(3);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString3(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            //getJedis().set(key, value);
            //addby wanglei
            //System.out.println("jedis set:"+Thread.currentThread().getId()+":"+key+":"+value);
            jedis = getJedis();
            jedis.select(3);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }


    /**
     * 设置 4库
     *
     * @param key
     * @param value
     */
    public static void setString4(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(4);
            jedis.set(key, value);
            returnResource(jedis);
            //addby wanglei
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString4(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(4);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }


    /**
     * 设置 5库
     *
     * @param key
     * @param value
     */
    public static void setString5(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            //getJedis().set(key, value);
            //addby wanglei
            //System.out.println("jedis set:"+Thread.currentThread().getId()+":"+key+":"+value);
            jedis = getJedis();
            jedis.select(5);
            jedis.set(key, value);
            returnResource(jedis);
            //addby wanglei
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings getString5(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(5);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }


    /**
     * 设置 6库
     *
     * @param key
     * @param value
     */
    public static void setString6(Strings key, Strings value) {
        Strings x = key.substring(0, 10);
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(6);
            Set s = jedis.keys(x + ":*");
            Iterator it = s.iterator();
            int w = 0;
            while (it.hasNext()) {
                Strings key1 = (Strings) it.next();
                Strings value1 = jedis.get(key1);
                if (value1.contains(value)) {
                    w = 1;
                }
            }
            if (w == 0) {
                jedis.set(key, value);
            }
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings getString6(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(6);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    public static void setString6Ex(Strings key, int t, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(6);
            jedis.setex(key, t, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings delString6(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(6);
            Set s = jedis.keys(key + "*");
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Strings key1 = (Strings) it.next();
                //Todo 返回再删除
                jedis.del(key1);
            }
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    public static List<Strings> getAllCommd6(Strings IMEI) {
        List<Strings> list = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(6);
            Set s = jedis.keys(IMEI + "*");
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Strings key = (Strings) it.next();
                Strings value = jedis.get(key);
                //Todo 返回再删除
//                 jedis.del(key);
                list.add(value);
            }
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error(" getAllComd error : " + e);
        }
        return list;
    }

    /**
     * 设置 7库
     *
     * @param key
     * @param value
     */
    public static void setString7(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(7);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings getString7(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(7);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 8库
     *
     * @param key
     * @param value
     */
    public static void setString8(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(8);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings getString8(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(8);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 9库
     *
     * @param key
     * @param value
     */
    public static void setString9(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(9);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    public static Strings getString9(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(9);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 10库
     *
     * @param key
     * @param value
     */
    public static void setString10(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(10);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString10(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(10);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 11库
     *
     * @param key
     * @param value
     */
    public static void setString11(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(11);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString11(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(11);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 12库
     *
     * @param key
     * @param value
     */
    public static void setString12(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(12);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString12(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(12);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 13库
     *
     * @param key
     * @param value
     */
    public static void setString13(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(13);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString13(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(13);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 14库
     *
     * @param key
     * @param value
     */
    public static void setString14(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(14);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString14(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(14);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    /**
     * 设置 15库
     *
     * @param key
     * @param value
     */
    public static void setString15(Strings key, Strings value) {
        Jedis jedis = null;
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis = getJedis();
            jedis.select(15);
            jedis.set(key, value);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);

        }
    }

    public static Strings getString15(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(15);
            str = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }

    public static void delStringByKeyAndDbNum(Strings key, int dbNum) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(dbNum);
            System.out.println("redis delete " + key);
            jedis.del(key);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
    }

    /*
   指令库 6
    */
    public static Map<Strings, Strings> getAllCommdMap() {
        Map<Strings, Strings> aMap = new HashMap<Strings, Strings>();
        List<Strings> list = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(6);
            Set s = jedis.keys("*");
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Strings key = (Strings) it.next();
                Strings value = jedis.get(key);
                key = key.split(":")[0];
                aMap.put(key, value);
            }
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error(" getAllComd error : " + e);
        }
        return aMap;
    }

    /*
    指令库 6
     */
    public static Strings getAllCommd(Strings IMEI) {
        Strings xStrings = "";
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(6);
            xStrings = jedis.get(IMEI);
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error(" getAllComd error : " + e);
        }
        return xStrings;
    }

    public static Strings delString7(Strings key) {
        Jedis jedis = null;
        Strings str = null;
        try {
            jedis = getJedis();
            jedis.select(7);
            Set s = jedis.keys(key + "*");
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Strings key1 = (Strings) it.next();
                //Todo 返回再删除
                jedis.del(key1);
            }
            returnResource(jedis);
        } catch (Exception e) {
            returnResource(jedis);
            e.printStackTrace();
            logger.error("Set key error : " + e);
        }
        return str;
    }
}
