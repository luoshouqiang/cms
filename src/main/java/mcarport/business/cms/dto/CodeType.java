package mcarport.business.cms.dto;

public enum CodeType {
	
	SUCCESS(0,"成功"),ERROR(-1,"应用异常"),NOTPHONE(1,"未输入电话号码"),HAVEPARKING(2,"当前用户已经存在车位"),
	NOTPARKING(3,"不存在的车位"),PARKINGHASBIND(4,"车位已经被占用"),
	PARAMERRORS(-2,"参数异常");
	
	private int status;
	
	private String msg;
	
	CodeType(int status,String msg){
		this.status = status;
		this.msg = msg;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getMsg() {
		return msg;
	}
	
	
	
	
	
	

}
