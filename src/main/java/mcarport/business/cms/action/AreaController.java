package mcarport.business.cms.action;

import java.util.List;
import java.util.Map;

import mcarport.business.cms.dto.AreaInfo;
import mcarport.business.cms.dto.Page;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.service.PinYin;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

/**
 *  
* @ClassName: AreaController
* @Description: TODO(小区维护的对象)
* @author ZhangWei
* @date 2015年7月16日 下午2:10:36
*
 */
@Controller 
@RequestMapping("/area")  
public class AreaController {
	
		
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private UpdateService updateService;
	
	
	
	/**
	 *  获取所有的小区
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public  List<Map<String, Object>> list(){
		return queryService.getAreas();
		
	}
	
	
	/**
	 *  添加小区
	 * @return
	 */
	@RequestMapping(value="AddAreas",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> AddAreas(AreaInfo ai){
		ReturnData<String> rd = new ReturnData<String>();
		
		try {
			if(com.mysql.jdbc.StringUtils.isNullOrEmpty( ai.getAreaName())
					){
				rd.setCode(201);
				rd.setMsg("参数异常");
				return rd;
			}
			updateService.addArea(ai,rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}
	
	/**
	 *  
	 * @param gatewayName 匝道口名称
	 * @param gatewayType 闸口类型
	 * 				1：小区兼停车场进出闸口
					2：停车场进出闸口
	 * @param areaId 小区ID 
	 * @param lotsId 停车场ID
	 * @param entrance 是否是进口
			  		1：是  
					0：否
	 * @param exit    是否是出口
	  				1：是  
					0：否
	 * @return
	 */
	@RequestMapping(value="addGateway",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> addGateway(String  gatewayName,Integer gatewayType ,
				Long areaId,Long lotsId,Integer entrance,Integer exit
			){
		ReturnData<String> rd = new ReturnData<String>();
		try {
			if(com.mysql.jdbc.StringUtils.isNullOrEmpty(gatewayName) || 
					null==gatewayType || null==lotsId || null==entrance || null==exit
					){
				rd.setCode(201);
				rd.setMsg("参数异常");
				return rd;
			}
			
			List<Map<String, Object>> oldGata = queryService.selectGateWayByAreaId(areaId);
			if(null!=oldGata && !oldGata.isEmpty()){
				for (Map<String, Object> map : oldGata) {
					
					if(map.get("gateway_name").toString().equals(gatewayName)){
						rd.setCode(201);
						rd.setMsg("已经存在的名称");
						return rd;
					}
				}
			}
			
			updateService.addGataway(gatewayName, gatewayType, areaId, lotsId, entrance, exit);
			rd.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		return rd;
	}
	
	/**
	 *  查询小区的匝道口
	 * @param areaId
	 * @return
	 */
	@RequestMapping(value="getGateWaysByAreaId",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData< List<Map<String, Object>>> getGateWaysByAreaId(Long areaId){
		ReturnData< List<Map<String, Object>>> rd = new ReturnData<List<Map<String,Object>>>();
		try {
			rd.setData(queryService.selectGateWayByAreaId(areaId));
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		return rd;
	}
	
	/**
	 *  添加停车辆
	 * @return
	 */
	@RequestMapping(value="addParkingLots",method=RequestMethod.POST)	
	@ResponseBody
	public  ReturnData<String> addParkingLots(Long areaId,String lotsName,String desc,Long parent){
		ReturnData<String> rd = new ReturnData<String>();
		try {
			if(null==areaId || com.mysql.jdbc.StringUtils.isNullOrEmpty(lotsName)){
				rd.setCode(201);
				rd.setMsg("参数异常");
				return rd;
			}
			if(!queryService.selectAreaParkinglotsByLotsName(lotsName, areaId)){
				updateService.addParkingPlots(areaId, lotsName,desc,parent);
				rd.setCode(200);
			}else{
				rd.setCode(201);
				rd.setMsg("重复的停车场名称");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}
	
	
	/**
	 *  
	 * @param areaId
	 * @param parkingLotsId
	 * @param parkingFloor
	 * @param parkingMax
	 * @param type
	 *        1:物业
	 *        2：车必达	
	 * @return
	 */
	@RequestMapping(value="AddVirtualParking",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> AddVirtualParking(Long areaId,Long parkingLotsId,Integer parkingFloor,Integer parkingMax,
			Integer type,Integer index
			){
		ReturnData<String> rd = new ReturnData<String>();
		
		try {
			if(null==areaId || null == parkingLotsId || null==parkingFloor || null==parkingMax || null ==type ){
				rd.setCode(201);
				rd.setMsg("参数异常");
				return rd;
			}
			if(!queryService.hasInitAreaParking(areaId, parkingLotsId, parkingFloor,type)){
				updateService.initVirtualAreaParking(areaId, parkingLotsId, parkingFloor,parkingMax,rd,type,index);
				rd.setCode(200);
			}else{
				rd.setCode(201);
				rd.setMsg("已经初始化虚拟车位");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		return rd;
	}
	
	
	/**
	 *  添加小区用户 （物业管理的账号（APP端））
	 *  同时初始化小区
	 * @return
	 */
	@RequestMapping(value="AddUserAccount",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> AddUserAccount(Long areaId, String userName, String loginName,String phone){
		ReturnData<String> rd = new ReturnData<String>();
		
		try {
			if(com.mysql.jdbc.StringUtils.isNullOrEmpty(loginName) ||  
					 com.mysql.jdbc.StringUtils.isNullOrEmpty(phone)  ){
				rd.setCode(201);
				rd.setMsg("参数异常");
				return rd;
			}
			
			if(!queryService.hasInitAPPBaseUser(areaId)){
				updateService.addBaseUser(areaId, userName, loginName, phone,rd);
				rd.setCode(200);
			}else{
				rd.setCode(201);
				rd.setMsg("已经添加过账号（APP账号）");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}
	
	/**
	 *  初始化标准车位
	 * @return
	 */
	@RequestMapping(value="addStandardParking",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> addStandardParking(Long areaId,Long parkingLotsId,
			Integer parkingFloor,Integer index, Integer parkingMax,String prefix){
		ReturnData<String> rd = new ReturnData<String>();
		
		if(null==areaId || null==parkingLotsId || null==parkingFloor ||  null==parkingMax
				|| StringUtils.isNullOrEmpty(prefix) || null== index ){
			rd.setCode(201);
			rd.setMsg("参数异常");
			return rd;
		} 
		prefix += "-";
		try {
			if(!queryService.hasInitAreaParking(areaId, parkingLotsId, null,prefix)){
				updateService.initStrandardAreaParking(areaId, parkingLotsId, parkingFloor, parkingMax,prefix,index);
				rd.setCode(200);
			}else{
				rd.setCode(201);
				rd.setMsg("车位前缀已经存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		return rd;
	}
	
	/**
	 *  获取小区下面的车位
	 * @param areaId
	 * @return
	 */
	@RequestMapping(value="getParking",method=RequestMethod.GET)	
	@ResponseBody
	public  ReturnData<Page<List<Map<String,Object>>>> getParking(Long areaId,Long lotsId,Integer parkingFloor,
			String parkingType,Page<List<Map<String, Object>>> page){
		ReturnData<Page<List<Map<String, Object>>>> rd = new ReturnData<Page<List<Map<String, Object>>>>();
		 try {
			rd.setData(queryService.selectAreaParking(areaId, lotsId, parkingFloor,parkingType,page));
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("服务器异常");
		}
		return rd;
		
	}
	
	/**
	 *  获取小区拼音首字母
	 * @return
	 */
	@RequestMapping(value="getPinYin",method=RequestMethod.POST)	
 	@ResponseBody
	public ReturnData<String> getPinYin(String areaName){
		ReturnData<String> rd = new ReturnData<String>();
		if(com.mysql.jdbc.StringUtils.isNullOrEmpty(areaName)){
			rd.setCode(201);
			rd.setMsg("小区名字为空");
			return  rd;
		}else{//处理名字的特殊字符
			areaName = areaName.replaceAll("\\.", "");
			rd.setData(PinYin.cn2FirstSpell(areaName));
			rd.setCode(200);
		}
		
		return rd;
	}
	
}
