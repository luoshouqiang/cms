package mcarport.business.cms.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mcarport.business.cms.dto.RelationType;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;

import org.hibernate.loader.custom.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

/**
 *  维护用户基本操作的controller
 * @author Administrator
 *
 */
@Controller 
@RequestMapping("/parking")  
public class ParkingController {
		
	private static final Logger LOG =LoggerFactory.getLogger(ParkingController.class);
	
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private UpdateService updateService;
	
	/**
	 *  获取用户租赁的关系
	 * @return
	 */
	@RequestMapping(value="relationWay",method=RequestMethod.GET)	
	@ResponseBody
	public Object[] relationWay( ){
		List<String>  ways = new ArrayList<String>();
		for (Map<String, Object> map : queryService.queryRelationWay()) {
			for (Entry<String, Object> entry : map.entrySet()) {
					RelationType rt = RelationType.valueOf(entry.getValue().toString());
					ways.add(rt.getStatus());
			}
		}
		LOG.info(" 获取用户租赁的关系");
		return ways.toArray();
	}
	

	
	
	/**
	 *  添加车位
	 * @param areaId 小区ID
	 * @param parkingLotsId 停车场ID
	 * @param parkingType 车位类型
	 * @param parkingFloor 楼层
	 * @param parkingLocation 位置
	 * @param parkingSpace 
	 * @param parkingCode
	 * @return
	 */
	@RequestMapping(value="addAreaParking",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> addAreaParking(Long areaId,Long parkingLotsId,Long parkingType,Integer parkingFloor,
			String parkingLocation,String parkingSpace,String parkingCode
			){
		ReturnData<String> rd = new ReturnData<String>();
		
		if(null==areaId || null==parkingLotsId || null==parkingType || null==parkingFloor|| null==parkingCode){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		
		int status = updateService.addAreaParking(areaId, parkingLotsId, parkingCode, parkingType, parkingFloor, parkingLocation, parkingSpace);
		if(status==1){
			rd.setCode(300);
			rd.setMsg("当前车位编码编号已经存在");
		}else if(status==2){
			rd.setCode(300);
			rd.setMsg("不支持当前楼层");
		}else{
			rd.setCode(200);
		}
		
		return rd;
	}
	
	
	@RequestMapping(value="getAreaPlots",method=RequestMethod.GET)	
	@ResponseBody
	public List<Map<String, Object>> getAreaPlots(Long areaId
			){
		return queryService.selectAreaParkinglotsByAreaId(areaId);
	}
	
	/**
	 *  修改车位类型
	 * @param parkingId
	 * @param space
	 * @return
	 */
	@RequestMapping(value="updatePakringType",method=RequestMethod.POST)	
	@ResponseBody
	public  ReturnData<String> updatePakringType(Long parkingId,String  space){
		ReturnData<String> rd = new ReturnData<String>();
		try {
			updateService.updateAreaPakring(parkingId, space);
			rd.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setMsg("应用异常");
			rd.setCode(500);
		}
		
		return rd;
	}
	
	
	/**
	 *  
	 *   重命名 车位名称
	 *  
	 * @param start 起止编号
	 * @param end 截止编号
	 * @param prefix 前缀
	 * @param rePrefix 需要替换的前缀
	 * @param areaId 小区ID
	 * @param lotsId 停车场ID
	 */
	@RequestMapping(value="renameParking",method=RequestMethod.POST)	
	@ResponseBody
	public  ReturnData<List<Map<String, Object>>>  renameParking(Integer start,Integer end,String prefix,String rePrefix,
			Long areaId,Long parkingLotsId
			){
		
		ReturnData<List<Map<String, Object>>> rd = new ReturnData<List<Map<String, Object>>>();
		
		if(null==start  || null==end || StringUtils.isNullOrEmpty(prefix)||
				StringUtils.isNullOrEmpty(rePrefix) || null==areaId
				){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		
		prefix = prefix.toUpperCase();
		rePrefix = rePrefix.toUpperCase();
		
		
		List<Map<String, Object>>  parkings = queryService.selectParkingTypeIsLOAN(areaId, parkingLotsId, prefix,start,end);
		
		if(null==parkings || parkings.isEmpty()){
			rd.setCode(201);
			rd.setMsg("没有符合条件的车位");
			return rd;
		}
		
		List<Map<String, Object>>  tragetParking = queryService.selectParking(areaId, parkingLotsId, rePrefix,null);
		
		if(null!=tragetParking && !tragetParking.isEmpty()){
			rd.setCode(201);
			rd.setMsg("需要修改的前缀已经存在");
			return rd;
		}
		
		
		try {
			rd.setData(updateService.renameOfParking(parkings, prefix, rePrefix,areaId,parkingLotsId));
			rd.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		return rd;
	
}


}
