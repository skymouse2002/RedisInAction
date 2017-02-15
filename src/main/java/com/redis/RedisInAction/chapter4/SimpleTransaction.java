/**
 * 
 */
package com.redis.RedisInAction.chapter4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redis.RedisInAction.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月10日 上午10:54:39 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class SimpleTransaction {

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
		String key="key";
		Jedis jedis=null;
		Map<String,Double> hash=new HashMap<String,Double>();
		Thread t1=new Thread(new AddPrice(),"addpriceThread");
 		try {
 			jedis=JedisPoolUtil.getResource();
 			jedis.set("car", "100000");
 			jedis.set("price", "10000");
 			String  res=jedis.watch("price");
 			Transaction tx = jedis.multi();
 			
 			t1.start();
 			
 			tx.incrBy("car", 20009);
// 			System.out.println(res.toString());
 			tx.incrBy("price", 100);
 			List<Object> results = tx.exec();
 			System.out.println(jedis.get("price"));
 			System.out.println(jedis.get("car"));
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

