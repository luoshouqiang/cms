package mcarport.business.cms.entity;

import com.qiniu.util.Auth;

public class Keys {
	
	
	public static  final String domain = "http://7xli7w.com2.z0.glb.qiniucdn.com";
	
	public  static  final String AK= "sxTJUBWiWxcHn7rBXbmlPfVSiVRJrvEUFez28C3S";
	
	public  static  final String SK= "Vz64r3wLrxZPTRZ6_DUScSCpfwwhk-e4lE6dHzrc";
	
	
	public static Auth getAuth(){
		return  Auth.create(AK, SK);
	}
	
}
