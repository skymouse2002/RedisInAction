/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月7日 下午10:40:45 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisMap {

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
		String map="map";
		Jedis jedis=null;
		Map<String,String> hash=new HashMap<String,String>();
 		try {
 			jedis=JedisPoolUtil.getResource();
 			jedis.del(map);
 			hash.put("k1", "v1");
 			hash.put("k2", "v2");
 			hash.put("k3", "v3");
 			System.out.println(jedis.hmset(map, hash));
 			System.out.println(jedis.hlen(map));
 			System.out.println(jedis.hkeys(map));
 			System.out.println(jedis.hvals(map));
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

