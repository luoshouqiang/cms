package mcarport.business.cms.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mcarport.business.cms.dto.AreaInfo;
import mcarport.business.cms.dto.ManagerInfo;
import mcarport.business.cms.dto.Page;
import mcarport.business.cms.dto.QueryData;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.dto.UserInfo;
import mcarport.business.cms.dto.Vehicle;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;
import mcarport.business.cms.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller 
@RequestMapping("/areacms")  
public class CMSController {
		
	private static final Logger LOG =LoggerFactory.getLogger(CMSController.class);
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private UpdateService updateService;
	
	@RequestMapping(value="list",method=RequestMethod.POST)	
	@ResponseBody
	public Page<List<UserInfo>> queryList(@RequestBody QueryData data,@RequestBody Page<List<UserInfo>> page){
		queryService.fuzzQuery(data,page);
		return page;
		
	}
	
	@RequestMapping(value="login")	
	@ResponseBody
	public ManagerInfo login(@RequestParam String loginName,@RequestParam String password,HttpServletRequest request){		
		HttpSession session = request.getSession(true);
		session.setAttribute("loginName", loginName);
		ManagerInfo manager=queryService.login(loginName, password);
		session.setAttribute("ManagerInfo", manager);
		LOG.info("登录中,loginName:"+loginName+",managerId:"+manager.getManageId());
		return manager;
		
	}
	
	@RequestMapping(value="areaInfo")	
	@ResponseBody
	public List<AreaInfo> areaInfo(@RequestParam long managerId){		
		List<AreaInfo> areaList=queryService.queryAreaInfo(managerId);
		return areaList;
		
	}
	
	@RequestMapping(value="carList")	
	@ResponseBody
	public List<Vehicle> carList(@RequestParam long userId,Long areaId){		
		List<Vehicle> carList=queryService.queryCarList(userId,areaId);
		return carList;
		
	}
	
	@RequestMapping(value="addVehicle")	
	@ResponseBody
	public ReturnData<String>  areaInfo(@RequestParam long userId,@RequestParam String platNo,Double sweptVolume,Long brandId,
			Long seriesId, Long modelId, String manualModel ){
		LOG.info("添加车辆.userId:"+userId+",platNo"+platNo+",sweptVolume:"+sweptVolume);
		ReturnData<String> rd = new ReturnData<String>();
		updateService.addVehicle(userId, platNo,sweptVolume,brandId,seriesId,manualModel,modelId,rd);
		return rd;
		
	}
	
	@RequestMapping(value="updateDueTime")	
	@ResponseBody
	public boolean updateDueDate(@RequestParam long areaId,@RequestParam long parkingId,@RequestParam String dueDate){		
		System.out.println("修改到期时间 paringId:"+parkingId +"....dueDate:"+dueDate);
		return updateService.updateDueDate(areaId,parkingId, dueDate);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@RequestMapping(value="rebind")	
	@ResponseBody
	public ReturnData<String> rebindParkingAndVehicle(@RequestParam long areaId,@RequestParam long vehicleId,@RequestParam long parkingId){		
		ReturnData<String> rd = new ReturnData<String>();
		LOG.info("重新绑定车位和车辆的关系。vehicleId："+vehicleId+",parkingId"+parkingId+",areaId"+areaId);
		String oldPlatNo = "";
		List<Map<String, Object>> resultList =	queryService.selectParkingVehicleRelByParkingId(parkingId);
		if (!CollectionUtils.isEmpty(resultList)) {
			oldPlatNo = (String) resultList.get(0).get("plat_no");
		}
		if(updateService.rebindParkingAndVehicle(areaId,vehicleId, parkingId,rd) && rd.getCode()==200){
			if(vehicleId == 0L){ // 取消绑定关系
			}else{
				try {
					Map data = new LinkedHashMap();
					data.put("areaId", String.valueOf(areaId));
					data.put("parkingId", String.valueOf(parkingId));
					data.put("platNo", com.mysql.jdbc.StringUtils.isNullOrEmpty(oldPlatNo)?"aa":oldPlatNo);
					String bindUrl = updateService.getUrl() + UpdateService.updateRebindUrl;
					Utils.postData(bindUrl, data);
				} catch (RuntimeException e) {
					e.printStackTrace();
					rd.setCode(500);
					rd.setMsg("本地成功，下发停车场失败");
				}
				
			}
		}
		
		
		return rd;
	}
	
	@RequestMapping(value="getAreasByUserId")	
	@ResponseBody
	public List<Map<String, Object>> getAreasByUserId(Long userId){
		LOG.info("获取用户的小区信息:"+userId);
		return queryService.queryAreaByUserId(userId);
	}
	
	
	
}
