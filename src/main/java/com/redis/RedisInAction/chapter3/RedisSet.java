/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import redis.clients.jedis.Jedis;

import com.redis.RedisInAction.util.JedisPoolUtil;
/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月7日 下午3:58:14 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisSet {

	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 * @author 闭门车  
	 */
	public static void main(String[] args) {
		String set="setKey";
		Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			jedis.del(set);
 			System.out.println(jedis.sadd(set, "a","b","c"));
 			System.out.println(jedis.smembers(set));
 			System.out.println(jedis.srem(set, "d","c"));
 			System.out.println(jedis.smembers(set));
 			System.out.println(jedis.scard(set));
	} catch (Exception e) {
         //释放redis对象
   	  JedisPoolUtil.returnResource(jedis);
         e.printStackTrace();
     } finally {
         //返还到连接池
   	  System.out.println("release jedis");
   	  JedisPoolUtil.close(jedis);
     } 
	}

}

