package mcarport.business.cms.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import mcarport.business.cms.dto.AreaInfo;
import mcarport.business.cms.dto.ReturnCode;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.dto.UserInfo;
import mcarport.business.cms.entity.CheckImg;
import mcarport.business.cms.entity.MyException;
import mcarport.business.cms.entity.VehicleBindParkingException;
import mcarport.business.cms.utils.OrderUtils;
import mcarport.business.cms.utils.OrderUtils.Pattern;
import mcarport.business.cms.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mysql.jdbc.Statement;

@Service
@Transactional(rollbackOn=Exception.class)
public class UpdateService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	private QueryService queryService;

	private String url;

	public static final String updateDueTimeUrl = "/updatedExpiryDate";

	public static final String updateRebindUrl = "/changeRel";
	
	public static final String unbind = "/unbind";

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addVehicle(final long userId, final String platNo,final Double sweptVolume,ReturnData<String> rd) {
		try {
			if (!Utils.validaVehicleNo(platNo)){
				rd.setCode(201);
				rd.setMsg("车牌格式不正确");
				return false;
			}
			int status =jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call PROC_add_vehicle(?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setLong(1, userId);// 设置输入参数的值
					cs.setBigDecimal(2, new BigDecimal(sweptVolume));
					cs.setString(3, platNo);
					cs.registerOutParameter(4, Types.INTEGER);
					return cs;
				}
			}, new CallableStatementCallback<Integer>() {
				public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					cs.execute();
					return cs.getInt(4);
				}
			});
			
			if(status==1){
				rd.setCode(201);
				rd.setMsg("车辆已经绑定了车位，请先解绑车辆和车位的关系");
			}else if(status==3){
				rd.setCode(200);
				rd.setMsg("重新绑定了用户和车辆的关系");
			}else{
				rd.setCode(200);
				rd.setMsg("新增关系");
			}
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
			return false;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addVehicle(final long userId, final String platNo,final Double sweptVolume,
			final Long brandId,final Long seriesId,final String manualModel,
			final Long modelId,
			ReturnData<String> rd) {
		try {
			if (!Utils.validaVehicleNo(platNo)){
				rd.setCode(201);
				rd.setMsg("车牌格式不正确");
				return false;
			}
			int status = jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call PROC_add_vehicle(?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setLong(1, userId);// 设置输入参数的值
					cs.setBigDecimal(2, new BigDecimal(sweptVolume));
					cs.setString(3, platNo);
					cs.setLong(4, brandId);
					cs.setLong(5, seriesId);
					cs.setString(6, manualModel);
					cs.setLong(7, modelId);
					cs.registerOutParameter(8, Types.INTEGER);
					return cs;
				}
			}, new CallableStatementCallback<Integer>() {
				public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					cs.execute();
					return cs.getInt(8);
				}
			});
			System.out.println(status);
			if(status==1){
				rd.setCode(201);
				rd.setMsg("车辆已经绑定了车位，请先解绑车辆和车位的关系");
			}else if(status==3){
				rd.setCode(200);
				rd.setMsg("重新绑定了用户和车辆的关系");
			}else{
				rd.setCode(200);
				rd.setMsg("新增关系");
			}
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			rd.setCode(500);
			rd.setMsg("应用异常");
			return false;
		}
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addVehicle(final CheckImg ci,ReturnData<String> rd) throws Exception{
			if (!Utils.validaVehicleNo(ci.getPlatNo())){
				rd.setCode(201);
				rd.setMsg("车牌格式不正确");
				return ;
			}
			int status = jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call PROC_add_vehicle(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setLong(1, ci.getUserId());// 设置输入参数的值
					cs.setBigDecimal(2, new BigDecimal(ci.getSweptVolume()));
					cs.setString(3, ci.getPlatNo());
					cs.setLong(4, ci.getBrandId());
					cs.setLong(5, ci.getSeriesId());
					cs.setString(6, ci.getManualModel());
					cs.setLong(7, ci.getModelId());
					cs.setString(8, ci.getVehicleType());
					cs.setString(9, ci.getOwner());
					cs.setString(10, ci.getUseCharacter());
					cs.setString(11, ci.getAddress());
					cs.setString(12, ci.getModel());
					cs.setString(13, ci.getVin());
					cs.setString(14, ci.getEngineNo());
					cs.setDate(15, new  Date(    OrderUtils.toDate(ci.getRegisterDate()).getTimeInMillis()));
					cs.setDate(16, new  Date(    OrderUtils.toDate(ci.getIssueDate()).getTimeInMillis()));
					cs.setString(17, ci.getStatus().toUpperCase());
					cs.registerOutParameter(18, Types.INTEGER);
					return cs;
				}
			}, new CallableStatementCallback<Integer>() {
				public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					cs.execute();
					return cs.getInt(18);
				}
			});
			System.out.println(status);
			if(status==1){
				rd.setCode(201);
				rd.setMsg("车辆已经绑定了车位，请先解绑车辆和车位的关系");
			}else if(status==3){
				rd.setCode(200);
				rd.setMsg("重新绑定了用户和车辆的关系");
				Map<String, Object> data = this.queryService.selectVehicleByPlatNo(ci.getPlatNo()).get(0);
				String newStatus = ci.getStatus();
				if( null==data.get("check_status") || data.get("check_status").toString().equalsIgnoreCase("old")){
					if(!newStatus.equalsIgnoreCase("PASS")){
						newStatus = "OLD";
					}
				}
				String vid = data.get("vehicle_id").toString();
				this.jdbcTemplate.update(" update base_vehicle set check_status = ?  where vehicle_id = ? ",newStatus, Long.valueOf(vid));
				
			}else{
				rd.setCode(200);
				rd.setMsg("新增关系");
				Map<String, Object> data = this.queryService.selectVehicleByPlatNo(ci.getPlatNo()).get(0);
				String vid = data.get("vehicle_id").toString();
				this.jdbcTemplate.update(" update base_vehicle set check_status = ?  where vehicle_id = ? ",ci.getStatus().toUpperCase(), Long.valueOf(vid));
			}
			
	}

	
	
	/** 
	 * 添加行驶证副本
	 */
	public  void addEctype(Long vehicleId,String pids) throws MyException,Exception{
		List<Map<String, Object>> data = this.queryService.selectVehicleById(vehicleId);
		if(null!=data && !data.isEmpty() ){
			String status = data.get(0).get("check_status").toString();
			if(!status.equalsIgnoreCase("pass")){
				throw new MyException("车辆行驶证审核未通过");
			}
		}else{
			throw new Exception("错误的车辆ID，没有该条记录!");
		}
		// 删除上一次提交未审核的图片
		this.jdbcTemplate.update(" delete from  base_vehicle_pic where pictype != 'VL_ORI'   AND picstatus = 'NEW' and vehicle_id = ? ",vehicleId);
		String[] arrPid = pids.split(",");
		for (String pid : arrPid) {
			updatePicOwner(Long.valueOf(pid), vehicleId);
		}
		this.jdbcTemplate.update("   update base_vehicle set =  check_insure_status = 'NEW'  where vehicle_id = ?  ",vehicleId);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean updateDueDate(long areaId, final long parkingId, final String dueDate) {
		try {
			if (parkingId == 0 || StringUtils.isEmpty(dueDate))
				return false;

			jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call PROC_update_duedate(?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setLong(1, parkingId);
					cs.setString(2, dueDate);
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					cs.execute();
					return null;
				}
			});
			Map data = new LinkedHashMap();
			data.put("areaId", String.valueOf(areaId));
			data.put("parkingId", String.valueOf(parkingId));
			String bindUrl = url + updateDueTimeUrl;
			Utils.postData(bindUrl, data);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean rebindParkingAndVehicle(final long areaId, final long vehicleId, final long parkingId, ReturnData<String> rd) {
		try {
			if (parkingId == 0){
				rd.setCode(201);
				rd.setMsg("参数错误");
				return false;
			}
			
			String oldPlatNo = "";
			String sql = "select plat_no from base_vehicle v,parking_vehicle_rel pvr where v.vehicle_id=pvr.vehicle_id and pvr.parking_id=?";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, parkingId);
			if (!CollectionUtils.isEmpty(resultList)) {
				oldPlatNo = (String) resultList.get(0).get("plat_no");
			}
			
			if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(oldPlatNo)){
				List<Map<String, Object>> hasBind = jdbcTemplate.queryForList("SELECT  * FROM  parking_realtime_record prr WHERE  prr.plat_no = ? AND prr.isvalid = 0", oldPlatNo);
				if(  null!=hasBind && !hasBind.isEmpty()){
					rd.setCode(202);
					rd.setMsg("已经入库的车辆不可以修改");
					return false;
				}
			}
			
			
			if(vehicleId == 0L){ // 取消绑定关系
				/*jdbcTemplate.update(" DELETE FROM  parking_vehicle_rel WHERE parking_id = ?",parkingId);
				Map data = new LinkedHashMap();
				data.put("areaId", String.valueOf(areaId));
				data.put("platNo", oldPlatNo);
				String bindUrl = url + unbind;
				Utils.postData(bindUrl, data);*/
			//	List<Map<String, Object>> hasBind = jdbcTemplate.queryForList("SELECT  * FROM  parking_realtime_record prr WHERE  prr.plat_no = ? AND prr.isvalid = 0", oldPlatNo);
//				if(null==hasBind || hasBind.isEmpty()){
					jdbcTemplate.update(" DELETE FROM  parking_vehicle_rel WHERE parking_id = ?   ",parkingId);
					Map data = new LinkedHashMap();
					data.put("areaId", String.valueOf(areaId));
					data.put("platNo", oldPlatNo);
					String bindUrl = url + unbind;
					try {
						Utils.postData(bindUrl, data);
						rd.setCode(200);
					} catch (Exception e) {
						e.printStackTrace();
						rd.setCode(500);
						rd.setMsg("下发停车场失败");
						throw new Exception("下发停车场失败");
					}
//				}else{
					
//				}
			}else{
				jdbcTemplate.execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						String storedProc = "{call PROC_add_parking(?,?,?,?)}";// 调用的sql
						CallableStatement cs = con.prepareCall(storedProc);
						cs.setLong(1, vehicleId);
						cs.setLong(2, parkingId);
						cs.setLong(3, areaId);
						cs.registerOutParameter(4,Types.VARCHAR );
						return cs;
					}
				}, new CallableStatementCallback() {
					public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
						cs.execute();
						return null;
					}
				});
			/*	Map data = new LinkedHashMap();
				data.put("areaId", String.valueOf(areaId));
				data.put("parkingId", String.valueOf(parkingId));
				data.put("platNo", com.mysql.jdbc.StringUtils.isNullOrEmpty(oldPlatNo)?"aa":oldPlatNo);
				String bindUrl = url + updateRebindUrl;
				Utils.postData(bindUrl, data);*/
				rd.setCode(200);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			rd.setCode(500);
			rd.setMsg("服务器异常");
			return false;
		}
	}
	
	/* 老版本修改车牌
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean rebindParkingAndVehicle(long areaId, final long vehicleId, final long parkingId) {
		try {
			if (vehicleId == 0 || parkingId == 0)
				return false;
			String oldPlatNo = "";
			String sql = "select plat_no from base_vehicle v,parking_vehicle_rel pvr where v.vehicle_id=pvr.vehicle_id and pvr.parking_id=?";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, parkingId);
			if (!CollectionUtils.isEmpty(resultList)) {
				oldPlatNo = (String) resultList.get(0).get("plat_no");
			}

			jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call PROC_add_parking(?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setLong(1, vehicleId);
					cs.setLong(2, parkingId);
					cs.registerOutParameter(3,Types.VARCHAR );
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					cs.execute();
					return null;
				}
			});
			Map data = new LinkedHashMap();
			data.put("areaId", String.valueOf(areaId));
			data.put("parkingId", String.valueOf(parkingId));
			data.put("platNo", oldPlatNo);
			String bindUrl = url + updateRebindUrl;
			Utils.postData(bindUrl, data);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  ReturnCode addUser(final UserInfo user){
		
		
		
	return	jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{ CALL PROC_add_user(?,?,?,?,?,?,?,?,?) } ";
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, user.getUserName());
				cs.setString(2, user.getPhoneNum());
				cs.setString(3, user.getHouseId());
				cs.setString(4, user.getCardId());
				cs.setLong(5, Long.valueOf(user.getParkingId()));
				cs.setString(6, user.getWay());
				cs.setDate(7, user.getCodeToDate());
				cs.registerOutParameter(8,Types.INTEGER );
				cs.registerOutParameter(9, Types.BIGINT);
				return cs;
			}
		}, new CallableStatementCallback<ReturnCode>() {
			public ReturnCode doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				ReturnCode rc = new ReturnCode();
				rc.setCode( cs.getInt(8));
				rc.setUserId(cs.getLong(9));
				return rc;
			}
		});
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  ReturnCode updateUser(final UserInfo user){
	return	jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{ CALL PROC_update_user(?,?,?,?,?,?,?,?,?)} ";
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, user.getUserName());
				cs.setString(2, user.getPhoneNum());
				cs.setString(3, user.getHouseId());
				cs.setString(4, user.getCardId());
				cs.setLong(5, Long.valueOf(user.getParkingId()));
				cs.setString(6, user.getWay());
				cs.setDate(7, user.getCodeToDate());
				cs.registerOutParameter(8,Types.INTEGER );
				cs.registerOutParameter(9, Types.BIGINT);
				return cs;
			}
		}, new CallableStatementCallback<ReturnCode>() {
			public ReturnCode doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				ReturnCode rc = new ReturnCode();
				rc.setCode( cs.getInt(8));
				rc.setUserId(cs.getLong(9));
				return rc;
			}
		});
	}
	
	
	public  void updateParkingUserRelation(final Long parkingId,final Long userId,final String way){
		String exeSql = "UPDATE parking_user_rel SET relation_type=? WHERE parking_id = ? AND user_id = ?;";
		jdbcTemplate.update(exeSql, new PreparedStatementSetter() {  
			@Override
			public void setValues(java.sql.PreparedStatement ps)
					throws SQLException {
				ps.setString(1, way);
				ps.setLong(2, parkingId);
				ps.setLong(3, userId);
			}});  
		
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Transactional
	public void deleteUserParkingRelation(final Long userId,final Long parkingId,final Long vehicle_id){
		//删除用户和车位的关系
	//	jdbcTemplate.update("{ CALL PROC_del_user_parking_rel(?,?,?) } ", new Object[]{userId,parkingId,vehicle_id});
		
		jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call PROC_del_user_parking_rel(?,?,?,?)}";
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setLong(1, userId);
				cs.setLong(2, parkingId);
				cs.setLong(3, vehicle_id);
				cs.registerOutParameter(4,Types.INTEGER );
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				return cs.getInt(4);
			}
		});
		
	}
	
	
	public  void updateServerOrder(final Long orderId,final String servProv,final Integer servProvNo,final String remark,
			final	String startTime,final String endTime,final String status){
		jdbcTemplate.update("UPDATE serv_orders SET serv_prov = ? , serv_prov_no = ? ,serv_start_time = ? , serv_end_time = ?, serv_remark = ? , order_status = ?"
				+ "WHERE order_id = ?	",new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, servProv);
						ps.setInt(2, servProvNo);
						ps.setTimestamp(3, com.mysql.jdbc.StringUtils.isNullOrEmpty(startTime)?null:new Timestamp(OrderUtils.toDate( startTime,Pattern.YMDHMS).getTimeInMillis()));
						ps.setTimestamp(4, com.mysql.jdbc.StringUtils.isNullOrEmpty(endTime)?null:new Timestamp(OrderUtils.toDate( endTime,Pattern.YMDHMS).getTimeInMillis()));
						ps.setString(5, remark);
						ps.setString(6, status);
						ps.setLong(7, orderId);
					}
				});
		
		
	}
	
	
	
	
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public  int  addAreaParking(final Long areaId,final Long parkingLotsId,final String parkingCode,
			final Long parkingType,final Integer parkingFloor, final String parkingLocation,final String parkingSpace){
		
	
		return jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call PROC_add_new_parking(?,?,?,?,?,?,?,?)}";
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setLong(1,areaId);
				cs.setLong(2,parkingLotsId);
				cs.setString(3, parkingCode);
				cs.setInt(4, Integer.parseInt(parkingType.toString()));
				cs.setInt(5, parkingFloor);
				cs.setString(6, parkingLocation);
				cs.setString(7, parkingSpace);
				cs.registerOutParameter(8, Types.INTEGER);
				return cs;
			}
		}, new CallableStatementCallback<Integer>() {
			public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				return cs.getInt(8);
			}
		});	
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  ReturnCode addBaseUser(final UserInfo user){
	return	jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{ CALL PROC_add_base_user(?,?,?,?,?,?,?,?)} ";
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, user.getUserName());
				cs.setString(2, user.getPhoneNum());
				cs.setString(3, user.getHouseId());
				cs.setString(4, user.getCardId());
				cs.setString(5, user.getLoginName());
				cs.setLong(6, user.getAreaId());
				cs.registerOutParameter(7,Types.INTEGER );
				cs.registerOutParameter(8, Types.BIGINT);
				return cs;
			}
		}, new CallableStatementCallback<ReturnCode>() {
			public ReturnCode doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				ReturnCode rc = new ReturnCode();
				rc.setCode( cs.getInt(7));
				rc.setUserId(cs.getLong(8));
				return rc;
			}
		});
	}
	
	@Transactional
	public  void  addArea(final AreaInfo ai,ReturnData<String> rd) throws Exception{
		
		if(com.mysql.jdbc.StringUtils.isNullOrEmpty(ai.getAreaName())){
			rd.setCode(201);
			rd.setMsg("未初始化小区名称");
			throw new Exception("未初始化小区名称");
		}
		
		ai.setLoginName("cd_"+PinYin.cn2FirstSpell(ai.getAreaName()));
		ai.setManagerPwd(PinYin.cn2FirstSpell(ai.getAreaName())+"2015");
		ai.setUserPwd("888888");
		
		
		List<Map<String, Object>> count = queryService.selectAreaManagerByLoginName(ai.getLoginName());
		if(null!=count && !count.isEmpty()){
			rd.setCode(201);
			rd.setMsg("已经存在的小区名");
			throw new Exception("已经存在的小区名");
		}
		
	/*	List<Map<String, Object>> users = queryService.selectUserByMobile(ai.getContactPhone());
		if(null!=users && !users.isEmpty()){
			rd.setCode(201);
			rd.setMsg("已经存在的电话号码");
			throw new Exception("已经存在的电话号码");
		}*/
		
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		final StringBuilder sql = new StringBuilder("insert into base_area (created_time, created_by, updated_time, updated_by, isvalid, sync_status,  area_name, login_name, login_password, cpu_id, period, weixin_id, contacts, mobile, province_id, city_id, county_id, amount_of_parkinglots, amount_of_parking, amount_of_regular, amount_of_rent, amount_of_temp, amount_of_spare, allow_temp, allow_rent_to_change, supplier_id, system_version, area_desc)"
				+ " values('NOW()',NULL,NULL,NULL,0,2,?,?,?,NULL,15,0,?,?,0,0,0,1,0,0,0,0,0,0,0,0,1.0,?);");
		
		// 添加小区
		jdbcTemplate.update(new  PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				 PreparedStatement ps = conn.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
				 ps.setString(1, ai.getAreaName());
				 ps.setString(2, ai.getLoginName());
				 ps.setString(3, ai.getManagerPwd());
				 ps.setString(4, ai.getContact());
				 ps.setString(5,ai.getContactPhone());
				 ps.setString(6, ai.getDesc());
				return ps;
			}
		},keyHolder);
		
		Map<String, Object> keys =  keyHolder.getKeys();
		if(null==keys || keys.isEmpty() || !keys.containsKey("area_id")){
			rd.setCode(201);
			rd.setMsg("小区添加失败");
			throw new Exception("小区号生成错误");
		}
		
		ai.setId(Long.valueOf(keys.get("area_id").toString()));

		
		//2 添加base_user  //888888
		
		/*UserInfo ui = new UserInfo();
		ui.setUserName(ai.getContact());
		ui.setPhoneNum(ai.getContactPhone());
		ui.setAreaId(ai.getId());
		ui.setLoginName(ai.getLoginName());
		ReturnCode rc =  this.addBaseUser(ui);
		
		if(rc.getCode()!=0)
			throw new Exception("数据库调用过程状态码错误"+rc.getCode());*/
		
	
		// 添加小区的物业管理系统的账号
		StringBuilder managerSql = new StringBuilder("insert into area_manager (created_time, created_by, updated_time, updated_by, isvalid, sync_status, area_id, manager_name, manager_pwd, is_admin) "
				+ "values(now(),NULL,NULL,NULL,'0','2',?,?,?,'1');");
		jdbcTemplate.update(managerSql.toString(),new Object[]{ ai.getId(),ai.getLoginName(),ai.getManagerPwd()});
		String time = OrderUtils.toStrDate(Calendar.getInstance());
		
		
	//	jdbcTemplate.update("insert into serv_area_item (isvalid,area_id,item_id,begin_date,end_date,relation_status,comments) values ( 0,"+ai.getId()+",1,'"+time+"','"+time+"',1, '洗车请在晚上9点前下单') ");
		
		final List<Map<String,Object>> areaItemList=jdbcTemplate.queryForList("select isvalid,area_id,item_id,begin_date,end_date,relation_status,comments from serv_area_item where area_id=1");
		
		String insertSql="insert into serv_area_item (isvalid,area_id,item_id,begin_date,end_date,relation_status,comments) values ( 0,?,?,'"+time+"','"+time+"',1, ?)";
		if(!CollectionUtils.isEmpty(areaItemList)){
			jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Map<String,Object> item=areaItemList.get(i);
					ps.setLong(1,ai.getId());
					ps.setLong(2,(Long)item.get("item_id"));
					ps.setString(3,(String)item.get("comments"));
					
				}
				
				@Override
				public int getBatchSize() {
					return areaItemList.size();
				}
			});
		}
		
		jdbcTemplate.update("  insert into base_area_user(isvalid,area_id,user_id,sync_status) values (?,?,?,?)  ",0,ai.getId(),1025L,2);
		
		
		rd.setCode(200);
		rd.setData(" 物业端（网页）登录账号："+ai.getLoginName() +",密码:"+ai.getManagerPwd());
		
	}
	
	@Transactional(rollbackOn=Exception.class,value=TxType.REQUIRED)
	 public  void  initVirtualAreaParking(Long areaId,Long parkingLotsId,
			 Integer floor,Integer floorMax,ReturnData<String> rd,Integer type,Integer index) throws Exception{
		
				if(type==1){
					
					List<Map<String, Object>> users = queryService.selectBaseUserByAreaId(areaId);
					if(null==users || users.isEmpty()){
						rd.setCode(201);
						rd.setMsg("当前小区为初始化账号（APP端）");
						throw new Exception("当前小区为初始化账号（APP端）");
					}
					Long userId = Long.valueOf( users.get(0).get("user_id").toString());
					
					bindBusParking(areaId, parkingLotsId, floor,userId,floorMax,index);
				}else{
					bindOwnParking(areaId, parkingLotsId, floor,floorMax,1);
				}
				
				
	 }
	
	/**
	 *   初始化虚拟车位 ,同时创建虚拟车位
	 * @param areaId
	 * @param userName
	 * @param loginName
	 * @param phone
	 * @param rd
	 * @throws Exception
	 */
	@Transactional(rollbackOn=Exception.class,value=TxType.REQUIRED)
	 public  void  addBaseUser(Long areaId, String userName,String loginName,String phone,ReturnData<String> rd) throws Exception{
		
		
		List<Map<String, Object>> users = queryService.selectUserByLoginName(loginName);
		if(null!=users && !users.isEmpty()){
			rd.setCode(201);
			rd.setMsg("已经存在的用户名");
			throw new Exception("已经存在的登录名称");
		}
		
		
		 users = 	queryService.selectUserByMobile(phone);
			if(null!=users && !users.isEmpty()){
				rd.setCode(201);
				rd.setMsg("已经存在的电话号码");
				throw new Exception("已经存在的电话号码");
			}
		
		UserInfo ui = new UserInfo();
		ui.setUserName(userName);
		ui.setPhoneNum(phone);
		ui.setAreaId(areaId);
		ui.setLoginName(loginName);
		ReturnCode rc =  this.addBaseUser(ui);
		
		if(rc.getCode()!=0){
			rd.setCode(201);
			rd.setMsg("数据库调用过程状态码错误"+rc.getCode());
			throw new Exception("数据库调用过程状态码错误"+rc.getCode());
		}
				
/*		 List<Map<String, Object>> lots = queryService.selectAreaParkinglotsByAreaId(areaId);
		 if(null==lots || lots.isEmpty()){
				rd.setCode(201);
				rd.setMsg("未添加停车场");
			 throw new Exception("未添加停车场");
		 }*/
		 
		 
		/*Long lotId = Long.valueOf( lots.get(0).get("parking_lots_id").toString() );
		
				bindBusParking(areaId, lotId, -2,rc.getUserId());
				bindOwnParking(areaId, lotId, -2);*/
				
	 }
	
	@Transactional(rollbackOn=Exception.class,value=TxType.REQUIRED)
	 public  void  initStrandardAreaParking(Long areaId,Long parkingLotsId,
			 Integer floor,Integer max,String prefix,Integer index) throws Exception{
				
		StringBuilder busParking =  new StringBuilder("INSERT INTO area_parking (area_id,parking_lots_id,parking_type,parking_code,custom_code,parking_space,parking_floor) values ");
		for (int i = index; i < (index + max); i++) {
			 String _prefix = 	 prefix;
			if(i<=9){
				_prefix += "00"+i;
			}else if(i >= 10 && i <= 99){
				_prefix += "0"+i;
			}else{
				_prefix += +i;
			}
			busParking.append("("+areaId + "," + parkingLotsId +",1" + ",'"+_prefix +"','"+_prefix + "','标准车位'"+"," + floor + " ),");
		}
		
		final String exeBusParking =  busParking.substring(0, busParking.length()-1);
		jdbcTemplate.execute(exeBusParking);
		
		
	 }

	 /**
	  *  创建物业的虚拟车位
	  * @param areaId
	  * @param parkingLotsId
	  * @param floor
	  * @throws Exception
	  */
	@Transactional(rollbackOn=Exception.class)
	private void bindBusParking(Long areaId, Long parkingLotsId, Integer floor,Long userId,Integer ploorMax,Integer index)
			throws Exception {
		StringBuilder busParking =  new StringBuilder("INSERT INTO area_parking (area_id,parking_lots_id,parking_type,parking_code,custom_code,parking_space,parking_floor) values ");
		for (int i = index; i < (index + ploorMax); i++) {
			String prefix = 	 OrderUtils.getPrefix(floor);
			if(i<=9){
				prefix += "WY00"+i;
			}else if(i>=10 && i<=99){
				prefix += "WY0"+i;
			}else{
				prefix += "WY"+i;
			}
			busParking.append("("+areaId + "," + parkingLotsId +",1" + ",'"+prefix +"','"+prefix + "','物业专用'"+"," + floor + " ),");
		}
		
		final String exeBusParking =  busParking.substring(0, busParking.length()-1);
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new  PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				 PreparedStatement ps = conn.prepareStatement(exeBusParking.toString(),Statement.RETURN_GENERATED_KEYS);
				return ps;
			}
		},keyHolder);
		
		List<Map<String, Object>>  lists =  keyHolder.getKeyList();
		if(null==lists || lists.isEmpty()){
			throw new Exception("错误的添加物业专用车位");
		}
		
		// 建立物业和车位的绑定关系
		StringBuilder bindBusParking = new StringBuilder("INSERT INTO parking_user_rel(create_time,area_id,user_id,parking_id,relation_type,end_date,due_date) values ");
		for (Map<String, Object> map : lists) {
		Long parkingId = Long.valueOf(map.get("parking_id").toString());
		bindBusParking.append(" (now(),"+areaId+","+userId+","+parkingId +","+"'SELF'"+", '2019-12-29', '2019-12-29' ),");
		}
		String exeBindBusParking =  bindBusParking.substring(0, bindBusParking.length()-1);
		jdbcTemplate.update(exeBindBusParking);
	}
	
	
	 /**
	  *  创建车必达虚拟车位
	  * @param areaId
	  * @param parkingLotsId
	  * @param floor
	  * @throws Exception
	  */
	@Transactional(rollbackOn=Exception.class)
	private void bindOwnParking(Long areaId, Long parkingLotsId, Integer floor,Integer floorMax,Integer index)
			throws Exception {
		StringBuilder busParking =  new StringBuilder("INSERT INTO area_parking (area_id,parking_lots_id,parking_type,parking_code,custom_code,parking_space,parking_floor) values ");
		for (int i = index; i < (index + floorMax); i++) {
			String prefix = 	 OrderUtils.getPrefix(floor);
			if(i<=9){
				prefix += "YY00"+i;
			}else if(i>=10 && i<= 99){
				prefix += "YY0"+i;
			}else{
				prefix += "YY"+i;
			}
			busParking.append("("+areaId + "," + parkingLotsId +",1" + ",'"+prefix +"','"+prefix + "','虚拟'"+"," + floor + " ),");
		}
		
		final String exeBusParking =  busParking.substring(0, busParking.length()-1);
		System.out.println(exeBusParking);
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		// 添加订单
		jdbcTemplate.update(new  PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				 PreparedStatement ps = conn.prepareStatement(exeBusParking.toString(),Statement.RETURN_GENERATED_KEYS);
				return ps;
			}
		},keyHolder);
		
		List<Map<String, Object>>  lists =  keyHolder.getKeyList();
		if(null==lists || lists.isEmpty()){
			throw new Exception("错误的添加虚拟专用车位");
		}
		
		Long busId  = 1025L;
		StringBuilder bindBusParking = new StringBuilder("INSERT INTO parking_user_rel(create_time,area_id,user_id,parking_id,relation_type,end_date,due_date) values ");
		for (Map<String, Object> map : lists) {
		Long parkingId = Long.valueOf(map.get("parking_id").toString());
		bindBusParking.append(" (now(),"+areaId+","+busId+","+parkingId +","+"'SELF'"+",'2019-12-29','2019-12-29'),");
		}
		String exeBindBusParking =  bindBusParking.substring(0, bindBusParking.length()-1);
		jdbcTemplate.update(exeBindBusParking);
	}
	
	public  void addParkingPlots(Long areaId,String lotsName,String desc,Long parent){
		jdbcTemplate.update("INSERT INTO area_parkinglots(created_time,area_id,parking_lots_name,parking_lots_desc,parent_lots_id) VALUES (now(),?,?,?,?)",new Object[]{ areaId,lotsName,desc,parent});
	}
	
	public  void  updateBaseUserInfo(Long userId,String userName) throws Exception{
				 jdbcTemplate.update("update base_user set user_name = ? where user_id = ?",new Object[]{userName,userId});
				 jdbcTemplate.update("update base_account set user_name = ? where user_id = ?",new Object[]{userName,userId});
	}
	
	public  void  addGataway(String gateName,Integer gateType,Long areaId,Long lotsId,Integer entrance,Integer exit) throws Exception{
		
		String insert = new String("insert into area_gateway "
				+ "(created_time,isvalid,gateway_type,area_id,parking_lots_id,carriageway,is_entrance,is_exit,gateway_name,control_no,open_no,open_type) "
				+ "values (now(),0,?,?,?,1,?,?,?,?,?,?);");
		jdbcTemplate.update(insert, new Object[]{gateType,areaId,lotsId,entrance,exit,gateName,"conNo",1,1});
	}
	
	
	public  void  updateAreaPakring(Long parkingId,String space) throws Exception{
		String updateSql = new String("update area_parking set parking_space = ? where parking_id = ? ");
		jdbcTemplate.update(updateSql,space,parkingId);
	}

	
	public void batchAddSerOrders(Integer total, String orderTime,Integer days,
			ReturnData<Integer> rd) throws Exception {
		List<Map<String, Object>> users = queryService.selectUserNotWashCar(orderTime,days);
		if(null==users || users.isEmpty()){
			rd.setCode(201);
			rd.setMsg("当前条件无法查询出符合条件的用户");
			return ;
		}
		Collections.shuffle(users);
		int  icount = 0; //计数器
		String dueTime  = orderTime+" 09:00:00";
		StringBuilder baseSql = new StringBuilder(" insert into serv_orders (created_time,isvalid,sync_status,"
				+ " order_name,order_type,order_total_price,order_create_time,"
				+ "order_date,pay_type,due_date,order_status, area_id,user_id,vehicle_id,parking_id )  values  ");
		
		
		for (Map<String, Object> map : users) {
			String createTime = orderTime;
			icount ++;
			createTime += " "+OrderUtils.randomHMS();
			baseSql.append(" ( '"+orderTime+"',1,0,'清洗外观','wash',10,'"+createTime+"','"+createTime +"','ACCOUNT','"+dueTime+"','SERVICE_FINISHED',"
					+ ""+map.get("area_id").toString()+","+map.get("user_id")+","+map.get("vehicle_id").toString()+","+map.get("parking_id").toString()+"  ),");

			if(icount>=total){
				break;
			}
		}
		rd.setData(icount);
		rd.setMsg("当前条件总共添加"+rd.getData()+"条记录!");
		
		String sql =  baseSql.substring(0, baseSql.length()-1);
		
		jdbcTemplate.update(sql);
		rd.setCode(200);
		
	}
	
	
	public  List<Map<String, Object>>  renameOfParking(List<Map<String, Object>>  parkings,String prefix,String rePrefix,Long areaId,Long lotsId)
	throws Exception
	{
		StringBuilder queryCode = new StringBuilder();
		for (Map<String, Object> map : parkings) { 
			String newName = map.get("parking_code").toString().replaceFirst(prefix, rePrefix);
			jdbcTemplate.update("  update area_parking set parking_code = ?,custom_code = ?  where parking_id = ? ",newName,newName,Long.valueOf(map.get("parking_id").toString()));
			queryCode.append("'"+newName+"'"+",");
		}
		addOldParking(parkings,queryCode);
		String inSql = queryCode.substring(0, queryCode.length()-1);
		System.out.println("O:"+inSql);
		return	queryService.selectParking(areaId, lotsId, null, inSql);
		
	}
	
	
	
	public  void addOldParking(List<Map<String, Object>>  parkings,StringBuilder queryCode)
	throws Exception
	{
		
		StringBuilder parkingSql =  new StringBuilder("INSERT INTO area_parking (area_id,parking_lots_id,parking_type,parking_code,custom_code,parking_space,parking_floor) values ");
		for (Map<String, Object> map : parkings) { 
			parkingSql.append("("+map.get("area_id").toString() + "," + map.get("parking_lots_id").toString() +",1" + ",'"+map.get("parking_code").toString() +"','"+map.get("parking_code").toString() + "','标准车位'"+"," + map.get("parking_floor").toString() + " ),");
			queryCode.append("'"+map.get("parking_code").toString()+"',");
		}
		
	String addSql = parkingSql.substring(0, parkingSql.length()-1);
	jdbcTemplate.execute(addSql);
		
	}
	
	public  void  updateUser(Long userId,Long areaId,String roomNum) throws Exception{
		this.jdbcTemplate.update("update base_area_user set address = ? where area_id = ? and user_id = ?", new Object[]{roomNum,areaId,userId});
	}
	
	/**  上传图片完成的时候保存url到数据库
	 * 
	 * @param url
	 * @param type
	 * @param status
	 * @return
	 */
public  Long  insertTempVehiclePic(final String url,final String type,final String status){
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new  PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				 PreparedStatement ps = conn.prepareStatement(" insert into  base_vehicle_pic (upload_time,picurl,pictype,picstatus) values (?,?,?,?)",
						 Statement.RETURN_GENERATED_KEYS);
				 ps.setTimestamp(1,new Timestamp( System.currentTimeMillis()));
				 ps.setString(2, url);
				 ps.setString(3, type);
				 ps.setString(4, status);
				return ps;
			}
		},keyHolder);
		Map<String, Object> keys =  keyHolder.getKeys();
		return Long.valueOf( keys.get("id").toString());
	}


/** 
 * 添加成功的时候，同时把图片付给他
 *  
 * @param ci
 * @param rd
 * @throws Exception
 */
public  void addVehicleAndImgs( CheckImg ci,ReturnData<String> rd) throws Exception{
	
	addVehicle(ci, rd);
	if(rd.getCode()!=1){//只有车辆为绑定车位的情况下，才允许插入该条记录
		Long id = Long.valueOf( this.queryService.selectVehicleByPlatNo(ci.getPlatNo()).get(0).get("vehicle_id").toString());
		// 删除上一次添加的图片
		this.jdbcTemplate.update(" delete from base_vehicle_pic where pictype = 'VL_ORI' AND picstatus = 'NEW' and vehicle_id = ? ",id);
	    if(!StringUtils.isEmpty(ci.getIds())){
			String[] ids = ci.getIds().split(",");
			for (String pid : ids) {
				updatePicOwner(Long.valueOf(pid), id);
			}
	    }
	//	this.jdbcTemplate.update(" update base_vehicle set check_status = 'NEW' where vehicle_id = ?  ",id);
	}else{
		throw new  VehicleBindParkingException("当前车牌已经绑定了车位，不能修改所属用户");
	}
}
	
	/**
	 *  修改图片的所属车辆
	 * @param pid
	 * @param vehicleId
	 */
	public  void  updatePicOwner(Long pid, Long vehicleId){
		this.jdbcTemplate.update(" update base_vehicle_pic set vehicle_id = ? where id = ? ",
				vehicleId,pid);
	}
	
	/**
	 *  审核正本
	 * @param vehicleId
	 * @param status
	 * @param urls
	 * @throws VehicleBindParkingException
	 * @throws Exception
	 */
	public  void checkVehicleImgWithVehicle(Long vehicleId,String status,String urls,String comment) throws   VehicleBindParkingException, Exception{
		
		this.jdbcTemplate.update(" update base_vehicle set check_status = ? where vehicle_id = ? ",status.toUpperCase(),vehicleId);
		if(status.equalsIgnoreCase("pass")){
			//  把之前通过的状态设置为del
			this.jdbcTemplate.update(" update base_vehicle_pic set picstatus = 'DEL' where picstatus = 'PASS' AND pictype = 'VL_ORI' AND vehicle_id = ?  ",vehicleId);
		}
		
		String[] arrUrls  = urls.split(",");
		for (String url : arrUrls) { //  把审核的图片设置为对应的状态
			if(url.contains("?")){
				url = url.substring(0, url.indexOf("?"));
			}
			Map<String, Object> img = this.queryService.selectVehicleUploadByUrl(url);
			if(null==img){
				throw new MyException("错误的图片地址");
			}else{
				this.jdbcTemplate.update("update base_vehicle_pic  set picstatus = ?, opinion = ?  where picurl = ? ",status.toUpperCase(),comment,url);
			}
		}
	}
	
	
	
	/**
	 *  审核副本
	 * @param vehicleId
	 * @param status
	 * @param urls
	 * @throws VehicleBindParkingException
	 * @throws Exception
	 */
	public  void checkVehicleImgWithVehicleEctype(Long vehicleId,String status,String urls,String comment) throws   VehicleBindParkingException, Exception{
		
		this.jdbcTemplate.update(" update base_vehicle set check_insure_status = ? where vehicle_id = ? ",status.toUpperCase(),vehicleId);
		
		String[] arrUrls  = urls.split(",");
		for (String url : arrUrls) { //  把审核的图片设置为对应的状态
			if(url.contains("?")){
				url = url.substring(0, url.indexOf("?"));
			}
			Map<String, Object> img = this.queryService.selectVehicleUploadByUrl(url);
			if(null==img){
				throw new MyException("错误的图片地址");
			}else{
				this.jdbcTemplate.update("update base_vehicle_pic  set picstatus = ?,opinion = ?  where picurl = ? ",status.toUpperCase(), comment, url);
				
			}
		}
		
	}
	
public  void checkVehicleImg(CheckImg ci) throws   VehicleBindParkingException, Exception{
		
	String[] arrUrls  =ci.getUrls().split(",");
	if(ci.getStatus().equalsIgnoreCase("pass")){
		ReturnData<String> rd  = new ReturnData<String>();
		this.addVehicle(ci, rd);
		Long vid ;
		if(rd.getCode()!=1){
			vid = Long.valueOf(	this.queryService.selectVehicleByPlatNo(ci.getPlatNo()).get(0).get("vehicle_id").toString());
		}else{
			throw new  VehicleBindParkingException("当前车牌已经绑定了车位，不能修改所属用户");
		}
		
		//  把之前为PASS状态的改为DEL状态
		this.jdbcTemplate.update(" update base_vehicle_pic set picstatus = 'DEL' where picstatus = 'PASS' AND pictype = 'VL_ORI' AND vehicle_id = ?  ",vid);
		
		for (String url : arrUrls) { //  把审核的图片设置为对应的状态
			String ptype = url.replaceAll(".*type=(\\w+)&.*", "$1");
			String pstatus = url.replaceAll(".*status=(\\w+)", "$1");
			if(com.mysql.jdbc.StringUtils.isNullOrEmpty(ptype)|| com.mysql.jdbc.StringUtils.isNullOrEmpty(pstatus)){
				throw new MyException("图片参数传入有误！");
			}
			if(url.contains("?")){
				url = url.substring(0, url.indexOf("?"));
			}
			// 获取到临时表的图片对象信息
			Map<String, Object> userPic = this.queryService.selectUserUploadByUrl(url);
			if(null!=userPic){
				// 把临时表图片转入真正的表中
				String tType = "NEW";
				if(ptype.equalsIgnoreCase("VL_ORI")){
					tType = "PASS";
				}
				Long realId =	insertTempVehiclePic(url, ptype, tType);
				// 修改临时表的状态
				this.jdbcTemplate.update(" update base_user_upload_pic set picstatus = ? where id = ?  ",pstatus.toUpperCase(),userPic.get("id"));
				//  修改真正图片表，当前图片的所属车辆
				updatePicOwner(realId, vid);
				
			}else{
				throw new Exception("传入的URLS错误！");
			}
		}
		// 维护车辆的状态
/*		if(ci.getStatus().equalsIgnoreCase("pass")){
			if(url.contains("?")){
				url = url.substring(0, url.indexOf("\\?"));
			}
			this.jdbcTemplate.update("update base_vehicle set check_status = ? where vehicle_id = ? ",ci.getStatus().toUpperCase(),vid);
		}*/
	}else{
		for (String url : arrUrls) { //  把审核的图片设置为对应的状态
			String pstatus = url.replaceAll(".*status=(\\w+)", "$1");
			if(url.contains("?")){
				url = url.substring(0, url.indexOf("?"));
			}
			// 获取到临时表的图片对象信息
			Map<String, Object> userPic = this.queryService.selectUserUploadByUrl(url);
			if(null!=userPic){
				// 修改临时表的状态
				this.jdbcTemplate.update(" update base_user_upload_pic set picstatus = ? where id = ?  ",pstatus.toUpperCase(),userPic.get("id"));
				
			}else{
				throw new Exception("传入的URLS错误！");
			}
		}
	}
	
	}
	
	
	public  void  insertUserUploadPic(Long userId,String url,String type,String status){
		this.jdbcTemplate.update(" insert into  base_user_upload_pic (user_id,upload_time,picurl,pictype,picstatus) values (?,?,?,?,?)",
				userId,new Date(System.currentTimeMillis()),url,type,status);
	}
	
	
}
