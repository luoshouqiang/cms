package mcarport.business.cms.utils;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mcarport.business.cms.action.UserController;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	
private static final Logger LOG =LoggerFactory.getLogger(Utils.class);	
	
	public static boolean validaVehicleNo(String platNo) {
		if (StringUtils.isEmpty(platNo)) {
			return false;
		}
		Pattern p = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}(([a-zA-Z_0-9]{5})|([a-zA-Z_0-9]{4}[\u4e00-\u9fa5]{1}))$");
		Matcher matcher = p.matcher(platNo);
		return matcher.matches();

	}
	

	public static void postData(String url, Map<String, String> data) throws RuntimeException{
		if (CollectionUtils.isEmpty(data)) {
			return;
		}
		try {
			HttpClient client = new HttpClient();
			StringBuilder sb=new StringBuilder(url);
			NameValuePair[] dataArray = new NameValuePair[data.size()];
			int i=0;
			for (Map.Entry<String, String> entry : data.entrySet()) {
				
				NameValuePair dataPair = new NameValuePair();
				dataPair.setName(entry.getKey());
				dataPair.setValue(entry.getValue());
				sb.append("/");
				sb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
				dataArray[i++]=dataPair;
			}
			LOG.info(sb.toString());
			GetMethod method = new GetMethod(sb.toString());
//			method.setQueryString(dataArray);
//			method.setRequestBody(dataArray);
			client.executeMethod(method);
			int code=method.getStatusCode();
			LOG.info(code+"");
			LOG.info(method.getResponseBodyAsString());
			if(code!=200){
				throw new RuntimeException("调用异常,url:"+sb.toString()+",code:"+code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); 
		}
	}
	
	public static String objectToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static <T> T jsonToObject(String json, Class<T> classInfo) {
		ObjectMapper mapper = new ObjectMapper();
		T obj = null;
		try {
			obj = mapper.readValue(json, classInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	public static void main(String[] args) {
		/*Map data=new LinkedHashMap();
		data.put("areaId", "1");
		data.put("parkingId", "1");
		data.put("platNo", "川A12355");
		
		Utils.postData("http://172.16.10.248:8080/webserver/rest/changeRel", data);*/
		
		
	}
}
