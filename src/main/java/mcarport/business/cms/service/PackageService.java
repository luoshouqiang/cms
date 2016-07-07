package mcarport.business.cms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mcarport.business.cms.dto.OrderStatus;
import mcarport.business.cms.dto.PackageInsurOrder;
import mcarport.business.cms.dto.PackageOrder;
import mcarport.business.cms.dto.PackageOrderDetail;
import mcarport.business.cms.dto.SearchBean;
import mcarport.business.cms.exception.BusinessException;
import mcarport.business.cms.utils.DateUtils;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class PackageService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryAllPackageItem() {
		return jdbcTemplate.queryForList(" select package_item_id,item_name,item_provide_type,"
				+ " item_cost_price,item_market_price,remark from package_item");
	}

	public void saveInsurOrder(final PackageInsurOrder order) {
		final String sql = "insert into package_insur(insur_name,user_id,vehicle_id,insur_price,"
				+ "insur_content,insur_status,created_time)values(?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, order.getInsurName());
				ps.setLong(2, order.getUserId());
				ps.setLong(3, order.getVehicleId());
				ps.setBigDecimal(4, order.getInsurPrice());
				ps.setString(5, order.getInsurContent());
				ps.setString(6, OrderStatus.NO_PAY.name());
				ps.setDate(7, new java.sql.Date(new Date().getTime()));
				return ps;
			}
		});
	}

	public void saveOrder(final PackageOrder order) {
		if (order == null) {
			throw new BusinessException("订单数据错误");
		}
		if (CollectionUtils.isEmpty(order.getItemList())) {
			throw new BusinessException("订单数据错误");
		}
		final List<PackageOrderDetail> lists = order.getItemList();

		GeneratedKeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			String sql = "insert into package_user(package_name,user_id,vehicle_id,package_price,"
					+ "actual_price,package_status,created_time) values(?,?,?,?,?,?,?)";

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql.toString(), new String[] { "package_id" });
				ps.setString(1, "套餐包");
				ps.setLong(2, order.getUserId());
				ps.setLong(3, order.getVehicleId());
				ps.setBigDecimal(4, order.getActualPrice());
				ps.setBigDecimal(5, order.getActualPrice());
				ps.setString(6, OrderStatus.NO_PAY.name());
				ps.setDate(7, new java.sql.Date(new Date().getTime()));
				return ps;
			}

		}, key);

		final long packageId = (Long) key.getKey();
		String insertDetailSql = "insert into package_user_detail(package_id,package_item_id,total_times,used_times)"
				+ " values(?,?,?,?)";
		jdbcTemplate.batchUpdate(insertDetailSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PackageOrderDetail detail = lists.get(i);
				ps.setLong(1, packageId);
				ps.setLong(2, detail.getPackageItemId());
				ps.setLong(3, detail.getTotalTimes());
				ps.setLong(4, 0);
			}

			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});

	}

	public void processOrder(final long packgeId, final String orderStatus) {
		if (!checkStatus(orderStatus)) {
			throw new BusinessException("状态不对");
		}
		String sql = "update package_user set package_status= ? where package_id=?";
		jdbcTemplate.update(sql, orderStatus, packgeId);

	}

	public void processInsurOrder(final long insurId, final String orderStatus) {
		if (!checkStatus(orderStatus)) {
			throw new BusinessException("状态不对");
		}
		String sql = "update package_insur set insur_status= ? where insur_id=?";
		jdbcTemplate.update(sql, orderStatus, insurId);

	}

	private boolean checkStatus(String orderStatus) {
		try {
			OrderStatus status = OrderStatus.valueOf(orderStatus);
			if (status.equals(OrderStatus.ORDER_CANCEL) || status.equals(OrderStatus.SERVICE_FINISHED)) {
				return true;
			} else
				return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public List<Map<String, Object>> queryInsurOrder(SearchBean queryData) throws DateParseException {
		StringBuffer sql = new StringBuffer(
				"select insur_id,insur_name,insur_status, area_name,user_name,u.mobile,insur_price,insur_content,pu.created_time"
						+ " from package_insur pu,base_area a,base_area_user bau,base_user u"
						+ " where pu.user_id=u.user_id  and u.user_id=bau.user_id and bau.area_id=a.area_id ");
		if (queryData.getOrderId()!=null) {
			sql.append(" and pu.insur_id=" +queryData.getOrderId());
		}
		if (!StringUtils.isEmpty(queryData.getStartTime())) {
			sql.append(" and pu.created_time>='" +queryData.getStartTime()+"'");
		}
		if (!StringUtils.isEmpty(queryData.getEndTime())) {
			sql.append(" and pu.created_time<='" + queryData.getEndTime()+"'");
		}
		if (!StringUtils.isEmpty(queryData.getOrderId())) {
			sql.append(" and pu.insur_id<=" + queryData.getOrderId());
		}
		if (!StringUtils.isEmpty(queryData.getPhone())) {
			sql.append(" and u.mobile='" + queryData.getPhone()+"'");
		}
		if (!StringUtils.isEmpty(queryData.getStatus())) {
			sql.append(" and pu.insur_status='" + queryData.getStatus() + "'");
		}
		if (!StringUtils.isEmpty(queryData.getUserName())) {
			sql.append(" and u.user_name like '%" + queryData.getUserName()+"%'");
		}
	
		sql.append(" order by pu.created_time desc");
		List<Map<String, Object>> orderList = jdbcTemplate.queryForList(sql.toString());
		if (CollectionUtils.isEmpty(orderList)) {
			return Collections.emptyList();
		} else {
			for (Map<String, Object> map : orderList) {
				String orderStatus = (String) map.get("insur_status");
				Date createTime = (Date) map.get("created_time") == null ? new Date() : (Date) map.get("created_time");
				map.put("insur_status", OrderStatus.valueOf(orderStatus).getMsg());
				map.put("created_time", DateUtil.formatDate(createTime, "yyyy-MM-dd"));
			}
		}
		return orderList;

	}

	public List<Map<String, Object>> queryPackageOrder(SearchBean queryData) throws DateParseException {
		StringBuffer sql = new StringBuffer(
				" select area_name,user_name,u.mobile, package_id,package_name,package_price,actual_price,pu.package_status"
						+ " from package_user pu,base_vehicle v,base_area a,base_area_user bau,base_user u"
						+ " where pu.user_id=u.user_id and pu.vehicle_id=v.vehicle_id and u.user_id=bau.user_id and bau.area_id=a.area_id");
		if (queryData.getOrderId()!=null) {
			sql.append(" and pu.package_id=" +queryData.getOrderId());
		}
		if (!StringUtils.isEmpty(queryData.getStartTime())) {
			sql.append(" and pu.created_time>='" +queryData.getStartTime()+"'");
		}
		if (!StringUtils.isEmpty(queryData.getEndTime())) {
			sql.append(" and pu.created_time<='" + queryData.getEndTime()+"'");
		}
		if (!StringUtils.isEmpty(queryData.getOrderId())) {
			sql.append(" and pu.package_id<=" + queryData.getOrderId());
		}
		if (!StringUtils.isEmpty(queryData.getPhone())) {
			sql.append(" and u.mobile='" + queryData.getPhone()+"'");
		}
		if (!StringUtils.isEmpty(queryData.getStatus())) {
			sql.append(" and pu.package_status='" + queryData.getStatus() + "'");
		}
		if (!StringUtils.isEmpty(queryData.getUserName())) {
			sql.append(" and u.user_name like '%" + queryData.getUserName()+"%'");
		}

		sql.append(" order by pu.created_time desc");
		List<Map<String, Object>> orderList = jdbcTemplate.queryForList(sql.toString());
		if (CollectionUtils.isEmpty(orderList)) {
			return Collections.emptyList();
		}
		sql = new StringBuffer(
				"select item.item_name,total_times,used_times from package_user_detail pud,package_item item"
						+ " where pud.package_item_id=item.package_item_id and pud.package_id=?");
		for (Map<String, Object> map : orderList) {
			List<Map<String, Object>> detailList = jdbcTemplate.queryForList(sql.toString(),
					(Long) map.get("package_id"));
			String orderStatus = (String) map.get("package_status");
			Date createTime = (Date) map.get("created_time") == null ? new Date() : (Date) map.get("created_time");
			map.put("created_time", DateUtil.formatDate(createTime, "yyyy-MM-dd"));
			map.put("package_status", OrderStatus.valueOf(orderStatus).getMsg());
			map.put("detailList", detailList);
		}
		return orderList;
	}

	public List<Map<String, Object>> packageOrderDetail(long packageId) throws DateParseException {
		StringBuffer sql = new StringBuffer(
				" select area_name,user_name,u.mobile, package_id,package_name,package_price,actual_price,pu.package_status"
						+ " from package_user pu,base_vehicle v,base_area a,base_area_user bau,base_user u"
						+ " where pu.user_id=u.user_id and pu.vehicle_id=v.vehicle_id and u.user_id=bau.user_id and bau.area_id=a.area_id and pu.package_id=?");

		sql.append(" order by pu.created_time desc");
		List<Map<String, Object>> orderList = jdbcTemplate.queryForList(sql.toString(), packageId);
		if (CollectionUtils.isEmpty(orderList)) {
			return Collections.emptyList();
		}
		sql = new StringBuffer(
				"select item.item_name,total_times,used_times from package_user_detail pud,package_item item"
						+ " where pud.package_item_id=item.package_item_id and pud.package_id=?");
		for (Map<String, Object> map : orderList) {
			List<Map<String, Object>> detailList = jdbcTemplate.queryForList(sql.toString(),
					(Long) map.get("package_id"));
			String orderStatus = (String) map.get("package_status");
			Date createTime = (Date) map.get("created_time") == null ? new Date() : (Date) map.get("created_time");
			map.put("created_time", DateUtil.formatDate(createTime, "yyyy-MM-dd"));
			map.put("package_status", OrderStatus.valueOf(orderStatus).getMsg());
			map.put("detailList", detailList);
		}
		return orderList;
	}

}
