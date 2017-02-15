/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import java.util.Map;

import com.redis.RedisInAction.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月8日 下午5:41:56 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisSort {

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
		String zset="zsetKey";
		Jedis jedis=null;
		Map<String,Double> hash=new HashMap<String,Double>();
 		try {
 			jedis=JedisPoolUtil.getResource();
 			jedis.del(zset);
 			hash.put("a", 123.1);
 			hash.put("b", 1.23456789);
 			hash.put("c", 9.1234567809);
 			System.out.println(jedis.zadd(zset, 122.1111111111, "d"));
 			System.out.println(jedis.zincrby(zset, 12.5000000009, "a"));
 			System.out.println(jedis.zscore(zset, "a"));
 			System.out.println(jedis.zrank(zset, "b"));//查看b的排名
 			System.out.println(jedis.zrank(zset, "a"));
 			System.out.println(jedis.zrange(zset, 0, -1));
 			System.out.println(jedis.zrangeWithScores(zset, 0, -1));
 			hash.put("e", 123.456789);
 			Map<String,Double> hash1=new HashMap<String,Double>();
 			hash1.putAll(hash);
 			hash1.put("f", 123456789.0);
 			System.out.println(jedis.zadd("zsetKey1", hash1));
 			System.out.println(jedis.zrange(zset, 0, -1));
 			System.out.println(jedis.zrange("zsetKey1", 0, -1));

 			
 			System.out.println(jedis.zinterstore("zsetKey4", "zsetKey","zsetKey1"));

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

