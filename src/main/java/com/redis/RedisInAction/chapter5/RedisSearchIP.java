/**
 * 
 */
package com.redis.RedisInAction.chapter5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import com.redis.RedisInAction.chapter4.AddPrice;
import com.redis.RedisInAction.util.JedisPoolUtil;

/** 
 * @Description: 读取csv文件中的ip分段信息，然后根据ip查询所在位置，
 *            来源自Redis In Action中的第五章的第三节，实践发现书中的方法有误，按照书中的方法并不能正确的查到ip所在的位置
 *      错误原因：以locId 209为例，csv文件中有多个locId 209的数据，但是最终在Redis中存储的是最后一条数据，这导致了问题的存在
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年3月8日 下午3:52:49 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class RedisSearchIP {
	static  String city_id="ip2cityid:";
	static  String begin_city_id="ip4cityid:";
	/**
	 * 
	  * @Title: importCsv 
	  * @Description: 导入csv文件数据 
	  * @param @param file
	  * @param @return    设定文件 
	  * @return List<String>    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }
	/**
	 * 
	  * @Title: initCityIp 
	  * @Description: 将IP的开始段初始化到redis中
	  * @param     设定文件 
	  * @return void    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public static void initCityIp(){
        Jedis jedis=null;
		List<String> dataList=importCsv(new File("D:/IP/GeoLiteCity-Blocks.csv"));
 		try {
 			jedis=JedisPoolUtil.getResource();
 	        if(dataList!=null && !dataList.isEmpty()){
 	        	int i=0;
 	            for(String data : dataList){
 	            	if(i>=2){
 	                    String[] datas = data.split(",");
 	                    String endIp=datas[1].replaceAll("\"", "");
 	                    String cityId=datas[2].replaceAll("\"", "");
 	                    System.out.println(endIp+"cityid is:"+cityId);
 	                   jedis.zadd(city_id, Double.valueOf(endIp), cityId);
 	            	}
 	                i++;
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
	public static void initCityIpRowNum(){
        Jedis jedis=null;
		List<String> dataList=importCsv(new File("D:/IP/GeoLiteCity-Blocks.csv"));
 		try {
 			jedis=JedisPoolUtil.getResource();
 	        if(dataList!=null && !dataList.isEmpty()){
 	        	int i=0;
 	            for(String data : dataList){
 	            	if(i>=2){
 	                    String[] datas = data.split(",");
 	                    String endIp=datas[1].replaceAll("\"", "");
 	                    String cityId=datas[2].replaceAll("\"", "");
 	                    System.out.println(endIp+"cityid is:"+cityId);
 	                   jedis.zadd(city_id, Double.valueOf(endIp), cityId+"_"+i);
 	            	}
 	                i++;
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
	
	public static void initCityBeginIp(){
        Jedis jedis=null;
		List<String> dataList=importCsv(new File("D:/IP/GeoLiteCity-Blocks.csv"));
 		try {
 			jedis=JedisPoolUtil.getResource();
 	        if(dataList!=null && !dataList.isEmpty()){
 	        	int i=0;
 	            for(String data : dataList){
 	            	if(i>=2){
 	                    String[] datas = data.split(",");
 	                    String beginIp=datas[0].replaceAll("\"", "");
 	                    String cityId=datas[2].replaceAll("\"", "");
 	                    System.out.println(beginIp+"cityid is:"+cityId);
 	                   jedis.zadd(begin_city_id, Double.valueOf(beginIp), cityId);
 	            	}
 	                i++;
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
	  * @Title: initCityBeginIpAddRowNum 
	  * @Description: 初始化IP信息 ,member里加入行信息
	  * @param     设定文件 
	  * @return void    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public static void initCityBeginIpAddRowNum(){
        Jedis jedis=null;
		List<String> dataList=importCsv(new File("D:/IP/GeoLiteCity-Blocks.csv"));
 		try {
 			jedis=JedisPoolUtil.getResource();
 	        if(dataList!=null && !dataList.isEmpty()){
 	        	int i=0;
 	            for(String data : dataList){
 	            	if(i>=2){
 	                    String[] datas = data.split(",");
 	                    String beginIp=datas[0].replaceAll("\"", "");
 	                    String cityId=datas[2].replaceAll("\"", "");
 	                    System.out.println(beginIp+"cityid is:"+cityId);
 	                   jedis.zadd(begin_city_id, Double.valueOf(beginIp), cityId+"_"+i);
 	            	}
 	                i++;
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
	  * @Title: getIPScore 
	  * @Description: 获得ip对应的分值
	  * @param @param IP
	  * @param @return    设定文件 
	  * @return double    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public static double getIPScore(String IP){
		double result=0;
		String[] IPs=IP.split("\\.");
//		int i=0;
		int posi=IPs.length;
		for(int i=0;i<IPs.length;i++){
			result+=Double.valueOf(IPs[posi-i-1])*Math.pow(256, i);
		}
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		System.out.println("p score is: "+nf.format(result));   
		return result;
	}
	
	static void validateProblem(){
		String zset="zsetPro";
        Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			jedis.zadd(zset, 123, "peter");
 			jedis.zadd(zset, 129, "Tom");
 			jedis.zadd(zset, 137, "peter");
 			System.out.println(jedis.zrange(zset, 0, -1));//结果显示只有两个人的 分值
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
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 * @author 闭门车  
	 */
	public static void main(String[] args) {
		initCityBeginIpAddRowNum();

		double IPScore=getIPScore("1.180.235.39");

//        double IPScore=getIPScore("60.247.9.230");
        Jedis jedis=null;
 		try {
 			jedis=JedisPoolUtil.getResource();
 			System.out.println(IPScore);
 			Set<String> result=jedis.zrevrangeByScore(begin_city_id, IPScore, 0,0,2);
// 			Set<String> result=jedis.zrevrangeByScore(city_id, IPScore, 0, 0, 4);//错误的想法
 			System.out.println(result);
 		} catch (Exception e) {
	         //释放redis对象
	   	  JedisPoolUtil.returnResource(jedis);
	         e.printStackTrace();
	     } finally {
	         //返还到连接池
	   	  System.out.println("release jedis");
	   	  JedisPoolUtil.close(jedis);
	     } 
	
//		List<String> dataList=importCsv(new File("D:/IP/GeoLiteCity-Blocks.csv"));
//        if(dataList!=null && !dataList.isEmpty()){
//        	int i=0;
//            for(String data : dataList){
//            	if(i>=2){
//                    String[] datas = data.split(",");
//                    String endIp=datas[1].replaceAll("\"", "");
//                    System.out.println(endIp);
//                    System.out.println(datas[0]+" is:"+datas[1]+datas[2]);
//                    for(String d:datas){
//                    	System.out.println(d);
//                    }
//            	}
//                i++;
////                System.out.println(data);
//            }
//        }
	}

}

