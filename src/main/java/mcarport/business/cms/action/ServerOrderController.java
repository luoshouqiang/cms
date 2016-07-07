package mcarport.business.cms.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mcarport.business.cms.dto.ManagerInfo;
import mcarport.business.cms.dto.OrderStatus;
import mcarport.business.cms.dto.OrderType;
import mcarport.business.cms.dto.Page;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.dto.SearchBean;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;
import mcarport.business.cms.utils.ExcelUtil;
import mcarport.business.cms.utils.OrderUtils;

import org.hibernate.loader.custom.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

/**
 *  服务订单 对象 
 *  	
* @ClassName: ServerOrderController
* @Description: TODO(用于对服务订单的维护)
* @author ZhangWei
* @date 2015年7月6日 下午12:48:11
*
 */
@Controller 
@RequestMapping("/serverOrder")  
public class ServerOrderController {
	
	private static final Logger LOG =LoggerFactory.getLogger(ServerOrderController.class);
		
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private UpdateService updateService;
	
	@RequestMapping(value="/getTypes",method=RequestMethod.GET)
	@ResponseBody
	public  List<Map<String, Object>> getTypes(){
		LOG.info("获取所有订单的类型");
		List<Map<String, Object>> lists =  queryService.selectServerOrderTypes();
		List<Map<String, Object>> forResult =new ArrayList<Map<String,Object>>();
		Map<String, Object> row = null;
		if(null!=lists && !lists.isEmpty()){
			for (Map<String, Object> map : lists) {
				for (Entry<String, Object> entry : map.entrySet()) {
					try {
						row = new HashMap<String, Object>();
						OrderType os = OrderType.valueOf(entry.getValue().toString().toUpperCase());
						row.put(entry.getValue().toString(), os.getMsg());
						forResult.add(row);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return forResult;
	}
	
	@RequestMapping(value="/getStatus",method=RequestMethod.GET)
	@ResponseBody
	public  List<Map<String, Object>> getStatus(){
		LOG.info("获取所有订单的状态");
		List<Map<String, Object>> lists = queryService.selectServerOrderStatus();
		List<Map<String, Object>> forResult =new ArrayList<Map<String,Object>>();
		Map<String, Object> row = null;
		if(null!=lists && !lists.isEmpty()){
			for (Map<String, Object> map : lists) {
				for (Entry<String, Object> entry : map.entrySet()) {
					try {
						row = new HashMap<String, Object>();
						OrderStatus os = OrderStatus.valueOf(entry.getValue().toString().toUpperCase());
						row.put(entry.getValue().toString(), os.getMsg());
						forResult.add(row);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return forResult;
	}
	
	/**
	 *  搜索服务订单
	 * @param userName 订单用户名称
	 * @param orderId 订单Id
	 * @param startTime 订单创建的开始时间
	 * @param endTime  订单结束的结束时间
	 * @param parkingCode 车位编号
	 * @param platNo 车牌号
	 * @param orderBy 排序字段
	 * @param direction 方向
	 * @param phone 电话号码
	 * @param servProv 服务组织
	 * @param servProvNo 服务队伍编号
	 * @param type 订单类型
	 * @param status 订单状态
	 * @return 条件下的服务订单列表
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	
	public Page<List<Map<String, Object>>> list(@RequestBody SearchBean bean ,@RequestBody Page<List<Map<String, Object>>> page,
			HttpServletRequest request
			){
		 queryService.selectServerOrder
			(bean.getUserName(), bean.getOrderId(), bean.getStartTime(), bean.getEndTime(),bean.getParkingId()
					, bean.getPlatNo(),
					bean.getPhone(),bean.getServProv(),bean.getServProvNo(),bean.getType(),bean.getStatus(),
					bean.getRoomNo(),bean.getParkingCode(),
					bean.getOrderBy(),bean.getDirection(),page);
		if( !page.getDatas().isEmpty()){
			for (Map<String, Object> map : page.getDatas()) {
				for (Entry<String, Object>  entry : map.entrySet()) {
					if(entry.getKey().equals("created_time")){
						entry.setValue( OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString())));
					}
					if(entry.getKey().equals("serv_start_time")){
						Object obj =  entry.getValue();
						entry.setValue(null== obj?"": OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString())));
					}
					if(entry.getKey().equals("serv_end_time")){
						Object obj =  entry.getValue();
						entry.setValue(null== obj?"": OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString())));
					}
					if(entry.getKey().equals("order_status")){
						Object obj =  entry.getValue();
						entry.setValue(null== obj?"":OrderStatus.valueOf(obj.toString()).getMsg());
					}
				}
			}
		}
		request.getSession().setAttribute("content", page.getDatas());
		return page;
	}
	
	
	
	/**
	 *  搜索服务订单
	 * @param userName 订单用户名称
	 * @param orderId 订单Id
	 * @param startTime 订单创建的开始时间
	 * @param endTime  订单结束的结束时间
	 * @param parkingCode 车位编号
	 * @param platNo 车牌号
	 * @param orderBy 排序字段
	 * @param direction 方向
	 * @param phone 电话号码
	 * @param servProv 服务组织
	 * @param servProvNo 服务队伍编号
	 * @param type 订单类型
	 * @param status 订单状态
	 * @return 条件下的服务订单列表
	 */
	@RequestMapping(value="/outer/list",method=RequestMethod.POST)
	@ResponseBody
	
	public List<Map<String, Object>> listForcbd(@RequestBody SearchBean bean , @RequestBody Page<List<Map<String, Object>>> page,
			HttpServletRequest request
			){
		page.setPageSize(100000);
			queryService.selectServerOrder
			(bean.getUserName(), bean.getOrderId(), bean.getStartTime(), bean.getEndTime(),bean.getParkingId()
					, bean.getPlatNo(),
					bean.getPhone(),bean.getServProv(),bean.getServProvNo(),bean.getType(),bean.getStatus(),
					bean.getRoomNo(),bean.getParkingCode(),
					bean.getOrderBy(),bean.getDirection(),page);
		if( !page.getDatas().isEmpty()){
			for (Map<String, Object> map : page.getDatas()) {
				for (Entry<String, Object>  entry : map.entrySet()) {
					if(entry.getKey().equals("created_time")){
						entry.setValue( OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString())));
					}
					if(entry.getKey().equals("serv_start_time")){
						Object obj =  entry.getValue();
						entry.setValue(null== obj?"": OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString())));
					}
					if(entry.getKey().equals("serv_end_time")){
						Object obj =  entry.getValue();
						entry.setValue(null== obj?"": OrderUtils.toStrDate( OrderUtils.toDate(entry.getValue().toString())));
					}
					if(entry.getKey().equals("order_status")){
						Object obj =  entry.getValue();
						entry.setValue(null== obj?"":OrderStatus.valueOf(obj.toString()).getMsg());
					}
				}
			}
		}
		//request.getSession().setAttribute("content", result);
		return page.getDatas();
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/download",method=RequestMethod.GET)
	public void download(HttpServletRequest request,
			HttpServletResponse response
			){
	 	try {
	 		LOG.info("下载excel");
	 		ManagerInfo mi =  (ManagerInfo)request.getSession().getAttribute("ManagerInfo");
	 		Long mid = mi.getManageId();
	 		
	 		Map<String, Object> manager = queryService.selectManager(mid).get(0);
	 		int status = Integer.parseInt(manager.get("isvalid").toString());// 是否虚拟订单
	 	
	 		
	 		
	 		List<Map<String, Object>> results =	(List<Map<String, Object>>) request.getSession().getAttribute("content");
	 		
	 		if(null!=results && !results.isEmpty()){
	 			List<Map<String, Object>> deleteIndex = new ArrayList<Map<String, Object>>();
	 			for (int i = 0; i < results.size(); i++) {
	 				Map<String, Object> map = 	results.get(i);
	 				Integer isvalid = Integer.parseInt(map.get("isvalid").toString()) ;
	 				if(status==0 && isvalid ==1 ){
	 					deleteIndex.add(map);
	 				}else{
	 					map.remove("serv_start_time");
	 					map.remove("serv_end_time");
	 				}
	 			}
	 			
	 			if(!deleteIndex.isEmpty()){
	 				for (Map<String, Object> map : deleteIndex) {
	 					results.remove(map);
					}
	 			}
	 			
	 		}
	 		ExcelUtil.outputExcel("订单列表", new String[]{"创建时间","订单ID","订单名称","订单类型","订单状态","用户名称","订单价格","车位编号","车牌号","手机","备注信息","小区名称","停车场名称","房号"}, results, response);
	 		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  确认服务单据
	 * @param orderId 订单ID
	 * @param servProv 服务主体 
	 * @param servProvNo 队伍的编号
	 * @param remark 备足信息
	 * @param startTime 开始时间 
	 * @param endTime 结束时间
	 * @param status 操作的状态
	 * @return 操作的状态
	 */
	@RequestMapping(value="/executeOrder",method=RequestMethod.POST)
	@ResponseBody
	public ReturnData<String> executeOrder(String  status, Long orderId,String servProv,Integer servProvNo,String remark,
			String startTime,String endTime
			){
		LOG.info("确认服务单,orderId:"+orderId+",status:"+status+",servProv:"+servProv+",servProv:"+servProv+""
				+ ",remark:"+remark+",startTime:"+startTime+",endTime:"+endTime);
		ReturnData<String> rd = new ReturnData<String>();
		try {
			Map<String, Object>  order = queryService.selectServerOrderById(orderId);
			OrderStatus os = OrderStatus.getStatus(status);
			if(null==order || order.isEmpty() || null==os){
				LOG.info("orderId:"+orderId +"参数有误");
				rd.setCode(ReturnData.Type.PARERROR.getStatus());
				rd.setMsg(ReturnData.Type.PARERROR.getMsg());
				return rd;
			}
			updateService.updateServerOrder(orderId, servProv, servProvNo, remark, startTime, endTime,os.name());
			rd.setCode(ReturnData.Type.SUCCESS.getStatus());
			rd.setMsg(ReturnData.Type.SUCCESS.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(ReturnData.Type.EXCEPTION.getStatus());
			rd.setMsg(ReturnData.Type.EXCEPTION.getMsg());
			
		}
		
		return rd;
		
		
		
	}
	
	
	@RequestMapping(value="/outer/batchGenernateOrders",method=RequestMethod.POST)
	@ResponseBody
	public  ReturnData<Integer> batchGenernateOrders(Integer total,String orderTime,Integer days){ 
		
		ReturnData<Integer> rd = new ReturnData<Integer>();
		try {
			if(null==total || null==days || StringUtils.isNullOrEmpty(orderTime)){
				rd.setCode(201);
				rd.setMsg("参数错误");
				return rd;
			}
			
			updateService.batchAddSerOrders(total, orderTime, days,rd);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("服务器异常");
		}
		return rd;
		
	}

	
	
	
}
