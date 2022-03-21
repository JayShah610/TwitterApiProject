package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisHelper  {
    private static Logger logger = LoggerFactory.getLogger(RedisHelper.class);
    private static final String HOST = ConfigProperties.getProperty("redis.host");
    private static final Integer PORT = Integer.parseInt(ConfigProperties.getProperty("redis.port"));
    private static final String DEFAULTPASSWORD = ConfigProperties.getProperty("redis.password");
    private static final String KEY= "TweetIDs";
    private static final JedisPoolConfig POOLCONFIG;
    static {
        POOLCONFIG = new JedisPoolConfig();
        POOLCONFIG.setMaxTotal(64);
    }
    private static final JedisPool jedisPool = new JedisPool(POOLCONFIG,HOST ,PORT);

    private RedisHelper(){

    }
    public static String getHost() { return HOST; }
    public static Integer getPort() { return PORT;}
    public static String getPassword() { return DEFAULTPASSWORD;}
    public static String getKey() { return KEY; }



    public static void addToRedis(String id){
       try(Jedis jedis = jedisPool.getResource()){
            jedis.sadd(KEY,id);
       }
       catch (Exception e){
           logger.info("Problem in adding the element in Redis");
       }

    }

    public static boolean isPresent(String id){
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.sismember(KEY, id);
        }
        catch (Exception e){
            logger.info("Exception occurs at RedisHelper file");
        }
        return false;
    }

    public static void close(){
        jedisPool.close();
        logger.info("Redis closed");
    }

}
