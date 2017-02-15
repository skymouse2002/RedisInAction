/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import com.redis.RedisInAction.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月7日 下午1:28:52 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisList {

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
		String list="list";
		Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			System.out.println(jedis.rpush(list, "last"));
 			System.out.println(jedis.lpush(list, "first"));
 			System.out.println(jedis.rpush(list, "new last"));
 			System.out.println(jedis.lrange(list, 0, -1));
 			System.out.println(jedis.lpop(list));
 			System.out.println(jedis.lpop(list));
 			
 			System.out.println(jedis.rpush(list, "a","b","c"));
 			System.out.println(jedis.lrange(list, 0, -1));
 			System.out.println(jedis.ltrim(list, 6, 9));
 			System.out.println(jedis.lrange(list, 0, -1));
// 			System.out.println(jedis.lpop(list));
 			
 			System.out.println(jedis.rpush("list1", "item1"));
 			System.out.println(jedis.rpush("list1", "item2"));
 			System.out.println(jedis.rpush("list2", "item2"));
 			System.out.println(jedis.brpoplpush("list1", "list2", 1));
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

