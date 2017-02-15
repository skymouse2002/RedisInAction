/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import redis.clients.jedis.Jedis;

import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月8日 下午4:29:39 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class Publisher {

//    public Publisher(JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }

    public void start() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Jedis jedis=null;
        try{
        jedis=JedisPoolUtil.getResource();
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
                if (!"quit".equals(line)) {
                    jedis.publish("mychannel", line);
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
	/**
	 * 
	 */
	public Publisher() {
		// TODO Auto-generated constructor stub
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

