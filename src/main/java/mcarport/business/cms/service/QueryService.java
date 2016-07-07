package mcarport.business.cms.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mcarport.business.cms.dto.AreaInfo;
import mcarport.business.cms.dto.ManagerInfo;
import mcarport.business.cms.dto.Page;
import mcarport.business.cms.dto.QueryData;
import mcarport.business.cms.dto.UserInfo;
import mcarport.business.cms.dto.Vehicle;
import mcarport.business.cms.utils.OrderUtils;
import mcarport.business.cms.utils.OrderUtils.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class QueryService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public Page<List<UserInfo>> fuzzQuery(QueryData queryData,Page<List<UserInfo>> page) {
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		page.setDatas(userInfoList);
		StringBuilder sql = new StringBuilder();
		sql.append("select check_status, address, parking_lots_id, swept_volume, a.pay_id, a.area_id,a.area_name,user_id,user_name,mobile,parking_id,parking_code,parking_space,relation_type,vehicle_id,a.plat_no,"
				+ " case  when  due_date is null then null else due_date end as due_date ,"
				+ " case when b.plat_no is not null then '已入库' when  b.plat_no is null and a.plat_no is not null then '未入库' "
				+ " else '' end vehiclestatus from "
				+ " ("
				+ " select BV.check_status, baus.address, AP.parking_lots_id,  BV.swept_volume, AP.pay_id,AP.area_id,ba.area_name, COALESCE(BU.user_id,999999) user_id,BU.user_name, BU.mobile,AP.parking_id,AP.parking_code, AP.parking_space,"
				+ " case PUR.relation_type when 'SELF' then '购买' when 'LOAN' then '租赁' end relation_type, BV.vehicle_id,"
				+ " BV.plat_no , PUR.due_date from area_parking AP"
				+ " left join parking_user_rel PUR on AP.parking_id = PUR.parking_id"
				+ " left join parking_vehicle_rel PVR on AP.parking_id = PVR.parking_id"
				+ " left join base_user BU on BU.user_id = PUR.user_id  left join base_area_user baus on ( BU.user_id = baus.user_id and  AP.area_id = baus.area_id )  "
				+ " left join base_vehicle BV on BV.vehicle_id = PVR.vehicle_id   left join base_area ba on AP.area_id = ba.area_id  "
				+ " UNION"
				+ " select BV.check_status, BAU.address, NULL, BV.swept_volume, null, BAU.area_id,ba.area_name, BU.user_id, BU.user_name, BU.mobile , null,'无','无','无',BUV.vehicle_id , BV.plat_no,null"
				+ " from  base_user	BU, base_user_vehicle BUV, base_vehicle BV, base_area_user BAU ,base_area ba "
				+ " where BU.user_id = BUV.user_id  "
				
				+ " and BV.vehicle_id = BUV.vehicle_id"
				+ " and BAU.user_id = BU.user_id  and BAU.area_id = ba.area_id   "
				+ " and BUV.vehicle_id not in (select vehicle_id from  parking_vehicle_rel )"
				+ ") a "
				+ " left join (select distinct area_id,plat_no from parking_realtime_record where isvalid = 0) b on a.plat_no = b.plat_no and a.area_id = b.area_id where 1=1  and parking_space !='虚拟'  ");
		if (queryData.getAreaId() != 0) {
			sql.append(" and a.area_id=");
			sql.append(queryData.getAreaId());
		}
		if (queryData.getUserId() != 0) {
			sql.append(" and user_id=");
			sql.append(queryData.getUserId());
		}
		if (queryData.getParkingId()!=0) {
			sql.append(" and parking_id=");
			sql.append(queryData.getParkingId());
		}
		if (!StringUtils.isEmpty(queryData.getUserName())) {
			sql.append(" and user_name like '%");
			sql.append(queryData.getUserName());
			sql.append("%'");
		}
		if (!StringUtils.isEmpty(queryData.getPhoneNum())) {
			sql.append(" and mobile like '%");
			sql.append(queryData.getPhoneNum());
			sql.append("%'");
		}
		if (!StringUtils.isEmpty(queryData.getPlatNo())) {
			sql.append(" and a.plat_no  like '%");
			sql.append(queryData.getPlatNo());
			sql.append("%'");
		}
		if (!StringUtils.isEmpty(queryData.getParkingNo())) {
			sql.append(" and parking_code like '%");
			sql.append(queryData.getParkingNo());
			sql.append("%'");
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(queryData.getRoomNo())){
			
			 sql.append("  and  address  like '%"+queryData.getRoomNo()+"%'    ");
			
		}
		

		sql.append(" order by user_id ,parking_code");
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql.toString());
		if (CollectionUtils.isEmpty(resultList)) {
			return page;
		}
		
		page.setTotal(resultList.size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		resultList = jdbcTemplate.queryForList(sql.toString());
		List<Map<String, Object>> lots = this.selectAreaParkinglotsByAreaId();
		Map<Long, String> allLosts =  listToMap(lots);
		
		for (Map<String, Object> map : resultList) {
			UserInfo userInfo = new UserInfo();
			if(null!= map.get("parking_lots_id")){
				userInfo.setPlotsName(allLosts.get(Long.valueOf(map.get("parking_lots_id").toString())));
				userInfo.setLotsId(Long.valueOf(map.get("parking_lots_id").toString()));
			}
			userInfo.setUserName(nullEmpty((String) map.get("user_name")));
			userInfo.setParkingNo(nullEmpty((String) map.get("parking_code")));
			userInfo.setParkingSpace(nullEmpty((String) map.get("parking_space")));
		/*	if(userInfo.getParkingSpace().equals("虚拟")){
				continue;
			}*/
			if(null==map.get("due_date")){
				userInfo.setDueDate("无");
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) map.get("due_date"));
				userInfo.setDueDate(OrderUtils.toStrDate(cal));
			}
		//	userInfo.setDueDate(nullEmpty((String) map.get("due_date")));
			userInfo.setParkingType(nullEmpty((String) map.get("relation_type")));
			userInfo.setPlatNo(nullEmpty((String) map.get("plat_no")));
			userInfo.setPhoneNum(nullEmpty((String) map.get("mobile")));
			userInfo.setVehicleStatus(nullEmpty((String) map.get("vehicleStatus")));
			if(null!=map.get("address")){
				userInfo.setRoomNo(map.get("address").toString());
			}
			if(null!=map.get("area_name")){
				userInfo.setAreaName(map.get("area_name").toString());
			}
			
			if("999999".equals( map.get("user_id").toString())){
				//continue;
				userInfo.setUserId(-1L);
			}else{
				userInfo.setUserId((Long) map.get("user_id"));
			}
			
			//userInfo.setUserId((Long) map.get("user_id"));
			userInfo.setParkingId( null == map.get("parking_id")?"":map.get("parking_id").toString());
			userInfo.setVehicleId((Long) map.get("vehicle_id")==null?0L:(Long) map.get("vehicle_id"));
			userInfo.setAreaId((Long) map.get("area_id"));
			if(map.get("pay_id")==null ||  com.mysql.jdbc.StringUtils.isNullOrEmpty(map.get("pay_id").toString())  ){
				userInfo.setPayId(Long.valueOf(-1));
			}else{
				userInfo.setPayId(Long.valueOf(map.get("pay_id").toString()));
			}
			Object swept = map.get("swept_volume");
			userInfo.setSweptVolume(null==swept?"":swept.toString());
			userInfoList.add(userInfo);
		}
		return page;
	}

	/*public List<UserInfo> fuzzQuery(QueryData queryData) {
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		StringBuilder sql = new StringBuilder();
		sql.append("select parking_lots_id,parking_lots_name, swept_volume, a.pay_id, a.area_id,user_id,user_name,mobile,parking_id,parking_code,parking_space,relation_type,vehicle_id,a.plat_no,"
				+ " case  when  due_date is null then null else due_date end as due_date ,"
				+ " case when b.plat_no is not null then '已入库' when  b.plat_no is null and a.plat_no is not null then '未入库' "
				+ " else '' end vehiclestatus from "
				+ " ("
				+ " select apll.parking_lots_id,apll.parking_lots_name,  BV.swept_volume, AP.pay_id,apll.area_id, "
				+ " COALESCE(BU.user_id,999999) user_id,BU.user_name, BU.mobile,AP.parking_id,AP.parking_code, AP.parking_space, "
				+ "  case PUR.relation_type when 'SELF' then '购买' when 'LOAN' then '租赁' end relation_type, BV.vehicle_id, BV.plat_no , "
				+ " PUR.due_date from area_parking AP left join parking_user_rel PUR on AP.parking_id = PUR.parking_id left join parking_vehicle_rel PVR"
				+ " on AP.parking_id = PVR.parking_id left join base_user BU on BU.user_id = PUR.user_id left join base_vehicle BV on BV.vehicle_id = PVR.vehicle_id "
				+ "  full join area_parkinglots  apll on AP.parking_lots_id = apll.parking_lots_id  "
				+ " UNION"
				+ " select NULL,NULL, BV.swept_volume, null, BAU.area_id, BU.user_id, BU.user_name, BU.mobile , null,'无','无','无',BUV.vehicle_id , BV.plat_no,null"
				+ " from  base_user	BU, base_user_vehicle BUV, base_vehicle BV, base_area_user BAU "
				+ " where BU.user_id = BUV.user_id  "
				
				+ " and BV.vehicle_id = BUV.vehicle_id"
				+ " and BAU.user_id = BU.user_id"
				+ " and BUV.vehicle_id not in (select vehicle_id from  parking_vehicle_rel )"
				+ ") a "
				+ " left join (select distinct area_id,plat_no from parking_realtime_record where isvalid = 0) b on a.plat_no = b.plat_no and a.area_id = b.area_id where 1=1    and parking_space !='虚拟'    ");
		if (queryData.getAreaId() != 0) {
			sql.append(" and a.area_id=");
			sql.append(queryData.getAreaId());
		}
		if (queryData.getUserId() != 0) {
			sql.append(" and  user_id= ");
			sql.append(queryData.getUserId());
			sql.append(" and ( user_id=");
			sql.append(queryData.getUserId());
			sql.append(" or user_id = 999999 ) ");
		}
		if (queryData.getParkingId()!=0) {
			sql.append(" and parking_id=");
			sql.append(queryData.getParkingId());
		}
		if (!StringUtils.isEmpty(queryData.getUserName())) {
			sql.append(" and user_name like '%");
			sql.append(queryData.getUserName());
			sql.append("%'");
		}
		if (!StringUtils.isEmpty(queryData.getPhoneNum())) {
			sql.append(" and mobile like '%");
			sql.append(queryData.getPhoneNum());
			sql.append("%'");
		}
		if (!StringUtils.isEmpty(queryData.getPlatNo())) {
			sql.append(" and a.plat_no  like '%");
			sql.append(queryData.getPlatNo());
			sql.append("%'");
		}
		if (!StringUtils.isEmpty(queryData.getParkingNo())) {
			sql.append(" and parking_code like '%");
			sql.append(queryData.getParkingNo());
			sql.append("%'");
		}

		sql.append(" order by user_id ,parking_code");
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql.toString());
		if (CollectionUtils.isEmpty(resultList)) {
			return userInfoList;
		}
		
		for (Map<String, Object> map : resultList) {
			UserInfo userInfo = new UserInfo();
			if(null!= map.get("parking_lots_name")){
				userInfo.setPlotsName( map.get("parking_lots_name").toString());
			}
			
			userInfo.setLotsId( null==map.get("parking_lots_id") ?-1L: Long.valueOf(map.get("parking_lots_id").toString()));
			
			userInfo.setUserName(nullEmpty((String) map.get("user_name")));
			userInfo.setParkingNo(nullEmpty((String) map.get("parking_code")));
			userInfo.setParkingSpace(nullEmpty((String) map.get("parking_space")));
			if(userInfo.getParkingSpace().equals("虚拟")){
				continue;    
			}
			if(null==map.get("due_date")){
				userInfo.setDueDate("无");
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) map.get("due_date"));
				userInfo.setDueDate(OrderUtils.toStrDate(cal));
			}
		//	userInfo.setDueDate(nullEmpty((String) map.get("due_date")));
			userInfo.setParkingType(nullEmpty((String) map.get("relation_type")));
			userInfo.setPlatNo(nullEmpty((String) map.get("plat_no")));
			userInfo.setPhoneNum(nullEmpty((String) map.get("mobile")));
			userInfo.setVehicleStatus(nullEmpty((String) map.get("vehicleStatus")));
			if("999999".equals( map.get("user_id").toString())){
				//continue;
				userInfo.setUserId(-1L);
			}else{
				userInfo.setUserId((Long) map.get("user_id"));
			}
			
			//userInfo.setUserId((Long) map.get("user_id"));
			userInfo.setParkingId( null == map.get("parking_id")?"":map.get("parking_id").toString());
			userInfo.setVehicleId((Long) map.get("vehicle_id")==null?0L:(Long) map.get("vehicle_id"));
			userInfo.setAreaId((Long) map.get("area_id"));
			if(map.get("pay_id")==null ||  com.mysql.jdbc.StringUtils.isNullOrEmpty(map.get("pay_id").toString())  ){
				userInfo.setPayId(Long.valueOf(-1));
			}else{
				userInfo.setPayId(Long.valueOf(map.get("pay_id").toString()));
			}
			Object swept = map.get("swept_volume");
			userInfo.setSweptVolume(null==swept?"":swept.toString());
			userInfoList.add(userInfo);
		}
		return userInfoList;
	}*/

	public ManagerInfo login(String loginName, String password) {
		ManagerInfo manager = new ManagerInfo();
		String sql = "select * from cbdt_manager where manager_name=? and manager_pwd=?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, loginName, password);
		if (CollectionUtils.isEmpty(resultList)) {
			return manager;
		}
		long managerId = (Long) resultList.get(0).get("manager_id");
		manager.setManageId(managerId);

		return manager;
	}
	
	public List<Map<String, Object>> selectManager(Long id){
		return jdbcTemplate.queryForList(" select * from cbdt_manager where manager_id = ? ", id);
	}

	public List<AreaInfo> queryAreaInfo(long manageId) {
		List<AreaInfo> areaInfoList = new ArrayList<AreaInfo>();
		String sql = "select BA.area_id,area_name from base_area BA,area_manager AM "
				+ " where AM.area_id=BA.area_id and AM.manager_id=?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, manageId);
		if (CollectionUtils.isEmpty(resultList)) {
			return areaInfoList;
		}
		for (Map<String, Object> map : resultList) {
			AreaInfo areaInfo = new AreaInfo();
			areaInfo.setId((Long) map.get("area_id"));
			areaInfo.setAreaName((String) map.get("area_name"));
			areaInfoList.add(areaInfo);

		}
		return areaInfoList;
	}
	
	
	public  List<Map<String, Object>> queryAreaByUserId(Long userId){
		String sql = "SELECT ba.area_id,ba.area_name FROM base_area_user bau , base_area ba WHERE bau.area_id = ba.area_id AND bau.user_id = ?";
		return jdbcTemplate.queryForList(sql, userId);
	}
	
	

	public List<Vehicle> queryCarList(long userId,Long areaId) {
		List<Vehicle> carList = new ArrayList<Vehicle>();
		String sql = "SELECT bv.vehicle_id, bv.plat_no FROM	base_vehicle bv,	base_user_vehicle buv"
				+" WHERE	bv.vehicle_id = buv.vehicle_id "
				+" AND buv.user_id = ? AND bv.vehicle_id NOT IN ("
				+" select pvl.vehicle_id from  base_user_vehicle buv ,parking_vehicle_rel  pvl , area_parking ap where   buv.vehicle_id = pvl.vehicle_id    and pvl.parking_id = ap.parking_id  "
				+" AND user_id =?      AND bv.check_status <> 'NEW'  AND   ap.area_id = ?  )";
		
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, userId,userId,areaId);
		if (CollectionUtils.isEmpty(resultList)) {
			return carList;
		}
		for (Map<String, Object> map : resultList) {
			Vehicle vehicle = new Vehicle();
			vehicle.setId((Long) map.get("vehicle_id"));
			vehicle.setPlatNo((String) map.get("plat_no"));
			carList.add(vehicle);

		}
		return carList;
	}

	private String nullEmpty(String value) {
		if (StringUtils.isEmpty(value)) {
			return "无";
		} else
			return value;
	}
	
	public List<Map<String, Object>>  queryRelationWay(){
		String exeSql = "SELECT DISTINCT pur.relation_type FROM  parking_user_rel pur ;";
		return this.jdbcTemplate.queryForList(exeSql);
		
	}
	public Page<List<Map<String, Object>>> selectServerOrder(String userName,Long orderId,
			String startTime,String endTime,Long parkingId ,String platNo,
			String phone,String servProv,Integer servProvNo,String type,String status,
			String roomNum,String parkingCode,
			String orderBy,String direction,Page<List<Map<String, Object>>> page
			){
		StringBuilder sql = new StringBuilder("SELECT  so.created_time,so.order_id,so.order_name,so.order_type,so.order_status ,bu.user_name,so.order_total_price  ,ap.parking_code,bv.plat_no,bu.mobile,  "
				+ " so.serv_start_time,so.serv_end_time,so.serv_remark,ba.area_name,apl.parking_lots_name,bau.address as roomNo,so.isvalid FROM "
				+ " serv_orders so  , base_area ba ,  area_parking ap , base_user bu , base_vehicle bv ,area_parkinglots apl ,base_area_user bau	"
				+ "WHERE so.area_id = ba.area_id  "
				+ "AND so.parking_id = ap.parking_id "  
				+ " AND so.user_id = bu.user_id  "
				+ " AND  so.vehicle_id = bv.vehicle_id   and ap.parking_lots_id = apl.parking_lots_id"
				+ " AND (so.user_id = bau.user_id  and  ba.area_id = bau.area_id )   ");
		List<Object> params = new ArrayList<Object>();
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)){
			sql.append(" AND bu.user_name like  ?  ");
			params.add("%"+userName+"%");
		}
		if(null!=orderId){
			sql.append(" AND so.order_id = ? ");
			params.add(orderId);
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(startTime))	{
			sql.append(" AND   so.created_time    >=  ? ");
			params.add( OrderUtils.toDate( startTime+" 00:00:00",Pattern.YMDHMS));
			
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(endTime))	{
			sql.append(" AND  so.created_time  <=  ? ");
			params.add( OrderUtils.toDate( endTime+" 23:59:59",Pattern.YMDHMS));
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(roomNum))	{
			sql.append(" AND  bau.address  like   ? ");
			params.add("%"+roomNum+"%" );
		}
		
		if(null!=parkingId){
			sql.append(" and so.parking_id = ? ");
			params.add(parkingId);
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(platNo)){
			sql.append(" AND bv.plat_no = ? ");
			params.add(platNo);
		}
		
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(phone)){
			sql.append(" AND bu.mobile  like  ? ");
			params.add("%"+phone+"%");
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(servProv)){
			sql.append(" AND so.serv_prov like  ? ");
			params.add("%"+servProv+"%");
		}
		
		if(null!=servProvNo){
			sql.append(" AND so.serv_prov_no = ? ");
			params.add(servProvNo);
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(parkingCode)){
			sql.append(" AND ap.parking_code like ? ");
			params.add("%"+parkingCode+"%");
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(type)){
			sql.append(" AND so.order_type = ? ");
			params.add(type);
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(status)){
			sql.append(" AND so.order_status = ? ");
			params.add(status);
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(orderBy)){
			sql.append(" ORDER BY  " + orderBy  + " " +direction);
		}
		
		List<Map<String, Object>> datas =  jdbcTemplate.queryForList(sql.toString(), params.toArray());
		page.setTotal(datas.size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		datas =  jdbcTemplate.queryForList(sql.toString(), params.toArray());
		page.setDatas(datas);
		return page;
	}
	
	public Map<String, Object> selectServerOrderById(Long id){
		return jdbcTemplate.queryForMap("SELECT * FROM  serv_orders WHERE order_id  = ?",id);
	}
	
	public  List<Map<String, Object>> selectServerOrderStatus(){
		return jdbcTemplate.queryForList("SELECT  DISTINCT order_status  FROM  serv_orders");
	}
	
	public  List<Map<String, Object>> selectServerOrderTypes(){
		return jdbcTemplate.queryForList("SELECT  DISTINCT order_type FROM  serv_orders");
	}
	
	
	public List<Map<String, Object>> selectGoOutOrders(String startTime,String endTime,String platNo,Integer payType,Integer betweenTime){
		
		StringBuilder sql = new StringBuilder("SELECT pgor.coming_time ,  pgor.go_out_time, pgor.go_out_id,pgor.price,pgor.go_out_id,pgor.plat_no,bu.user_name,pgor.pay_type ,"
				+ " TIMESTAMPDIFF(HOUR, pgor.coming_time, pgor.go_out_time ) bt "
				+ "FROM  parking_go_out_record pgor,base_user bu"
				+ " WHERE    pgor.user_id = bu.user_id  AND   pgor.fee_type = ?  ");
		
		List<Object> params = new ArrayList<Object>();
		params.add("临时");
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(startTime)){
			sql.append(" AND pgor.go_out_time >= ?  ");
			params.add( OrderUtils.toDate( startTime+" 00:00:00",Pattern.YMDHMS));
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(endTime)){
			sql.append(" AND pgor.go_out_time <= ?  ");
			params.add( OrderUtils.toDate( endTime+" 23:59:59",Pattern.YMDHMS));
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(platNo)){
			sql.append(" AND pgor.plat_no = ?  ");
			params.add(platNo);
		}
		
		if(null!=payType){
			sql.append(" AND pay_type = ?  ");
			params.add(payType);
		}
		
		if(null!=betweenTime){
			sql.append(" AND TIMESTAMPDIFF(HOUR, pgor.coming_time, pgor.go_out_time ) = ?  ");
			params.add(betweenTime);
		}
		
		return jdbcTemplate.queryForList(sql.toString(), params.toArray());
	}
	
	public List<Map<String,Object>> selectParkingOrders(Long managerId,String userName,String startTime,String endTime,
			String phone,String parkingCode,
			String dueStartTime,String dueEndTime,
			String orderBy,String direction
			){

		StringBuilder sql = new StringBuilder("SELECT bu.mobile,  of.created_time,of.created_by,of.order_id, of.user_id,of.offline_code,of.order_status,of.pay_type,of.total_money,of.pay_time ,bu.user_name, ap.parking_code ,pof.amount_of_month,pof.end_date  FROM  parking_order_fixed of,base_user bu ,area_parking ap ,parking_order_fixed_detail pof WHERE of.user_id = bu.user_id AND of.manger_id = ?  AND bu.user_id = of.user_id  AND	pof.parking_id = ap.parking_id  AND of.order_id = pof.order_id  ");
		
		List<Object> params = new ArrayList<Object>();
	params.add(managerId);	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName))	{
		sql.append(" and  bu.user_name LIKE ? ");
		params.add("%"+userName+"%");
	}
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(startTime))	{
		sql.append(" AND   of.created_time    >=  ? ");
		params.add( OrderUtils.toDate( startTime+" 00:00:00",Pattern.YMDHMS));
	}
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(endTime))	{
		sql.append(" AND  of.created_time  <=  ? ");
		params.add( OrderUtils.toDate( endTime+" 23:59:59",Pattern.YMDHMS));
	}
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(phone))	{
		sql.append(" and  bu.mobile LIKE ? ");
		params.add("%"+phone+"%");
	}
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(parkingCode))	{
		sql.append(" and  ap.parking_code =  ? ");
		params.add(parkingCode);
	}
	
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(dueStartTime))	{
		sql.append(" AND   pof.end_date   >=  ? ");
		params.add( OrderUtils.toDate( dueStartTime+" 00:00:00",Pattern.YMDHMS));
	}
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(dueEndTime))	{
		sql.append(" AND   pof.end_date   <=  ? ");
		params.add( OrderUtils.toDate( dueEndTime+" 23:59:59",Pattern.YMDHMS));
	}
	
	if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(orderBy))	
	sql.append("  order by  "+ orderBy +" " + direction);
	
	return jdbcTemplate.queryForList(sql.toString(),params.toArray());
	
	}
	
	public  List<Map<String, Object>> getAreas(){
		return jdbcTemplate.queryForList("SELECT  ba.area_id,ba.area_name FROM  base_area ba ");
	}
	
	public  Integer getAreaByName(String name){
		List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT  ba.area_id,ba.area_name FROM  base_area ba WHERE ba.area_name = ? ",name);
		if(null==results || results.isEmpty()) return 0;
		return results.size();
	}
	
	public  List<Map<String, Object>> selectAreaParkinglotsByAreaId(Long areaId){
		return jdbcTemplate.queryForList("SELECT  a.parking_lots_id,a.parking_lots_name,a.parking_lots_desc,  case when  b.parking_lots_name is null then '无'  else b.parking_lots_name end as parent_name   FROM    area_parkinglots a  left join  area_parkinglots  b on a.parent_lots_id = b.parking_lots_id where a.area_id = ? ",areaId);
	}
	
	public List<Map<String, Object>> selectUserByMobile(String mobile){
		return jdbcTemplate.queryForList("SELECT * FROM base_user WHERE mobile LIKE ? ","%"+mobile+"%");
	}
	
	public List<Map<String, Object>> selectUserByLoginName(String loginName){
		return jdbcTemplate.queryForList("SELECT * FROM base_account WHERE login_name = ? ",loginName);
	}
	
	public List<Map<String, Object>> selectAreaManagerByLoginName(String loginName){
		return jdbcTemplate.queryForList("SELECT * FROM area_manager WHERE manager_name = ? ",loginName);
	}
	
	public  Map<String, Object> selectAreaById(Long areaId){
		return jdbcTemplate.queryForMap("SELECT * FROM  base_area WHERE area_id = ?",areaId);
	}
	
	public  boolean hasInitAreaParking(Long areaId,Long parkingLotsId,Integer floor,Integer type){
		
		List<Map<String, Object>> results = null;
		if(type==1){
		 results = jdbcTemplate.queryForList("SELECT * FROM area_parking ap WHERE ap.area_id = "
					+ "? AND ap.parking_lots_id = ? AND ap.parking_floor = ? AND ap.parking_code = ?",new Object[]{areaId,parkingLotsId,floor,OrderUtils.getPrefix(floor)+"WY001"});
		}else{
			 results = jdbcTemplate.queryForList("SELECT * FROM area_parking ap WHERE ap.area_id = "
						+ "? AND ap.parking_lots_id = ? AND ap.parking_floor = ? AND ap.parking_code = ?",new Object[]{areaId,parkingLotsId,floor,OrderUtils.getPrefix(floor)+"YY001"});
		}
		if(null==results || results.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	
	public  boolean hasInitAreaParking(Long areaId,Long parkingLotsId,Integer floor,String code){
		
		
		StringBuilder sql = new StringBuilder("SELECT * FROM area_parking ap WHERE ap.area_id = "
					+ "? AND ap.parking_lots_id = ?  AND ap.parking_code like ?  ");
		List<Object> params = new ArrayList<Object>();
		params.add(areaId);
		params.add(parkingLotsId);
		params.add("%"+code+"%");
		
 		if(null!=floor){
			sql.append(" AND AND ap.parking_floor = ? ");
			params.add(floor);
		}
		
		 List<Map<String, Object>> results = jdbcTemplate.queryForList(sql.toString(),params.toArray());
		if(null==results || results.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public  boolean hasInitAreaParking(Long areaId ,String suffix){
		
		List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM area_parking ap WHERE ap.area_id = "
				+ " ?  AND ap.parking_code  like ?  ",new Object[]{areaId,"%"+suffix+"%"});
		if(null==results || results.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public  boolean hasInitAPPBaseUser(Long areaId){
		
		List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT  *  FROM base_user  bu,  base_area_user bau where  bu.user_id = bau.user_id and  bau.area_id = ? and bu.user_type = 'WY'",areaId);
		if(null==results || results.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	
	public  List<Map<String, Object>> selectWYUser(Long areaId){
	return	 jdbcTemplate.queryForList("SELECT  bu.user_name,ba.login_name,bu.mobile,bu.user_id  FROM base_user  bu,  base_area_user bau ,base_account ba  where "
			+ " bu.user_id = bau.user_id and   bu.user_id = ba.user_id and  bau.area_id = ? and bu.user_type = 'WY'",areaId);
	
	}
	
	
			
	public  boolean selectAreaParkinglotsByLotsName(String name,Long areaId){
		List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from  area_parkinglots ap where ap.parking_lots_name = ? and ap.area_id = ?",name,areaId);
		if(null!=results && !results.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	    
		public  List<Map<String, Object>> selectBaseUserByAreaId(Long areaId){
			return jdbcTemplate.queryForList("SELECT  *  FROM base_user bu,  base_area_user bau where bu.user_id = bau.user_id and  bau.area_id = ? and bu.user_type = 'WY'",areaId);
		}
	    
	
	public  List<Map<String, Object>> selectAreaParkinglotsByAreaId(){
		return jdbcTemplate.queryForList("SELECT  a.parking_lots_id,a.parking_lots_name FROM  area_parkinglots a ");
	}
	
	private Map<Long, String> listToMap(List<Map<String, Object>> lots) {
		Map<Long, String> allLots = new  HashMap<Long, String>();
		for (Map<String, Object> map : lots) {
			allLots.put(Long.valueOf( map.get("parking_lots_id").toString()), map.get("parking_lots_name").toString());
		}
		return allLots;
	}
	
	public  List<Map<String, Object>>  selectParkingVehicleRelByParkingId(Long parkingId){
		String sql = "select plat_no from base_vehicle v,parking_vehicle_rel pvr where v.vehicle_id=pvr.vehicle_id and pvr.parking_id=?";
		return  jdbcTemplate.queryForList(sql, parkingId);
	}
	
	public  List<Map<String, Object>>  selectGateWayByAreaId(Long areaId) throws Exception{
		
		StringBuilder selectSql = new StringBuilder("select ag.gateway_name,ag.gateway_type,ag.is_entrance,ag.is_exit,ba.area_name ,"
				+ "ag.area_id,apl.parking_lots_name from area_gateway ag,base_area ba, area_parkinglots apl  "
				+ " where ag.area_id = ba.area_id  and ag.parking_lots_id = apl.parking_lots_id   ");
		List<Object> params = new ArrayList<Object>();
		if(null!=areaId){
			selectSql.append(" and ag.area_id = ?  ");
			params.add(areaId);
		}
		return jdbcTemplate.queryForList(selectSql.toString(), params.toArray());
	}
	
	
	public  Page<List<Map<String, Object>>> selectAreaParking(Long areaId,Long lotsId,Integer floor,String type,Page<List<Map<String, Object>>> page){
		StringBuilder sql = new StringBuilder("select   ap.parking_id,ap.parking_code,ap.parking_space, "
				+ " ap.parking_floor,aps.parking_lots_name "
				+ " from  area_parking ap,area_parkinglots aps where ap.parking_lots_id = aps.parking_lots_id ");
		List<Object> params = new ArrayList<Object>();
		if(null!=areaId){
			sql.append("  and ap.area_id = ? ");
			params.add(areaId);
		}
		
		if( null!=lotsId ){
			sql.append("  and ap.parking_lots_id = ? ");
			params.add(lotsId);
		}
		
		if( null!=floor ){
			sql.append("  and ap.parking_floor = ? ");
			params.add(floor);
		}
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(type)){
			
			if(type.equals("standard")){
				sql.append("  and ap.parking_space != '虚拟' and ap.parking_space != '物业专用'   ");
			}else if(type.equals("v_bus")) {
				sql.append("  and ap.parking_space = '物业专用' ");
			}else if(type.equals("v_cbd")){
				sql.append("  and ap.parking_space = '虚拟' ");
			}
			
		}
		page.setTotal(jdbcTemplate.queryForList(sql.toString(),params.toArray()).size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		page.setDatas(jdbcTemplate.queryForList(sql.toString(),params.toArray()));
		return page;
	}
	
	public List<Map<String, Object>> selectUserNotWashCar(String orderTime,Integer betweenDay){
		return jdbcTemplate.queryForList(" select bu.user_id,bu.user_name,buv.vehicle_id,pvr.parking_id,pvr.area_id from base_user bu  join base_user_vehicle buv on  bu.user_id = buv.user_id  join parking_vehicle_rel pvr on buv.vehicle_id = pvr.vehicle_id   where    buv.vehicle_id not in ( select distinct so.vehicle_id from  serv_orders so where  abs( (date(so.order_create_time) - date(?))) <= ?    )   ",orderTime,betweenDay);
	}
	
	public  List<Map<String, Object>> selectParkingTypeIsLOAN(Long areaId,Long lotsId,String prefix,Integer start,Integer end){
		StringBuilder sql = new StringBuilder( " select ap.parking_id,ap.parking_code,ap.area_id,ap.parking_lots_id,ap.parking_floor from  area_parking  ap,parking_user_rel pur  where  ap.parking_id = pur.parking_id  and  ap.area_id =  ?  " );
		List<Object> params = new ArrayList<Object>();
		params.add(areaId);
		if(null!=lotsId){
			sql.append(" and ap.parking_lots_id = ? ");
			params.add(lotsId);
		}
		sql.append(" and pur.relation_type = 'LOAN'   and ap.parking_code like ?  and proc_get_index(ap.parking_code) >=? and  proc_get_index(ap.parking_code) <=?    ");
		params.add(prefix+"%");
		params.add(start);
		params.add(end);
		return jdbcTemplate.queryForList(sql.toString(),params.toArray());
	}
	
	
	public  List<Map<String, Object>> selectParking(Long areaId,Long lotsId,String prefix,String parkingCodes){
		StringBuilder sql = new StringBuilder( " select ap.parking_id,ap.parking_code,ap.area_id,  ba.area_name, ap.parking_lots_id, apl.parking_lots_name, ap.parking_floor,(select  bv.plat_no from parking_vehicle_rel  pvr, base_vehicle bv where pvr.vehicle_id = bv.vehicle_id  and  pvr.parking_id = ap.parking_id) plotno  from  area_parking  ap,area_parkinglots apl ,base_area ba  where ap.parking_lots_id = apl.parking_lots_id and   apl.area_id = ba.area_id and    ap.area_id =  ?  " );
		List<Object> params = new ArrayList<Object>();
		params.add(areaId);
		if(null!=lotsId){
			sql.append(" and ap.parking_lots_id = ? ");
			params.add(lotsId);
		}

		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(prefix)){

			sql.append("   and  ap.parking_code like ?    ");
			params.add(prefix+"%");
		}

		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(parkingCodes)){
			sql.append("   and  ap.parking_code in   (   " +parkingCodes +" ) " );
		}

		sql.append(" order by ap.parking_id  ");

		return jdbcTemplate.queryForList(sql.toString(),params.toArray());
	}
	
	
	public  List<Map<String, Object>> selectParkingByParkingId(Long id){
		return jdbcTemplate.queryForList("select * from area_parking where parking_id = ?",id);
	}
	
	public List<Map<String, Object>> selectVehicleById(Long id){
		return jdbcTemplate.queryForList(" select * from base_vehicle where vehicle_id =  ? ",id);
	}
	
	public  List<Map<String, Object>> selectBrand(){
		return jdbcTemplate.queryForList("select brand_id,brand_name from dim_brand");
	}
	
	public  List<Map<String, Object>> selectSeriesByBrandId(Long id){
		return jdbcTemplate.queryForList("select series_id,series_name from  dim_series where brand_id = ?",id);
	}
	
	public  List<Map<String, Object>> selectModelBySeriesId(Long id){
		return jdbcTemplate.queryForList("	select model_id,model_name from dim_vehicle_model where series_id = ? ",id);
	}
	
	public  List<Map<String, Object>> selectDetailVehicleById(Long id){
		return jdbcTemplate.queryForList("select bv.plat_no,bv.swept_volume, bv.manual_model, db.brand_name,ds.series_name,dvm.model_name  from base_vehicle bv ,dim_brand db ,dim_series ds ,dim_vehicle_model dvm where  "
				+ "bv.brand_id = db.brand_id and bv.series_id = ds.series_id and bv.model_id = dvm.model_id  and bv.vehicle_id = ? ",id);
	}
	
/*	public void
	selectUserPic(String userName,String phone,String status,Page<List<Map<String, Object>>> page) throws Exception{
		StringBuilder sql = new StringBuilder("SELECT  buu.id,  buu.user_id,  buu.upload_time,  buu.picurl, "
				+ " buu.pictype, case when  buu.picstatus = 'NEW' then '待审核' when buu.picstatus = 'PASS' then '已通过' else '未通过' end as picstatus   ,bu.user_name,bu.mobile "
				+ "FROM public.base_user_upload_pic buu,base_user bu where buu.user_id=bu.user_id ");
		List<Object> params = new ArrayList<Object>();
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)){
			sql.append(" AND bu.user_name like ? ");
			params.add("%"+userName+"%");
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(phone)){
			sql.append("  AND bu.mobile like ? ");
			params.add("%"+phone+"%");
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(status)){
			sql.append(" AND buu.picstatus = ? ");
			params.add(status);
		}
		page.setTotal(jdbcTemplate.queryForList(sql.toString(), params.toArray()).size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		page.setDatas(jdbcTemplate.queryForList(sql.toString(),params.toArray()));
		
	}*/
	

	public  List<Map<String, Object>> selectVehicleByPlatNo(String platNo){
		return 	this.jdbcTemplate.queryForList("SELECT  * FROM base_vehicle where plat_no = ?",platNo);
	}
	
	
	public  List<Map<String, Object>> selectVehicleOtherPics(Long vehicleId,String type){
		if(type.equalsIgnoreCase("VL_ORI")){
			return 	this.jdbcTemplate.queryForList("  select  *  from  base_vehicle_pic  t where t.vehicle_id = ? and t.pictype =  ?   ",vehicleId,type);
		}else{
			return 	this.jdbcTemplate.queryForList("  select  *  from  base_vehicle_pic  t where t.vehicle_id = ? and t.pictype in ('VL_CPY_A','VL_CPY_B')   ",vehicleId);
		}
	}
	
	
	/**  
	 * 查询物业端上传的图片及其相关信息
	 * @param userName
	 * @param phone
	 * @param status
	 * @param vehicleId
	 * @param page
	 * @throws Exception
	 */
	public void
	selectVehiclePic(String userName,String phone,String status,Long vehicleId, Integer type,Page<List<Map<String, Object>>> page) throws Exception{
		StringBuilder sql = new StringBuilder("   select t.vehicle_id , get_other_pics(t.vehicle_id,2) others ,  bv.plat_no,  t.picurl picurls ,t.pictype,t.picstatus,bu.user_name,bu.mobile,bv.* from  base_vehicle_pic  t, "
				+ " base_user_vehicle buv ,base_user bu ,base_vehicle bv "
				+ " where t.vehicle_id = buv.vehicle_id and buv.user_id = bu.user_id "
				+ " and t.vehicle_id = bv.vehicle_id    ");
		List<Object> params = new ArrayList<Object>();
		if(type==1){
			sql.append( "  AND  t.pictype in ( ? ) ");
			params.add("VL_ORI");
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(status)){
			if(status.equalsIgnoreCase("NEW")){
				sql.append( " AND ( t.picstatus = ? or t.picstatus = 'OLD'   )   ");
			}else{
				sql.append( " AND ( t.picstatus = ?   )   ");
			}
			params.add(status);
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)){
			sql.append(" AND bu.user_name like ? ");
			params.add("%"+userName+"%");
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(phone)){
			sql.append(" AND  bu.mobile like ? ");
			params.add("%"+phone+"%");
		}
		
		if(null!= vehicleId){
			sql.append(" AND  t.vehicle_id = ? ");
			params.add(vehicleId);
		}
		
		
		page.setTotal(jdbcTemplate.queryForList(sql.toString(), params.toArray()).size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		page.setDatas(jdbcTemplate.queryForList(sql.toString(),params.toArray()));
		
	}
	
	
	/**  
	 * 查询物业端上传的副本信息
	 * @param userName
	 * @param phone
	 * @param status
	 * @param vehicleId
	 * @param page
	 * @throws Exception
	 */
	public void
	selectVehicleEctypePic(String userName,String phone,String status,Long vehicleId, Integer type,Page<List<Map<String, Object>>> page) throws Exception{
		StringBuilder sql = new StringBuilder(" select t.vehicle_id, get_other_pics(t.vehicle_id,1) others , bv.plat_no,t.pictypes, t.picurls,t.picstatus,bu.user_name,bu.mobile,bv.* from "
				+ " (   select   p.vehicle_id,string_agg(picurl,',') picurls  ,string_agg(picstatus,',') picstatus,string_agg(pictype,',') pictypes  from    base_vehicle_pic  p WHERE  p.pictype in ( 'VL_CPY_A','VL_CPY_B')  %s  group by p.vehicle_id"
				+ "  )  t,  base_user_vehicle buv ,base_user bu ,base_vehicle bv "
				+ "where t.vehicle_id = buv.vehicle_id and buv.user_id = bu.user_id  and t.vehicle_id = bv.vehicle_id  and  bv.check_status = 'PASS' ");
		List<Object> params = new ArrayList<Object>();
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(status)){
			if(status.equalsIgnoreCase("NEW")){
				sql = new StringBuilder(String.format(sql.toString(), " AND ( p.picstatus = ? or p.picstatus = 'OLD'   ) "));
			}else{
				sql = new StringBuilder(String.format(sql.toString(), " AND ( p.picstatus = ?   ) "));
			}
			params.add(status);
		}else{
			sql = new StringBuilder(String.format(sql.toString(), "  "));
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)){
			sql.append(" AND bu.user_name like ? ");
			params.add("%"+userName+"%");
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(phone)){
			sql.append(" AND  bu.mobile like ? ");
			params.add("%"+phone+"%");
		}
		
		if(null!= vehicleId){
			sql.append(" AND  t.vehicle_id = ? ");
			params.add(vehicleId);
		}
		
		
		page.setTotal(jdbcTemplate.queryForList(sql.toString(), params.toArray()).size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		page.setDatas(jdbcTemplate.queryForList(sql.toString(),params.toArray()));
		
	}
	

	/**  
	 * 查询手机上传的图片及其相关信息
	 * @param userName
	 * @param phone
	 * @param status
	 * @param vehicleId
	 * @param page
	 * @throws Exception
	 */
	public void
	selectUserPic(String userName,String phone,String status,Long userId, Page<List<Map<String, Object>>> page) throws Exception{
	/*	StringBuilder sql = 
				new StringBuilder("  select t.*,bu.user_name,bu.mobile  "
						+ "from   base_user_upload_pic   t , "
				+ "base_user bu    where t.user_id = bu.user_id   ");*/
		
		StringBuilder sql = 
				new StringBuilder("   select  t.*,bu.user_name,bu.mobile  from (  select   p.user_id,string_agg(picurl,',') picurls  ,string_agg(picstatus,',') picstatus  "
						+ " from   base_user_upload_pic   p  %s  group by p.user_id  ) t,   "
				+ "  base_user bu    where t.user_id = bu.user_id    ");
		List<Object> params = new ArrayList<Object>();
		
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(status)){
			sql = new StringBuilder(String.format(sql.toString(), " WHERE  p.picstatus = ?  "));
			params.add(status);
		}else{
			sql = new StringBuilder(String.format(sql.toString(), " "));
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)){
			sql.append(" AND bu.user_name like ? ");
			params.add("%"+userName+"%");
		}
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(phone)){
			sql.append(" AND  bu.mobile like ? ");
			params.add("%"+phone+"%");
		}
		if(null!=userId){
			sql.append(" AND  bu.t.user_id = ? ");
			params.add(phone);
		}
		
		page.setTotal(jdbcTemplate.queryForList(sql.toString(), params.toArray()).size());
		sql.append(" limit "+page.getPageSize()+" offset  " + (page.getPageIndex()-1) * page.getPageSize());
		page.setDatas(jdbcTemplate.queryForList(sql.toString(),params.toArray()));
		
	}
	
	public Map<String, Object> selectUserUploadByUrl(String url){
		List<Map<String, Object>> data  =  this.jdbcTemplate.queryForList("select * from  base_user_upload_pic  where picurl = ?",url);
		return  (data!=null&&!data.isEmpty())?data.get(0):null;
	}

	public Map<String, Object> selectVehicleUploadByUrl(String url){
		List<Map<String, Object>> data  =  this.jdbcTemplate.queryForList("select * from  base_vehicle_pic  where picurl = ?",url);
		return  (data!=null&&!data.isEmpty())?data.get(0):null;
	}
	
}
