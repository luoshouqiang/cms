package mcarport.business.cms.dto;

import java.sql.Date;

public class UserInfo {
	private long userId;
	private String userName;
	private String phoneNum;
	private String parkingNo;
	private String parkingType;
	private String parkingSpace;
	private String platNo;
	private String dueDate;
	private String vehicleStatus;
	private String parkingId;
	
	private String roomNo;

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	private transient String loginName;
	
	private Long lotsId;
	
	private String areaName;
	
public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

public Long getLotsId() {
		return lotsId;
	}

	public void setLotsId(Long lotsId) {
		this.lotsId = lotsId;
	}
private String plotsName;
	
	public String getPlotsName() {
		return plotsName;
	}

	public void setPlotsName(String plotsName) {
		this.plotsName = plotsName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	private long vehicleId;
	private long areaId;
	private Long payId; //支付类型ID
	
	private String sweptVolume; //排量
	
	private String cardId;//身份证编号
	
	private String houseId; //房间编号
	
	private String  way ; // 拥有方式
	
	private String  dueCode="today";//到期时间前台编码
	
	private  Date  codeToDate; //前台编码对应时间
	
	
	public String getDueCode() {
		return dueCode;
	}
	public void setDueCode(String dueCode) {
		this.dueCode = dueCode;
	}
	public Date getCodeToDate() {
		return codeToDate;
	}
	public void setCodeToDate(Date codeToDate) {
		this.codeToDate = codeToDate;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getHouseId() {
		return houseId;
	}
	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * @return the parkingNo
	 */
	public String getParkingNo() {
		return parkingNo;
	}
	/**
	 * @param parkingNo the parkingNo to set
	 */
	public void setParkingNo(String parkingNo) {
		this.parkingNo = parkingNo;
	}
	/**
	 * @return the parkingType
	 */
	public String getParkingType() {
		return parkingType;
	}
	/**
	 * @param parkingType the parkingType to set
	 */
	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}
	/**
	 * @return the parkingSpace
	 */
	public String getParkingSpace() {
		return parkingSpace;
	}
	/**
	 * @param parkingSpace the parkingSpace to set
	 */
	public void setParkingSpace(String parkingSpace) {
		this.parkingSpace = parkingSpace;
	}
	/**
	 * @return the platNo
	 */
	public String getPlatNo() {
		return platNo;
	}
	/**
	 * @param platNo the platNo to set
	 */
	public void setPlatNo(String platNo) {
		this.platNo = platNo;
	}
	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the vehicleStatus
	 */
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	/**
	 * @param vehicleStatus the vehicleStatus to set
	 */
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	/**
	 * @return the parkingId
	 */
	public String getParkingId() {
		return parkingId;
	}
	/**
	 * @param parkingId the parkingId to set
	 */
	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * @return the vehicleId
	 */
	public long getVehicleId() {
		return vehicleId;
	}
	/**
	 * @param vehicleId the vehicleId to set
	 */
	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public String getSweptVolume() {
		return sweptVolume;
	}
	public void setSweptVolume(String sweptVolume) {
		this.sweptVolume = sweptVolume;
	}
	public Long getPayId() {
		return payId;
	}
	public void setPayId(Long payId) {
		this.payId = payId;
	}
	
}
