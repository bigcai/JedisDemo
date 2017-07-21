# 这是Java的Redis客户端工具类  - 2016/12/15 caisz

## 该模块包含2个主要文件:

    包路径 com.keydak.utils.redis
    - 命令接口声明 IRedisUtils : 声明给外界使用的工具类接口
    - 抽象工具类 AbstractRedisUtils : 统一的工具类实现模板

## java Redis客户端拥有2类常用的实现方案：

- 采用jedis连接池直连redis单机服务器
- 采用jedis连接redis服务器集群

### redis服务器集群方案截止于目前有3种方案：

- 采用客户端shared切片访问相互独立的redis服务器集群，该方案存在数据同步问题。
- 采用服务器同步集群，客户端直连集群的Master，Master从slave选举获得，相关技术有官方的推出的redis cluster，暂时不适合生产环境，
以及 社区的Redis Sentinel哨兵，哨兵较多人使用 。
- 采用redis集群的代理中间件如：Twemproxy，以及 豌豆荚的Codis

### 本模块中 com.keydak.utils.redis.impl.JedisPoolRedisUtils 采用jedis连接池直连redis单机服务器,其他方案需要另外实现。



