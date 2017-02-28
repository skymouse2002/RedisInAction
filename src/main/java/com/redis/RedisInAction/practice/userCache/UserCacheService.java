/**
 * 
 */
package com.redis.RedisInAction.practice.userCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;

import com.redis.RedisInAction.util.DBUtils;
import com.redis.RedisInAction.util.JedisPoolUtil;
import com.redis.RedisInAction.util.ObjectUtil;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月23日 下午2:02:16 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class UserCacheService {
	String set="UserSet";
	public Message compareUser(User user){
		Message message=new Message();
		Jedis jedis=null;
		try {
			jedis=JedisPoolUtil.getResource();
			User userX=null;
				
				byte[] b= jedis.hget(set.getBytes(), user.getUsr_Code().getBytes());
				if(b==null){
					message.setNO(0);
					message.setMessage("用户不存在");
					return message;
				}
				userX=(User) ObjectUtil.bytesToObject(b);
				System.out.println("userX is "+userX.getUsr_Code()+userX.getUsr_Desc()+userX.getUsr_password());
				if(user.getUsr_Code().equals(userX.getUsr_Code())&&user.getUsr_password().equals(userX.getUsr_password())){
					message.setNO(1);
					message.setMessage("");
				}else{
					message.setNO(0);
					message.setMessage("用户与密码不符");
				}
//                jedis.hset(set.getBytes(), usr_code.getBytes(),ObjectUtil.objectToBytes(user));
			    System.out.println("init finished");
			}
		catch (Exception e) {
	         //释放redis对象
		   	  JedisPoolUtil.returnResource(jedis);
		         e.printStackTrace();
		     } finally {
		         //返还到连接池
		   	  System.out.println("release jedis");
		   	  JedisPoolUtil.close(jedis);
		     } 
		return message;
	}
	/**
	 * 
	  * @Title: initUsers 
	  * @Description: 把DB中的USER信息初始化到Redis中
	  * @param     设定文件 
	  * @return void    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public void initUsers(){
		
		Jedis jedis=null;
		ResultSet resultSet=queryUsersFromDB();
		try {
			jedis=JedisPoolUtil.getResource();
			while(resultSet.next()){
				String usr_code= resultSet.getString(1);
				String usr_desc= resultSet.getString(2);
				String usr_passoword= resultSet.getString(3);
                User user=new User(usr_code,usr_desc,usr_passoword);
                jedis.hset(set.getBytes(), usr_code.getBytes(),ObjectUtil.objectToBytes(user));
			    
			}
			System.out.println("init finished");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
	         //释放redis对象
		   	  JedisPoolUtil.returnResource(jedis);
		         e.printStackTrace();
		     } finally {
		         //返还到连接池
		   	  System.out.println("release jedis");
		   	  JedisPoolUtil.close(jedis);
		     } 
		

//
// 		try {
// 			jedis=JedisPoolUtil.getResource();
// 			jedis.del(set);
// 			System.out.println(jedis.sadd(set, "a","b","c"));
// 			System.out.println(jedis.smembers(set));
// 			System.out.println(jedis.srem(set, "d","c"));
// 			System.out.println(jedis.smembers(set));
// 			System.out.println(jedis.scard(set));
//	} catch (Exception e) {
//         //释放redis对象
//   	  JedisPoolUtil.returnResource(jedis);
//         e.printStackTrace();
//     } finally {
//         //返还到连接池
//   	  System.out.println("release jedis");
//   	  JedisPoolUtil.close(jedis);
//     } 
//	
	}
	
	/**
	 * 
	  * @Title: queryUsersFromDB 
	  * @Description: 查询 
	  * @param @return    设定文件 
	  * @return ResultSet    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public ResultSet queryUsersFromDB(){
		ResultSet resultSet=null;

		// TODO Auto-generated method stub
		DBUtils oraDB = new DBUtils();
//        Connection oraCon = oraDB.getConnection();
//        Statement stat = oraDB.getStatement();

		return resultSet=oraDB.executeQuery("Select USR_CODE,USR_DESC,USR_PASSWORD from R5USERS");
//        

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
		UserCacheService userCacheService=new UserCacheService();
		userCacheService.initUsers();
		User user=new User("WANGG", "王刚", "111111");
		Message mes=userCacheService.compareUser(user);
		System.out.println(mes);
		User user1=new User("R5", "R5", "111111");
		Message mes1=userCacheService.compareUser(user1);
		System.out.println(mes1);
	}

}

