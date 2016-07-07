package mcarport.business.cms.dto;
public final class ReturnCode{
	private int code;
	private Long userId;
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	
	public void setmsg(String msg) {
		this.msg = msg;
	}
	
	public ReturnCode setMsg() {
		switch (code) {
		case 0:
			msg = CodeType.SUCCESS.getMsg();
			break;
		case 1:
			msg =  CodeType.NOTPHONE.getMsg();
			break;
		case 2:
			msg =  CodeType.HAVEPARKING.getMsg();
			break;
		case 3:
			msg =  CodeType.NOTPARKING.getMsg();
			break;
		case 4:
			msg =   CodeType.PARKINGHASBIND.getMsg();
			break;
		case -2:
			msg = CodeType.PARAMERRORS.getMsg();
			break;
		default:
			msg =  CodeType.ERROR.getMsg();
			break;
		}
		return this;
		
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}