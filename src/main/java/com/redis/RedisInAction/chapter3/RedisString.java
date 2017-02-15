/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import redis.clients.jedis.Jedis;

import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月6日 下午3:23:39 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisString {
     private static String SEQ="seq";
     
     /**
      * 
       * @Title: initSeq 
       * @Description: 初始化序列
       * @param @param key
       * @param @param value
       * @param @return    设定文件 
       * @return String    返回类型 
       * @throws 
       * @author 闭门车
      */
     public boolean initSeq(String key,String value){
    	 Jedis jedis=null;
  		try {
  			jedis=JedisPoolUtil.getResource();
  			//初始化序列
  			if(!jedis.exists(key)){
  				jedis.set(key, value);
  				return true;
  			}
  			return false;
  	 } catch (Exception e) {
           //释放redis对象
     	  JedisPoolUtil.returnResource(jedis);
           e.printStackTrace();
           return false;
       } finally {
           //返还到连接池
     	  System.out.println("release jedis");
     	  JedisPoolUtil.close(jedis);
       }  
     }
     public String getSeqCur(String key){
    	 Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			//初始化序列
 			if(jedis.exists(key)){
 				return jedis.get(key);
 			}
 			return "";
 	 } catch (Exception e) {
          //释放redis对象
    	  JedisPoolUtil.returnResource(jedis);
          e.printStackTrace();
          return "error";
      } finally {
          //返还到连接池
    	  System.out.println("release jedis");
    	  JedisPoolUtil.close(jedis);
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
		String value="10000";
		Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			//初始化序列
 			if(!jedis.exists(SEQ)){
 				jedis.set(SEQ, value);
 				System.out.println("init SEQ success ");
 			}
				jedis.set(SEQ, "1");

// 			System.out.println(jedis.incr(SEQ));
 			String abc="abc";
 			String temp="defgh";
 			String bit="bit";
 			String bitvalue="1";
 			jedis.set(abc, abc);
 			System.out.println(jedis.append(abc, temp));
 			System.out.println(jedis.get(abc));
 			System.out.println(jedis.getrange(abc, 1, 3));
 			System.out.println(jedis.getrange(abc, 9, 12));
 			System.out.println(jedis.setrange(abc, 6L, "xyz"));
 			
 			System.out.println(jedis.get(abc));
 			System.out.println(jedis.del(bit));
 			System.out.println(jedis.setbit(bit, 21, "1"));
 			System.out.println(jedis.get(bit));
 			System.out.println(jedis.setbit(bit, 22, "1"));
 			System.out.println(jedis.setbit(bit, 20, "0"));
 			System.out.println(jedis.get(bit));
 			System.out.println(jedis.bitcount(bit));
 			
// 			System.out.println(jedis.getbit(bit, 0L));
// 			System.out.println(jedis.getbit(bit, 1L));
// 			System.out.println(jedis.getbit(bit, 2L));
// 			System.out.println(jedis.getbit(bit, 3L));
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

