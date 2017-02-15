/**
 * 
 */
package cn.liuchg.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 
 * @author  作者:闭门车 E-mail: hao_3602g@163.com
 * @date 创建时间：2016年12月30日 下午4:18:14 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
/**
 * @author Administrator
 *
 */
public class Configuration extends Properties {
	 private static final long serialVersionUID = 50440463580273222L;
	 
	    private static Configuration instance = null;
	 
	    public static synchronized Configuration getInstance() {
	        if (instance == null) {
	            instance = new Configuration();
	        }
	        return instance;
	    }
	 
	    public String getProperty(String key, String defaultValue) {
	        String val = getProperty(key);
	        return (val == null || val.isEmpty()) ? defaultValue : val;
	    }
	 
	    public String getString(String name, String defaultValue) {
	        return this.getProperty(name, defaultValue);
	    }
	 
	    public int getInt(String name, int defaultValue) {
	        String val = this.getProperty(name);
	        return (val == null || val.isEmpty()) ? defaultValue : Integer.parseInt(val);
	    }
	 
	    public long getLong(String name, long defaultValue) {
	        String val = this.getProperty(name);
	        return (val == null || val.isEmpty()) ? defaultValue : Integer.parseInt(val);
	    }
	 
	    public float getFloat(String name, float defaultValue) {
	        String val = this.getProperty(name);
	        return (val == null || val.isEmpty()) ? defaultValue : Float.parseFloat(val);
	    }
	 
	    public double getDouble(String name, double defaultValue) {
	        String val = this.getProperty(name);
	        return (val == null || val.isEmpty()) ? defaultValue : Double.parseDouble(val);
	    }
	 
	    public byte getByte(String name, byte defaultValue) {
	        String val = this.getProperty(name);
	        return (val == null || val.isEmpty()) ? defaultValue : Byte.parseByte(val);
	    }
	 
	    public Configuration() {
	        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("config.xml");
	        try {
	            this.loadFromXML(in);
	            in.close();
	        } catch (IOException e) {
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
  
	}

}

