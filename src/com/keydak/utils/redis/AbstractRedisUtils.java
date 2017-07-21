package com.keydak.utils.redis;

import redis.clients.jedis.Jedis;

/**
 * 抽象的RedisUtils
 *
 * 实现finalize（），确保在JVM出发‘垃圾回收机制’的时候，销毁系统资源。
 * 提供获取Jedis客户端的抽象方法
 * 提供初始化工具类的抽象方法
 *
 * Created by Caisz on 2016/12/14.
 */
public abstract class AbstractRedisUtils implements IRedisUtils {

    /**
     * 获取jedis客户端
     * @return
     */
    protected abstract Jedis getJedis();

    /**
     * 初始化构造方法的时候调用
     */
    protected abstract void init() throws Exception;

    protected void finalize() throws java.lang.Throwable {
        // 递归调用超类中的finalize方法
        destroy();
        super.finalize();
    }
}
