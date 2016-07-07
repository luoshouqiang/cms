package mcarport.business.cms.push;

import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Configuration {

	public static String distance;
	
	public static Properties allProperties=new Properties();
	static {

		try {
			Resource resource = new ClassPathResource("/push.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			props = PropertiesLoaderUtils.loadProperties(resource);
			mergeProperties(props);
			
			props = PropertiesLoaderUtils.loadProperties(resource);
			mergeProperties(props);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public static String getProperty(String key){
		return allProperties.getProperty(key, "");
	}
	private static void mergeProperties(Properties props){
		if(props==null){
			return;
		}
		allProperties.putAll(props);
	}
	

}
