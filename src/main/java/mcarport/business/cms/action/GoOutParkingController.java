package mcarport.business.cms.action;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.utils.OrderUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @ClassName: GoOutParkingController
* @Description: TODO(车辆出场的维护对象)
* @author ZhangWei
* @date 2015年7月9日 下午4:37:51
*
 */
@Controller 
@RequestMapping("/goOutParking")  
public class GoOutParkingController {
	
	private static final Logger LOG =LoggerFactory.getLogger(GoOutParkingController.class);
		
	@Autowired
	private QueryService queryService;
	
	
	/**
	 * 搜索临时出场订单
	 * @param startTime 开始时间
	 * @param endTime  结束时间
	 * @param platNo 车牌号
	 * @param payType 支付类型
	 * @param betweenTime 停留时常
	 * @return
	 */
	@RequestMapping(value="/temp/list",method=RequestMethod.GET)
	@ResponseBody
	public  List<Map<String, Object>> list(String startTime,String endTime,String platNo,Integer payType,
			Integer betweenTime
			){
		List<Map<String, Object>> result = queryService.selectGoOutOrders(startTime, endTime, platNo, payType, betweenTime);
			if(null!=result && !result.isEmpty()){
				for (Map<String, Object> map : result) {
					for (Entry<String, Object>  entry : map.entrySet()) {
						if(entry.getKey().equals("coming_time")|| entry.getKey().equals("go_out_time")){
							entry.setValue( OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString(),OrderUtils.Pattern.YMDHMS),OrderUtils.Pattern.YMDHMS));
						}
					}
				}
			}
		return result;
		
	}
	
	
	
}
