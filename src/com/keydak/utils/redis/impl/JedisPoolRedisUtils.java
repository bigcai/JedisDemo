package com.keydak.utils.redis.impl;

import com.keydak.utils.redis.AbstractRedisUtils;
import com.keydak.utils.redis.IRedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis命令工具类
 *
 * jedis本身不具备线程安全和连接池管理功能，使用JedisPool实现jedis客户端操作，
 * 采用JedisPool使得redis具备有线程安全特性，同时减少连接的重复创建次数。
 *
 * Created by Caisz on 2016/12/15.
 */
public class JedisPoolRedisUtils extends AbstractRedisUtils implements IRedisUtils {

    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static final int CONSTANT_MAX_TOTAL = 500;
    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
    private static final int CONSTANT_MAX_IDLE = 5;
    // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
    private static final long CONSTANT_MAX_WAIT_MILLIS = 1000*100 ;
    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static final boolean CONSTANT_TEST_ON_BORROW = true;
    // redis服务器 Ip + 端口
    private static final String CONSTANT_REDIS_SERVER_IP = "localhost";
    private static final int CONSTANT_REDIS_SERVER_PORT = 6379;


    private JedisPool pool = null;

    public JedisPoolRedisUtils() {
        try{
            // 初始化连接池
            init();
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }

    /**
     *  受保护的Jedis客户端获取方法
     * @return
     */
    @Override
    protected Jedis getJedis() {
        try{
            // 同步初始化Redis连接池
            if( pool == null ) {
                synchronized (JedisPoolRedisUtils.class) {
                    if( pool == null ) {
                        init();
                    }
                }
            }
            // 返回jedis客户端
            return pool.getResource();
        } catch ( Exception e ) {
            System.out.println( "pool Connection inited is Exception ! -- " + e.getMessage() );
        }
        return null;
    }

    /**
     *  初始化连接池
     * @throws Exception
     */
    @Override
    protected void init() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        config.setMaxTotal( CONSTANT_MAX_TOTAL );
        config.setMaxIdle( CONSTANT_MAX_IDLE );
        config.setMaxWaitMillis( CONSTANT_MAX_WAIT_MILLIS );
        config.setTestOnBorrow( CONSTANT_TEST_ON_BORROW );
        pool = new JedisPool(config, CONSTANT_REDIS_SERVER_IP, CONSTANT_REDIS_SERVER_PORT);
        System.out.println( "RedisUtils had inited!");
    }

    /**
     *  线程安全的连接池销毁方法，可以在异常处理时调用销毁连接池，或者等待 ‘JVM垃圾回收’ 的时候延迟销毁。
     */
    @Override
    public void destroy() {
        System.out.println( "RedisUtils had finalized!");
        try {
            // 同步销毁连接池
            if( pool != null ) {
                synchronized (JedisPoolRedisUtils.class) {
                    if( pool != null ) {
                        pool.close();
                    }
                }
            }
        } catch ( Exception e ) {
            System.out.println( "pool closed  throw exception! --- " + e.getMessage() );
        }

    }

    @Override
    public String ping() {
        Jedis jedis = null;
        try{
            jedis = this.getJedis();
            if( jedis != null ) {
                return jedis.ping();
            }
        }
        finally {
            if( jedis != null ) jedis.close();
        }
        return null;
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = this.getJedis();
            if( jedis != null ) {
                return jedis.set( key, value );
            }
        }
        finally {
            if( jedis != null )  jedis.close();
        }
        return null;
    }

    @Override
    public Object get(String key) {
        Jedis jedis = null;
        try{
            jedis = this.getJedis();
            if( jedis != null ) {
                return jedis.get(key);
            }
        }
        finally {
            if( jedis != null ) jedis.close();
        }
        return null;
    }

    /**
     * 使用示例
     * @param args
     */
    public static void main(String[] args) {
        IRedisUtils redisUtils = null;
        try{
            // 获取Redis工具类
            redisUtils = new JedisPoolRedisUtils();

            // 测试Ping命令
            System.out.println( "Server is running: " + redisUtils.ping() );
            // 测试set、get命令
            redisUtils.set( "myname", "假猪套天下第一！" );
            System.out.println( "myname : " + redisUtils.get( "myname" ) );
        }
        finally {
            // 即时关闭连接资源
            if( redisUtils != null ) { redisUtils.destroy(); }
        }
    }
}
