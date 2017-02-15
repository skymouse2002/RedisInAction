/**
 * 
 */
package com.redis.RedisInAction.chapter4;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月10日 上午10:57:31 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class WatchTransaction {

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
		int a=10;
		method(a);
		Jedis jedis=null;
		String buyer=null;
		String market="market:";
		String product="product";
		Map<String,Double> hash=new HashMap<String,Double>();
 		try {
 			jedis=JedisPoolUtil.getResource();
 			
 			Pipeline pipeline =jedis.pipelined();
 			pipeline.watch(market);
 			
 			pipeline.del(market);
 			Response<String> response=pipeline.multi();
 			for(int i=0;i<10;i++){
 				pipeline.sadd(market, product+i);
 			}		
 			Response<List<Object>> list=pipeline.exec();
 			List<Object> results =pipeline.syncAndReturnAll();
 			
 			String str=jedis.unwatch();
 			System.out.println();
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
//	public static void method(int a){
//		WatchTransaction WatchTransaction=new WatchTransaction();
//		Method[] methods= WatchTransaction.getClass().getMethods();
//		for(Method method:methods){
//			String str=method.getName();
//			if(str.equals("main")){
//				System.out.println(method.);
//			}
//		}
//	}

}

