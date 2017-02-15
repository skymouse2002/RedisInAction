/**
 * 
 */
package com.redis.RedisInAction.chapter3;

import com.redis.RedisInAction.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月8日 下午4:23:57 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class SubThread extends Thread {
//	private final JedisPool jedisPool;
    private final Subscriber subscriber = new Subscriber();

    private final String channel = "mychannel";

    public SubThread() {
//        super("SubThread");
//        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        Jedis jedis = null;
        try {
        	jedis=JedisPoolUtil.getResource();
            jedis.subscribe(subscriber, channel);
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
          //释放redis对象
   	   	  JedisPoolUtil.returnResource(jedis);
        } finally {
   	   	  System.out.println("release jedis"+this.getClass().getMethods());
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

	}

}

