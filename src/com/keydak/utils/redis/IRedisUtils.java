package com.keydak.utils.redis;

/**
 * Redis命令接口
 * Created by Caisz on 2016/12/14.
 */
public interface IRedisUtils {

    /**
     * RedisUtils 的析构函数，负责在回收对象时关闭资源，如:连接池对象等等
     */
    public void destroy();

    /**
     * redis ping命令
     * @return
     */
    public String ping();

    /**
     * redis set命令
     * @param key
     * @param value
     * @return
     */
    public Object set( String key,String value );

    /**
     * redis get命令
     * @param key
     * @return
     */
    public Object get( String key );

}
