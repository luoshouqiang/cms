package mcarport.business.cms.dto;

public class ReturnData<T> {
	
	
	public enum Type{
		SUCCESS(200,"成功"),EXCEPTION(500,"服务器异常"),PARERROR(201,"参数错误");
		private int status;
		private String msg;
		private Type(int status,String msg){
			this.status = status;
			this.msg = msg;
		}
		public String getMsg() {
			return msg;
		}
		public int getStatus() {
			return status;
		}
	}
	
	private int code;
	
	private T  data;
	
	private String msg ;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
