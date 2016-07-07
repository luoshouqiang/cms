package mcarport.business.cms.utils;

import mcarport.business.cms.entity.Keys;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class Upload {
	
	static UploadManager uploadManager = new UploadManager();

	// 简单上传，使用默认策略
	private static String getUpToken0(){
		Auth auth = Keys.getAuth();
	    return auth.uploadToken("cbdt");
	}
	
	public static Response upload(byte[] bytes,String name) throws Exception{
		return uploadManager.put(bytes, name, getUpToken0());
	}
	
	

}
