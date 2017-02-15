/**
 * 
 */
package com.redis.RedisInAction.util;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年1月3日 下午3:51:53 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class JedisPoolUtil {
private static JedisPool pool = null;
    static {
    	if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//            config.setMaxActive(500);
            config.setMaxTotal(20);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(20);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//            config.setMaxWait(1000 * 100);
            config.setMaxWaitMillis(1000*100);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "192.168.137.3", 6379);
        }
    }
    /**
     * 构建redis连接池
     * 
     * @param ip
     * @param port
     * @return JedisPool
     */
    public static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//            config.setMaxActive(500);
            config.setMaxTotal(500);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(50);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//            config.setMaxWait(1000 * 100);
            config.setMaxWaitMillis(1000*100);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "192.168.137.3", 6379);
        }
        return pool;
    }
    /**
     * 
      * @Title: returnResource 
      * @Description: TODO(这里用一句话描述这个方法的作用) 
      * @param @param pool
      * @param @param redis    设定文件 
      * @return void    返回类型 
      * @throws 
      * @author 闭门车
     */
    public static void returnResource(Jedis redis) {
    	returnResource(pool,redis);
    }
    /**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
//            pool.returnResourceObject(redis);
            pool.close();
        }
    }
    /**
     * 
      * @Title: returnBrokenResource 
      * @Description: TODO(这里用一句话描述这个方法的作用) 
      * @param @param jedis    设定文件 
      * @return void    返回类型 
      * @throws 
      * @author 闭门车
     */
	public static void returnBrokenResource(Jedis jedis) {
		// TODO Auto-generated method stub
		pool.returnBrokenResource(jedis);
	}
    /**
     * 
      * @Title: getResource 
      * @Description: TODO(这里用一句话描述这个方法的作用) 
      * @param @return    设定文件 
      * @return Jedis    返回类型 
      * @throws 
      * @author 闭门车
     */
    public static Jedis getResource(){
    	return pool.getResource();
    }
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String get(String key){
        String value = null;         
//        JedisPool pool = null;
        Jedis jedis = null;
        try {
//            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            pool.close();
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }         
        return value;
    }
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String set(String key,String value){
//        String value = null;         
//        JedisPool pool = null;
        Jedis jedis = null;
        try {
//            pool = getPool();
            jedis = pool.getResource();
            value = jedis.set(key, value);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }         
        return value;
    }
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static int lListSize(byte[] key) {
 
        List<byte[]> list = null;
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            list = jedis.lrange(key, 0, -1);
            return list.size();
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//        	pool.close();
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return list.size();
    }
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static List<byte[]> lpopList(byte[] key) {
 
        List<byte[]> list = null;
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            list = jedis.lrange(key, 0, -1);
            System.out.println(list.size());
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//        	pool.close();
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return list;
    }
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static byte[] rpop(byte[] key) {
 
        byte[] bytes = null;
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            bytes = jedis.rpop(key);
 
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//            pool.close();
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return bytes;
    }
    /**
     * 存储REDIS队列 顺序存储
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void lpush(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
 
            jedis =pool.getResource();
            jedis.lpush(key, value);
//           System.out.println(key);
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//        	pool.close();
            e.printStackTrace();
 
        } finally {
//        	jedis.close();
            //返还到连接池
            close(jedis);
 
        }
    }
 
    /**
     * 存储REDIS队列 反向存储
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpush(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            jedis.rpush(key, value);
 
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//        	pool.close();
            e.printStackTrace();	 
        } finally {	 
            //返还到连接池
            close(jedis);
 
        }
    }
    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpoplpush(byte[] key, byte[] destination) {
 
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            jedis.rpoplpush(key, destination);
 
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//            pool.close();
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
    /**
     * 
      * @Title: zadd 
      * @Description: TODO(这里用一句话描述这个方法的作用) 
      * @param @param key
      * @param @param score
      * @param @param member    设定文件 
      * @return void    返回类型 
      * @throws 
      * @author 闭门车
     */
    public static void zadd(String key, double score,String member) {
    	 
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            jedis.zadd(key, score, member);
 
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//            pool.close();
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
    /**
     * 
      * @Title: zadd 
      * @Description: TODO(这里用一句话描述这个方法的作用) 
      * @param @param key
      * @param @param score
      * @param @param member    设定文件 
      * @return void    返回类型 
      * @throws 
      * @author 闭门车
     */
    public static void zadd(byte[] key,double score,byte[] member) {
    	 
        Jedis jedis = null;
        try {
 
            jedis = pool.getResource();
            jedis.zadd(key, score, member);
 
        } catch (Exception e) {
        	pool.returnResource(jedis);
            //释放redis对象
//            pool.close();
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
    public static void close(Jedis jedis) {
        try {
            if(null != pool) {
                pool.returnResource(jedis);        
            }
//            jedisPool.returnResource(jedis); //低版本方法
//             pool.close();//2.8版本中释放redis对象方法
        } catch (Exception e) {
            if (jedis.isConnected()) {
                jedis.quit();
                jedis.disconnect();
            }
        }
    }
    
	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 * @author 闭门车  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JedisPoolUtil.set("123", "123456");
		System.out.println(JedisPoolUtil.get("123"));
	}


}

