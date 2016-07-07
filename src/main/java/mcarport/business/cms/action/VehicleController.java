package mcarport.business.cms.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcarport.business.cms.dto.Page;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.entity.CheckImg;
import mcarport.business.cms.entity.Keys;
import mcarport.business.cms.entity.VehicleBindParkingException;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;
import mcarport.business.cms.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

/**
 *  车辆信息维护对象
* @ClassName: VehicleController
* @Description: TODO(这里用一句话描述这个类的作用)
* @author ZhangWei
* @date 2015年8月21日 上午10:46:21
*
 */
@Controller 
@RequestMapping("/vehicle")  
public class VehicleController {
		
	private static final Logger LOG =LoggerFactory.getLogger(VehicleController.class);
	
	private static  final String SUCCESS = "SUCCESS";
	private static  final String FAIL = "FAIL";
	
	
	@Autowired
	private UpdateService updateService;
	
	@Autowired
	private QueryService queryService;
	
	/**
	 *  获取所有的车系
	 * @return
	 */
	@RequestMapping(value="getBrands",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<List<Map<String, Object>>> getBrands( ){
		ReturnData<List<Map<String, Object>>>  rd = new ReturnData<List<Map<String,Object>>>();
		LOG.info("获取所有的车系");
		rd.setData(this.queryService.selectBrand());
	    rd.setCode(200);
	    return rd;
	}
	
	/**
	 *  获取车辆的车系
	 * @return
	 */
	@RequestMapping(value="getSeriesByBrandId",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<List<Map<String, Object>>> getSeriesByBrandId(Long id ){
		ReturnData<List<Map<String, Object>>>  rd = new ReturnData<List<Map<String,Object>>>();
		if(null==id){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		LOG.info("获取车辆的车系");
		rd.setData(this.queryService.selectSeriesByBrandId(id));
	    rd.setCode(200);
	    return rd;
	}
	
	
	/**
	 *  获取车系的车型
	 * @return
	 */
	@RequestMapping(value="getModelBySeriesId",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<List<Map<String, Object>>> getModelBySeriesId(Long id ){
		ReturnData<List<Map<String, Object>>>  rd = new ReturnData<List<Map<String,Object>>>();
		if(null==id){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		LOG.info("获取车辆的车系");
		rd.setData(this.queryService.selectModelBySeriesId(id));
	    rd.setCode(200);
	    return rd;
	}
	
	

	/**
	 *  获取车辆详情
	 * @return
	 */
	@RequestMapping(value="getDetail",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<Map<String, Object>> getDetail(Long id){
		ReturnData<Map<String, Object>>  rd = new ReturnData<Map<String,Object>>();
		if(null==id){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		LOG.info("获取车辆的车系");
		List<Map<String, Object>> datas = this.queryService.selectDetailVehicleById(id);
		if( null==datas || datas.isEmpty()) {
		    rd.setCode(201);
		    rd.setMsg("无车辆信息");
		    return rd;
		}
		Map<String, Object> data = datas.get(0);
		Object url = data.get("vehicle_license_url");	
		if(null!=url){
			data.put("vehicle_license_url", Keys.getAuth().privateDownloadUrl(url.toString()));
		}else{
			data.put("vehicle_license_url", "无");
		}
		rd.setData(data);
	    rd.setCode(200);
	    return rd;
	}
	
	
	
	/**
	 * 
	 * @param userName 用户名称
	 * @param phone 电话号码
	 * @param status 状态
	 * @param vehicleId 车辆ID
	 * @param page 分页对象
	 * @return
	 */
	@RequestMapping(value="getVehiclePics",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<Page<List<Map<String, Object>>>> getVehiclePics(String userName,String phone,
			String status,Long vehicleId,Integer type,
			Page<List<Map<String, Object>>> page){
		ReturnData<Page<List<Map<String, Object>>>>  rd =new  ReturnData<Page<List<Map<String, Object>>>>();
		if(null== type){
			type = 1;
		}
		try {
			if(type==1){ //  查询正本
				this.queryService.selectVehiclePic(userName, phone, status,vehicleId,type, page);
			}else{ // 查询副本
				this.queryService.selectVehicleEctypePic(userName, phone, status, vehicleId, type, page);
			}
			if(!page.getDatas().isEmpty()){
				for (Map<String, Object> map : page.getDatas()) {
					map.put("picurls", getPrivateUrls(map.get("picurls").toString()));
					if(null!=map.get("others")){
						map.put("others", getPrivateUrls(map.get("others").toString()));
					}
				}
			}
			rd.setCode(200);
			rd.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
	    return rd;
	}
	
	
	/**
	 * 
	 * @param userName 用户名称
	 * @param phone 电话号码
	 * @param status 状态
	 * @param vehicleId 车辆ID
	 * @param page 分页对象
	 * @return
	 */
	@RequestMapping(value="getUserPics",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<Page<List<Map<String, Object>>>> getUserPics(String userName,String phone,String status,
			Long userId,
			Page<List<Map<String, Object>>> page){
		ReturnData<Page<List<Map<String, Object>>>>  rd =new  ReturnData<Page<List<Map<String, Object>>>>();
		try {
			this.queryService.selectUserPic(userName, phone, status,userId, page);
			if(!(page.getDatas().isEmpty())){
				for (Map<String, Object> map : page.getDatas()) {
					map.put("picurls", getPrivateUrls(map.get("picurls").toString()));
				}
			}
			rd.setCode(200);
			rd.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
	    return rd;
	}

	private String getPrivateUrls(String urls) {
		String[] arrPcurls =  urls.split(",");
		List<String> list = new ArrayList<String>();
		for (String url : arrPcurls) {
			list.add(Keys.getAuth().privateDownloadUrl(url));
		}
		return  list.toString();
	}
	
	
	
	/**
	 *  审核 物业车辆
	 * @param ci
	 * @return
	 */
	/**
	 * 
	 * @param vehicleId 车辆ID
	 * @param status 状态 （PASS（通过），NOPASS（未通过））
	 * @param urls 图片的URL地址。如果有多张就用,分割传过来
	 * @return
	 */
	@RequestMapping(value="checkPicWithVehicle",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> checkPicWithVehicle(Long  vehicleId,String status,String urls,Integer type ,String comment){
		ReturnData<String>  rd =new  ReturnData<String>();
		if(null==type){
			type = 1;
		}
		try {
			if(null==vehicleId ||StringUtils.isNullOrEmpty(urls) || StringUtils.isNullOrEmpty(status)  ){
				rd.setCode(201);
				rd.setMsg("参数错误");
				return rd;
			}
			if(type==1){
				this.updateService.checkVehicleImgWithVehicle(vehicleId, status, urls,comment);
			}else{
				this.updateService.checkVehicleImgWithVehicleEctype(vehicleId, status, urls,comment);
			}
			rd.setCode(200);
		} catch (VehicleBindParkingException e) {
			e.printStackTrace();
			rd.setCode(201);
			rd.setMsg("当前车辆已经入库");
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
	    return rd;
	}
	
	
	/**
	 *  审核同时上传车辆
	 * @param ci
	 * @return
	 */
	@RequestMapping(value="checkPic",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> checkPic(CheckImg ci ){
		ReturnData<String>  rd =new  ReturnData<String>();
		if( ci.getStatus().equalsIgnoreCase("pass")  && (null==ci.getUserId() 
				 ||
				null== ci.getSweptVolume() || StringUtils.isNullOrEmpty(ci.getIssueDate()) 
				|| StringUtils.isNullOrEmpty(ci.getRegisterDate())) 
				){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		
		if(ci.getStatus().equalsIgnoreCase("pass")  && !Utils.validaVehicleNo(ci.getPlatNo())){
			rd.setCode(201);
			rd.setMsg("车牌格式错误");
			return rd;
		}
		try {
			this.updateService.checkVehicleImg(ci);
			rd.setCode(200);
		} catch (VehicleBindParkingException e) {
			e.printStackTrace();
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
	    return rd;
	}
	
	
	/**
	 * 
	 * @param platNo 车牌号
	 * @return
	 */
	@RequestMapping(value="getVehicleByPlatNo",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<Map<String, Object>> getVehicleByPlatNo(String platNo){
		ReturnData<Map<String, Object>>  rd =new  ReturnData<Map<String, Object>>();
		List<Map<String, Object>> datas =  this.queryService.selectVehicleByPlatNo(platNo);
		if(null!=datas && !datas.isEmpty()){
			rd.setData(datas.get(0));
		}
	    return rd;
	}
	
	/**
	 * 
	 * @param vehicleId 车辆ID
	 * @param type 图片类型
	 * @return
	 */
	@RequestMapping(value="getVehicleOtherPics",method=RequestMethod.GET)	
	@ResponseBody
	public ReturnData<List<Map<String, Object>>> getVehicleByPlatNo(Long vehicleId,String type){
		ReturnData<List<Map<String, Object>>>  rd =new  ReturnData<List<Map<String, Object>>>();
		List<Map<String, Object>> datas =  this.queryService.selectVehicleOtherPics(vehicleId, type);
		for (Map<String, Object> map : datas) {
			map.put("picurl", getPrivateUrls(map.get("picurl").toString()));
		}
		
		rd.setData(datas);	
	    return rd;
	}
	
	
}
