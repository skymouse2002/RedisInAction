/**
 * 
 */
package com.redis.RedisInAction.chapter5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.redis.RedisInAction.chapter4.AddPrice;
import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月11日 下午10:07:14 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisSaveConfig {
    private volatile boolean IS_UNDER_MAINTENANCE=false;
    private volatile long Last_checked=0L;
    
    /**
     * 
      * @Title: is_under_maintenance 
      * @Description: Redis存储服务器的是否正在维护的标志
      * @param @param jedis
      * @param @return    设定文件 
      * @return boolean    返回类型 
      * @throws 
      * @author 闭门车
     */
    public boolean is_under_maintenance( Jedis jedis){
    	if(Last_checked<System.currentTimeMillis()-1000){
    		Last_checked=System.currentTimeMillis();
    		IS_UNDER_MAINTENANCE=Boolean.valueOf(jedis.get("is_under_maintenance"));
    	}
    	return IS_UNDER_MAINTENANCE;
    }
    /**
     * 
      * @Title: set_config 
      * @Description: Redis存储服务器的Json配置
      * @param @param jedis
      * @param @param type
      * @param @param component
      * @param @param jsonConfig    设定文件 
      * @return void    返回类型 
      * @throws 
      * @author 闭门车
     */
    public void set_config(Jedis jedis,String type,String component,String jsonConfig){
    	jedis.set("config:"+type+":"+component, jsonConfig);
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

