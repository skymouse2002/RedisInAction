/**
 * 
 */
package cn.liuchg.redis;

import com.redis.RedisInAction.util.JedisPoolUtil;

import cn.liuchg.redis.queue.ObjectUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2016年12月30日 下午5:02:50 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class TestRedisQuene {
	public static byte[] redisKey = "key".getBytes();
    static{
        init();
    }

 
    private static void pop() {
    	
        byte[] bytes = JedisPoolUtil.rpop(redisKey);
        Message msg;
		try {
			msg = (Message) ObjectUtil.bytesToObject(bytes);
	        if(msg != null){
	            System.out.println(msg.getId()+"   "+msg.getContent());
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
 
    private static void init() {
        Message msg1 = new Message(1, "内容1");
        Message msg2 = new Message(2, "内容2");
        Message msg3 = new Message(3, "内容3");
        Message msg4 = new Message(4, "内容4");
        Message msg5 = new Message(5, "内容5");
        Message msg6 = new Message(6, "内容6");
        try {
        	
			JedisPoolUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg1));
			JedisPoolUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg2));	        
			JedisPoolUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg3));
			JedisPoolUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg4));
			JedisPoolUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg5));
			JedisPoolUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg6));
			System.out.println(" has inited");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
//		JedisPoolUtil.set("1234", "123456");
//		System.out.println(JedisPoolUtil.get("1234"));
//		init();
		int i=JedisPoolUtil.lListSize(redisKey);
		for(int j=0;j<i;j++){
			pop();
		}
		
	}

}

