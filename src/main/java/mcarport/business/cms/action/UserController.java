package mcarport.business.cms.action;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mcarport.business.cms.dto.CodeType;
import mcarport.business.cms.dto.RelationType;
import mcarport.business.cms.dto.ReturnCode;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.dto.UserInfo;
import mcarport.business.cms.entity.CheckImg;
import mcarport.business.cms.entity.Keys;
import mcarport.business.cms.entity.MyException;
import mcarport.business.cms.entity.TempImg;
import mcarport.business.cms.entity.VehicleBindParkingException;
import mcarport.business.cms.service.QueryService;
import mcarport.business.cms.service.UpdateService;
import mcarport.business.cms.utils.OrderUtils;
import mcarport.business.cms.utils.Upload;
import mcarport.business.cms.utils.Utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.qiniu.http.Response;

/**
 *  维护用户基本操作的controller
 * @author Administrator
 *
 */
@Controller 
@RequestMapping("/user")  
public class UserController {
		
	private static final Logger LOG =LoggerFactory.getLogger(UserController.class);
	
	private static  final String SUCCESS = "SUCCESS";
	private static  final String FAIL = "FAIL";
	
	
	@Autowired
	private UpdateService updateService;
	
	@Autowired
	private QueryService queryService;
	
	/**
	 *  添加用户
	 * @param user 添加用户的对象
	 * @return
	 */
	@RequestMapping(value="addUser",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnCode addUser(UserInfo user){
		ReturnCode rc = new ReturnCode();
		checkParams(user, rc);
		if(!StringUtils.isNullOrEmpty(rc.getMsg())){
			return rc;
		}
		LOG.info("添加用户："+user.getParkingId() + "," + user.getPhoneNum()+","+user.getUserName());
		Calendar dueDate = OrderUtils.getCalendarByPros(user.getDueCode());
		user.setCodeToDate(new Date(dueDate.getTimeInMillis()));
	    rc =  updateService.addUser(user);
	    return rc.setMsg();
	}

	/**
	 *  用于检查参数的合法性
	 * @param user
	 * @param rc
	 */
	private void checkParams(UserInfo user, ReturnCode rc) {
		if(StringUtils.isNullOrEmpty(user.getUserName()) || StringUtils.isNullOrEmpty(user.getParkingId()) ){
			rc.setCode(CodeType.PARAMERRORS.getStatus());
			rc.setMsg();
			LOG.info(rc.getMsg());
		}
		else if(StringUtils.isNullOrEmpty(user.getPhoneNum())){
			rc.setCode(CodeType.NOTPHONE.getStatus());
			 rc.setMsg();
			 LOG.info(rc.getMsg());
		}
		else if(StringUtils.isNullOrEmpty(user.getParkingId())){
			rc.setCode(CodeType.NOTPARKING.getStatus());
			 rc.setMsg();
			 LOG.info(rc.getMsg());
		}else if (StringUtils.isNullOrEmpty(user.getWay())){
			rc.setCode(201);
			rc.setmsg("参数不对");
			 LOG.info(rc.getMsg());
		}
	}
	
	/**
	 *  如果用户基本信息
	 *   目前仅仅有用户名称
	 * @param user
	 * @return
	 */
	@RequestMapping(value="updateBaseUserName",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnCode updateBaseUserName(UserInfo user){
		ReturnCode rc = new ReturnCode();
		if(user.getUserId()==0 || StringUtils.isNullOrEmpty( user.getUserName())){
			rc.setCode(201);
			rc.setmsg("参数错误");
			return rc;
		}
		try {
			updateService.updateBaseUserInfo(user.getUserId(), user.getUserName());
			rc.setmsg("修改成功");
			rc.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			rc.setCode(500);
			rc.setmsg("服务器异常");
		}
		
		
	    return rc.setMsg();
	}
	
	
	/**
	 *  如果用户已经有车位了，
	 *  还需要为该用户添加车位就会调用这个方法
	 * @param user
	 * @return
	 */
	@RequestMapping(value="updateUser",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnCode updateUser(UserInfo user){
		ReturnCode rc = new ReturnCode();
		checkParams(user, rc);
		if(!StringUtils.isNullOrEmpty(rc.getMsg())){
			return rc;
		}
		System.err.println("way:"+user.getWay());
		LOG.info("修改用户："+user.getParkingId() + "," + user.getPhoneNum()+","+user.getUserName());
		
		Calendar dueDate = OrderUtils.getCalendarByPros(user.getDueCode());
		user.setCodeToDate(new Date(dueDate.getTimeInMillis()));
		rc =  updateService.updateUser(user);
	    return rc.setMsg();
	}

	
	
	/**
	 *  解除用户和车位的绑定关系
	 * @param userInfo
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="delUserParking",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> delUserParking(Long userId, Long parkingId,Long vehicleId){
		ReturnData<String> rd = new ReturnData<String>();
		try {
			if(null==userId || null== parkingId){
				rd.setMsg("参数错误");
				throw new Exception("参数错误");
			}
			LOG.info("解除用户和车位绑定关系:userId："+userId+",parkingId："+parkingId+",vehicleId:"+vehicleId);
			updateService.deleteUserParkingRelation(userId, parkingId,vehicleId);
			LOG.info("解除用户和车位绑定关系成功");
			if(null!=vehicleId && vehicleId !=0L){
				Map<String, Object> parking = queryService.selectParkingByParkingId(parkingId).get(0);
				Long areaId = Long.valueOf( parking.get("area_id").toString());
				Map<String, Object> car =  queryService.selectVehicleById(vehicleId).get(0);
				String platNo = car.get("plat_no").toString();
				Map data = new LinkedHashMap();
				data.put("areaId", String.valueOf(areaId));
				data.put("platNo", String.valueOf(platNo));
				String  unbind = updateService.getUrl() + UpdateService.unbind;
				try {
					Utils.postData(unbind, data);
					LOG.info("下发停车场成功");
					rd.setCode(200);
				} catch (Exception e) {
					e.printStackTrace();
					rd.setCode(500);
					LOG.info("本地修改成功，下发停车场失败");
					rd.setMsg("本地修改成功，下发停车场失败");
				}
			}else{
				LOG.info("没有车辆信息，无需下发");
				rd.setCode(200);
			}
		} catch (Exception e) {
			rd.setCode(500);
			rd.setMsg("解除用户和车位绑定关系失败");
			LOG.info("解除用户和车位绑定关系失败");
			e.printStackTrace();
		}
		return rd;
	}
	
	
	
	/**
	 *  
	 *  修改用户和车位的关系
	 * @param user
	 * @return
	 */
	@RequestMapping(value="updateUserParkingRelation",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})	
	@ResponseBody
	public String  updateUserParkingRelation(
			String way,Long parkingId,Long userId){
		String msg = "";
		try {
			RelationType rt = RelationType.getType(way);
			if(null==rt || null==parkingId || null==userId){
				throw new Exception();
			}
			LOG.info("修改用户和车位的关系，parkingId："+parkingId+",userId:"+userId+"way:"+ rt.name());
			updateService.updateParkingUserRelation(parkingId, userId, rt.name());
			msg = "修改成功";
		} catch (Exception e) {
			msg = "应用异常";
			LOG.info(msg);
		}
		return msg;
	}
	
	@RequestMapping(value="getWYUser",method=RequestMethod.GET)	
	@ResponseBody
	public  ReturnData<List<Map<String,Object>>> getWYUser(Long areaId){
		ReturnData<List<Map<String,Object>>> rd = new ReturnData<List<Map<String,Object>>>();
		
		try {
			rd.setCode(200);
			rd.setData(queryService.selectWYUser(areaId));
		} catch (Exception e) {
			rd.setCode(500);
			rd.setMsg("应用异常");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="updateRoomNum",method=RequestMethod.POST)	
	@ResponseBody
	public  ReturnData<String>  updateRoomNum(Long areaId,Long userId,String roomNum){
		ReturnData<String> rd = new ReturnData<String>();
		if(null==areaId || null== userId || StringUtils.isNullOrEmpty(roomNum)){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		
		try {
			this.updateService.updateUser(userId, areaId, roomNum);
			rd.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("服务器错误");
		}
		
		return rd;
		
	}
	
	
	@RequestMapping(value="upload",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<TempImg> upload(HttpServletRequest request)
			 {
		ReturnData<TempImg> rd = new ReturnData<TempImg>();
		TempImg ti = new TempImg();
		rd.setData(ti);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			String type =  null;
			String url = null;
			List<FileItem> list = (List<FileItem>)upload.parseRequest(request);
			for(FileItem item : list){
				
				if(item.isFormField()){
					if(item.getFieldName().equals("type")){
						type =  item.getString();
					}
				}else{
					String name = item.getName();
					InputStream is = item.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(is, baos);
					name = System.currentTimeMillis()+ name.substring(name.lastIndexOf("."));
					Response resp = Upload.upload(baos.toByteArray(), name);
					System.out.println(resp);
					if(resp.toString().contains("error:null")){
						rd.setCode(200);
						url = Keys.domain+"/"+name;
						ti.setUrl(Keys.getAuth().privateDownloadUrl(url));
					}else{
						rd.setMsg(resp.toString());
						rd.setCode(201);
					}
				}
			}
			if(StringUtils.isNullOrEmpty(type)){
				rd.setCode(201);
				rd.setMsg("参数错误");
				return rd;
			}
			Long id = this.updateService.insertTempVehiclePic(url, type, "NEW");
			ti.setId(id);
		} catch (Exception e1) {
			e1.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		return rd;
	}




	@RequestMapping(value="addVehicle")	
	@ResponseBody
	public ReturnData<String>  addVehicle(CheckImg ci ){
		ReturnData<String>  rd =new  ReturnData<String>();
		if(  null==ci.getUserId()){
			rd.setCode(201);
			rd.setMsg("参数错误");
			return rd;
		}
		try {
			
			this.updateService.addVehicleAndImgs(ci, rd);
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

	@RequestMapping(value="addEctype")	
	@ResponseBody
	public ReturnData<String>  addEctype(Long vehicleId ,String pids){
		ReturnData<String>  rd =new  ReturnData<String>();
	
		try {
			this.updateService.addEctype(vehicleId, pids);
			rd.setCode(200);
		}catch(MyException e){
			e.printStackTrace();
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
		}
		
	    return rd;
	}

	
}
