/**
 * 
 */
package com.redis.RedisInAction.chapter6;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月15日 下午4:45:36 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisQueue {
	public static String dumpJson(String seller,String item,String price,String buyer){
		return null;
	}
	public static String loadJson(String json){
		return null;
	}
	 static String fentch_data_and_send_email(String email){
		return null;
	}
	/**
	 * 
	  * @Title: send_sold_email_via_queue 
	  * @Description: 将信息转json序列化后推入邮件列表
	  * @param @param jedis
	  * @param @param seller
	  * @param @param item
	  * @param @param price
	  * @param @param buyer
	  * @param @param timeout
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public static String send_sold_email_via_queue(Jedis jedis,String seller,String item,String price,String buyer){
    	String identifier=UUID.randomUUID().toString().replace("-", "");
    	String json=dumpJson( seller, item, price, buyer);
    	jedis.rpush("queue:email", json);
    	return null;  	
    }
	public static void process_sold_email_queue(Jedis jedis){
    	boolean quit=false;
    	List<String> list=null;
    	while(!quit){
    		 list=jedis.blpop(30, "queue:email");
        	if(list.size()==0){
        		quit=false;//未取得邮件，继续
        	}
        	for(String json:list){
                String to_send=loadJson(json);
                fentch_data_and_send_email(to_send);
                System.out.println("Send sold email:"+to_send);
    	    }
        	quit=true;
    	}	
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

