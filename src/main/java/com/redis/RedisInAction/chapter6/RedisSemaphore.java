/**
 * 
 */
package com.redis.RedisInAction.chapter6;

import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月13日 下午5:30:37 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisSemaphore {
	/**
	 * 
	  * @Title: acquireSemaphore 
	  * @Description: 尝试获取信号量
	  * @param @param jedis
	  * @param @param semName
	  * @param @param limit
	  * @param @param timeout
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
    public static String acquireSemaphore(Jedis jedis,String semName,int limit,long timeout){
    	String identifier=UUID.randomUUID().toString().replace("-", "");
    	long now=System.currentTimeMillis();
//    	String recent="recent:"+user;
    	Pipeline pipeLine =jedis.pipelined();
    	pipeLine.zremrangeByScore(semName,0, now-timeout);
    	pipeLine.zadd(semName, now, identifier);
    	Response<Long> posi=pipeLine.zrank(semName, identifier);
    	pipeLine.sync();
    	Long position=posi.get();
    	if(position>limit){
    		return identifier;
    	}
    	return null;  	
    }
    public static String acquireFairSemaphore(Jedis jedis,String semName,int limit,long timeout){
    	String identifier=UUID.randomUUID().toString().replace("-", "");
    	String czset=semName+":owner";
    	String ctr=semName+":counter";
    	long now=System.currentTimeMillis();
//    	String recent="recent:"+user;
    	Pipeline pipeLine =jedis.pipelined();
    	pipeLine.zremrangeByScore(semName,0, now-timeout);
    	pipeLine.zinterstore(czset, params, sets);
    	pipeLine.zadd(semName, now, identifier);
    	Response<Long> posi=pipeLine.zrank(semName, identifier);
    	pipeLine.sync();
    	Long position=posi.get();
    	if(position>limit){
    		return identifier;
    	}
    	return null;  	
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

	}

}

