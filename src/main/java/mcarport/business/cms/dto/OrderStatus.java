package mcarport.business.cms.dto;

/**
 *  
* @ClassName: OrderStatus
* @Description: TODO(订单的状态 )
* @author ZhangWei
* @date 2015年7月6日 下午3:48:08
*
 */
public enum OrderStatus {

	 NO_PAY("未付款"),
	 NO_PROCESS("未处理"),
	 ORDER_FAILED("订单失败"),
	 WAIT_SERVICE("等待服务"),
	 SERVICE_FAILED("服务失败"),
	 SERVICE_FINISHED("服务完成"),
	 ORDER_CANCEL("订单取消"),
	 ORDER_EXPIRY("订单失效");
	 
	 private String msg;
	 
	 private OrderStatus(String msg){
		 this.msg = msg;
	 }

	 public String getMsg() {
		return msg;
	}
	 
	public static  OrderStatus getStatus(String msg){
		OrderStatus[] rts =	OrderStatus.values();
		for (OrderStatus relationType : rts) {
			if(relationType.getMsg().equals(msg)){
				return relationType;
			}
		}
		System.out.println("状态值传入有误");
		return null;
	}
	 
	 
}
