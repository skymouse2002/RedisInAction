/**
 * 
 */
package com.redis.RedisInAction.chapter6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redis.RedisInAction.chapter4.AddPrice;
import com.redis.RedisInAction.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月11日 下午10:28:27 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisSerUser {
    
	/**
	 * 
	  * @Title: add_update_contract 
	  * @Description: 最近的联系人列表
	  * @param @param jedis
	  * @param @param user
	  * @param @param contract    设定文件 
	  * @return void    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
    public static void add_update_contract(Jedis jedis,String user,String contract){
    	String recent="recent:"+user;
    	Pipeline pipeLine =jedis.pipelined();
    	pipeLine.lrem(recent, 0, contract);//若用户存在于最近的列表中，移除
    	pipeLine.lpush(recent, contract);//将用户添加入最近联系人的列表
    	pipeLine.ltrim(recent, 0, 49);
    	pipeLine.sync();
    }
    /**
     * 
      * @Title: fetch_autocomplete_list 
      * @Description: 某个用户的最近联系人中带prefix关键字的人
      * @param @param jedis
      * @param @param user
      * @param @param prefix
      * @param @return    设定文件 
      * @return List    返回类型 
      * @throws 
      * @author 闭门车
     */
    public static List<String> fetch_autocomplete_list(Jedis jedis,String user,String prefix){
    	String recent="recent:"+user;
    	List<String> candidates=jedis.lrange(recent, 0, -1);
    	List<String> recentList=new ArrayList(50);
    	for(String contract:candidates){
    		if(contract.toLowerCase().startsWith(prefix)){
    			recentList.add(contract);
    		}
    	}
    	return recentList;
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
		Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			String user="user";
 			for(int i=0;i<53;i++){
 				add_update_contract(jedis,user,user+i);
 			}
 			System.out.println(jedis.lrange("recent:user", 0, -1));
 			List<String> list=fetch_autocomplete_list(jedis,user,user+1);
 			for(String str:list){
 				System.out.print(str);
 			}
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

