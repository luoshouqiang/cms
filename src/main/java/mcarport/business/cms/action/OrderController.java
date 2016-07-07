package mcarport.business.cms.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mcarport.business.cms.dto.ManagerInfo;
import mcarport.business.cms.dto.OrderStatus;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;
import mcarport.business.cms.utils.OrderUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

/**
 *  维护订单操作的基本操作
 * @author Administrator
 *
 */
@Controller 
@RequestMapping("/order")  
public class OrderController {
	
	private static final Logger LOG =LoggerFactory.getLogger(OrderController.class);
		
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private UpdateService updateService;
	


	
	/**
	 *  搜索订单
	 * @param request
	 * @param userName 用户名
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param phone 电话号码
	 * @param parkingCode 车位编号 
	 * @param dueStartTime 到期开始时间
	 * @param dueEndTime 到期截止时间
	 * @param orderBy 排序字段
	 * @param direction 排序方向
	 * @return
	 */
	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public  List<Map<String, Object>> list(HttpServletRequest request,String userName,
			String startTime,String endTime,
			String phone,String parkingCode,
			String dueStartTime,String dueEndTime,
			String orderBy,String direction
			){
		if(StringUtils.isNullOrEmpty(direction)){//默认排序的方向
			direction= "ASC";
		}
		ManagerInfo mf =  (ManagerInfo)request.getSession().getAttribute("ManagerInfo");
		LOG.info("搜索订单,userName:"+userName+",startTime"+startTime+",endTime"+endTime+",phone:"+phone+",parkingCode:"+parkingCode);
		List<Map<String, Object>> lists = queryService.selectParkingOrders(mf.getManageId(), userName,startTime,endTime,phone,parkingCode,dueStartTime,dueEndTime, orderBy,direction);
		for (Map<String, Object> map : lists) {
			map.put("created_time", OrderUtils.toStrDate(OrderUtils.toDate(map.get("created_time").toString(),OrderUtils.Pattern.YMDHMS),OrderUtils.Pattern.YMDHMS) );
			if(null!=map.get("end_date")){
				map.put("end_date", OrderUtils.toStrDate(OrderUtils.toDate(map.get("end_date").toString())));
			}else{
				map.put("end_date", "未激活");
			}
			if(null!= map.get("order_status")){
				map.put("order_status",OrderStatus.valueOf(map.get("order_status").toString()).getMsg());
			}
			
		}
		return lists;
	}
	
	
	
	
	
}
