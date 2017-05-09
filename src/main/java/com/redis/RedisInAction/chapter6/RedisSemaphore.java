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
	  * @Description: 尝试获取信号量 （两个系统时间不一致的情况会有信号量被偷的情况）快的会偷取慢的系统的信号量
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
    	//删除超时的信号量
    	pipeLine.zremrangeByScore(semName,0, now-timeout);
    	//将新的加入到zset中
    	pipeLine.zadd(semName, now, identifier);
    	//排序 查看持有的信号量的位置 如果超过了信号量大小 则取得信号量失败，否则成功
    	Response<Long> posi=pipeLine.zrank(semName, identifier);
    	pipeLine.sync();
    	Long position=posi.get();
    	if(position>limit){
    		return identifier;
    	}
    	return null;  	
    }
    /**
     * 
      * @Title: acquireFairSemaphore 
      * @Description: 公平信号量  
      * @param @param jedis
      * @param @param semName
      * @param @param limit
      * @param @param timeout
      * @param @return    设定文件 
      * @return String    返回类型 
      * @throws 
      * @author 闭门车
     */
    public static String acquireFairSemaphore(Jedis jedis,String semName,int limit,long timeout){
    	String identifier=UUID.randomUUID().toString().replace("-", "");
    	String czset=semName+":owner";
    	String ctr=semName+":counter";
    	long now=System.currentTimeMillis();
//    	String recent="recent:"+user;
    	Pipeline pipeLine =jedis.pipelined();
    	//删除超时的信号量
    	pipeLine.zremrangeByScore(semName,0, now-timeout);//
    	pipeLine.zinterstore(czset, "1 0", czset+" "+semName);//
    	Response<Long> counter =pipeLine.incr(ctr);
    	//获取添加信号量
    	pipeLine.zadd(semName, now, identifier);
    	pipeLine.zadd(czset, counter.get(), identifier);
    	
    	Response<Long> posi=pipeLine.zrank(semName, identifier);//检查添加的信号量排名
    	pipeLine.sync();
    	Long position=posi.get();
    	if(position>limit){
    		return identifier;
    	}
    	//获取信号量失败后删除掉添加失败的信号量
    	pipeLine.zrem(semName, identifier);
    	pipeLine.zrem(czset, identifier);
    	return null;  	
    }
    /**
     * 
      * @Title: refresh_Fair_Semaphore 
      * @Description: 刷新信号量
      * @param @param jedis
      * @param @param semName
      * @param @param identifier
      * @param @return    设定文件 
      * @return boolean    返回类型 
      * @throws 
      * @author 闭门车
     */
    public boolean refresh_Fair_Semaphore(Jedis jedis,String semName,String identifier){
    	Pipeline pipeLine =jedis.pipelined();
    	long now=System.currentTimeMillis();
    	Response<Long> posi=pipeLine.zadd(semName, now, identifier);
    	//添加成功的话 就是大于0表明原来不存在这个信号量  删除添加的信号量  返回false
    	if(posi.get()>0){
    		pipeLine.zrem(semName, identifier);
    		return false;
    	}
    	pipeLine.sync();
    	//刷新成功 返回true
    	return true;
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

