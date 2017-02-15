/**
 * 
 */
package com.redis.RedisInAction.chapter4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月10日 下午10:09:10 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class AddPrice implements Runnable {

	public void run() {
		// TODO Auto-generated method stub
		String key="key";
		Jedis jedis=null;
		Map<String,Double> hash=new HashMap<String,Double>();
 		try {
 			jedis=JedisPoolUtil.getResource();
 			jedis.incrBy("price", 99);
 			
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

