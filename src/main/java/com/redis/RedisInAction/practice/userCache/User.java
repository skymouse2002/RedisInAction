/**
 * 
 */
package com.redis.RedisInAction.practice.userCache;

import java.io.Serializable;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2017年2月23日 下午12:26:01 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class User implements Serializable {
    
/**
	 * 
	 */
	private static final long serialVersionUID = -6272729766012182219L;

	private String usr_Code;
	public User() {
	}
	public User(String usr_code, String usr_desc, String usr_passoword) {
		this.usr_Code=usr_code;
		this.usr_Desc=usr_desc;
		this.usr_password=usr_passoword;
	}
	public String getUsr_Code() {
		return usr_Code;
	}
	public void setUsr_Code(String usr_Code) {
		this.usr_Code = usr_Code;
	}
	private String usr_Desc;
	private String usr_password;
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
	public String getUsr_Desc() {
		return usr_Desc;
	}
	public void setUsr_Desc(String usr_Desc) {
		this.usr_Desc = usr_Desc;
	}
	public String getUsr_password() {
		return usr_password;
	}
	public void setUsr_password(String usr_password) {
		this.usr_password = usr_password;
	}

}

