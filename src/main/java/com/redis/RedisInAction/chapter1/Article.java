/**
 * 
 */
package com.redis.RedisInAction.chapter1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.redis.RedisInAction.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月3日 下午3:06:49 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class Article {
	/** 文章序列**/
    private static String acticleSeq="acticleSeq";
    /** 超期时间**/
    private static int ONE_WEEK_IN_SECONDS=86400*7*1000;
    /** 投票分数**/
    private static int VOTE_SCORE=432;
    /** 文章**/
    private static String ACTICLE="acticle";
    /** 文章分数**/
    private static String SCORE="score";
    /** 文章时间**/
    private static String TIME="time";
    /** 分隔符**/
    private static String SEPARATOR=":";
    
  
	public Article() {
		Jedis jedis=null;
		try {
			jedis=JedisPoolUtil.getResource();
			//初始化序列
			if(!jedis.exists(acticleSeq)){
				jedis.set(acticleSeq, "1000000");
			}
			System.out.println("acticleSeq now is:"+jedis.get(acticleSeq));
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
      * @Title: createArticle 
      * @Description: 创建文章
      * @param @param title
      * @param @param user    设定文件 
      * @return void    返回类型 
      * @throws 
      * @author 闭门车
     */
	public void createArticle(String title, String user){
		Jedis jedis=null;
		try {
			jedis=JedisPoolUtil.getResource();

		String 	acticleId=jedis.incr(acticleSeq).toString();
		System.out.println("new acticleId is "+acticleId);
		String voted="voted:"+acticleId;
		jedis.sadd(voted, user);
		jedis.expire(voted, ONE_WEEK_IN_SECONDS);//设置超时
		
		long now=System.currentTimeMillis();
		String acticle=ACTICLE+SEPARATOR+acticleId;
		jedis.zincrby(SCORE,VOTE_SCORE,acticle);
		jedis.hincrBy(acticle, "votes", 1);
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("title", title);
		map.put("poster", user);
		map.put("time", String.valueOf(now));
		map.put("votes", String.valueOf(1));
		jedis.hmset(acticle, map);
		jedis.zadd(SCORE, now+VOTE_SCORE,acticle);
		jedis.zadd(TIME,  now,acticle);
      } catch (Exception e) {
          //释放redis对象
    	  JedisPoolUtil.returnResource(jedis);
          e.printStackTrace();
      } finally {
          //返还到连接池
    	  System.out.println("release jedis method is createArticle");
    	  JedisPoolUtil.close(jedis);
      }    
	}
	/**
	 * 
	  * @Title: getArticles 
	  * @Description: 获得前10的文章 
	  * @param     设定文件 
	  * @return void    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public void getArticles(){
		int page=10;
		int start=0;
		int end=start+page-1;
		Jedis jedis=null;
		try {
			jedis=JedisPoolUtil.getResource();
			Set<String> ids=jedis.zrevrange(SCORE, start, end);
            for(String id:ids){
            	Map<String,String> article_data=jedis.hgetAll(id);
            	System.out.println(article_data.get("title"));
            }
      } catch (Exception e) {
          //释放redis对象
    	  JedisPoolUtil.returnBrokenResource(jedis);
          e.printStackTrace();
      } finally {
          //返还到连接池
    	  System.out.println("release jedis");
    	  JedisPoolUtil.returnResource(jedis);
      }       
	}
	/**
	 * 
	  * @Title: updateVoteSet 
	  * @Description: 用户对文章进行投票记录
	  * @param @param acticle
	  * @param @param user    设定文件 
	  * @return void    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public void updateVoteSet(String acticle,String user){
		long cutoff=System.currentTimeMillis()-ONE_WEEK_IN_SECONDS;
		Jedis jedis=null;
		try {
			jedis=JedisPoolUtil.getResource();
			long time=jedis.zscore(TIME, acticle).longValue();
			System.out.println(time +" "+cutoff);
			if(time<cutoff){
				System.out.println("文章已超时");
				return;
			}
		String 	acticleId=acticle.substring(acticle.indexOf(SEPARATOR)+1);
		jedis.sadd("voted:"+acticleId, user);
		jedis.zincrby(SCORE,VOTE_SCORE,acticle);
		jedis.hincrBy(acticle, "votes", 1);
      } catch (Exception e) {
          //释放redis对象
    	  JedisPoolUtil.returnBrokenResource(jedis);
          e.printStackTrace();
      } finally {
          //返还到连接池
    	  System.out.println("release jedis method is updateVoteSet");
    	  JedisPoolUtil.close(jedis);
      }        
	}
	/**
	 * 
	  * @Title: getArticleSet 
	  * @Description: 获得文章集合
	  * @param @return    设定文件 
	  * @return Set<String>    返回类型 
	  * @throws 
	  * @author 闭门车
	 */
	public Set<String> getArticleSet(){
		Set<String> articles=null;
        Jedis jedis=null;
		try {
			jedis=JedisPoolUtil.getResource();
			articles = jedis.keys(ACTICLE+SEPARATOR+"*");
			
		 } catch (Exception e) {
	         //释放redis对象
	   	  JedisPoolUtil.returnResource(jedis);
	         e.printStackTrace();
	     } finally {
	         //返还到连接池
	   	  System.out.println("release jedis");
	   	  JedisPoolUtil.close(jedis);
	     } 
		return articles;
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
		Article article=new Article();
		Random random=new Random();
        String[] users={"李胜","霍帅帅","任东辉","郭冉","余鹏","张强","陈祥","郑国辉","王建新","张小飞","钟德勤","倪英强","刘宏亮","李培","刘晓永","印风超","段炜","刘","俞","李志民","黄鑫山","江","甘欣","翟文锋","向久鸿","蔡远田","张琦","易荣海","高振华","笪标","刘洋","王宇","谢永康","郑海波","陈惠华","刘懿","高朋平","黄建明","张宝地","罗龙","刘国华","林锦有","王旭","王","高利全","许祺泉","张绍强","陈理达","潘毅斌","王宗平","张守杰","陈海斌","张卫东","方建国","杨江伟","韩德志","梁进","林杨","刘长青","詹伟","苗川","蒋龙鑫","池强","杜建平","贾志军","金业朋","李新富","杨高善","杨晓龙","李金","林达渊","张丰","尤群东","林冬冬","夏喜","肖锋","贺养飞","王东","尉晓慧","杨淑萍","王霄凌","陈文","王淑丹","许","付洪青","曹拥军","张常斌","王泽巍","杨风水","孟凡山","刘帅","于长龙","姜山","刘志青","袁清波","孔祥龙","于","张丽","马"};
//        for(int i=0;i<50;i++){
//	    	article.createArticle(users[i]+i, users[i]);
//	    }
        Set<String> articles=article.getArticleSet();
        
		for(String acticle :articles){
			int voteNum= random.nextInt(6);//随机投票
			for(int i=0;i<voteNum;i++){
				int userNum= random.nextInt(99);
				article.updateVoteSet(acticle, users[userNum]);
			}
		}
		System.out.println(articles.size());
	}

}

