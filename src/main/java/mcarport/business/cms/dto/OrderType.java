package mcarport.business.cms.dto;

/**
 * 
* @ClassName: OrderType
* @Description: TODO(订单的类型)
* @author ZhangWei
* @date 2015年7月7日 下午4:57:31
*
 */
public enum OrderType {


	WASH("洗车");
	
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	
	private OrderType(String msg){
		this.msg = msg;
	}
	
	
	 
	 
}
