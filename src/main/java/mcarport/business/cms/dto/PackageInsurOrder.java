package mcarport.business.cms.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PackageInsurOrder {
	private String insurName;
	private long userId;
	private long vehicleId;
	private Date beginDate;
	private Date endDate;
	private BigDecimal insurPrice;
	private String insurContent;
	/**
	 * @return the insurName
	 */
	public String getInsurName() {
		return insurName;
	}
	/**
	 * @param insurName the insurName to set
	 */
	public void setInsurName(String insurName) {
		this.insurName = insurName;
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
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the insurPrice
	 */
	public BigDecimal getInsurPrice() {
		return insurPrice;
	}
	/**
	 * @param insurPrice the insurPrice to set
	 */
	public void setInsurPrice(BigDecimal insurPrice) {
		this.insurPrice = insurPrice;
	}
	/**
	 * @return the insurContent
	 */
	public String getInsurContent() {
		return insurContent;
	}
	/**
	 * @param insurContent the insurContent to set
	 */
	public void setInsurContent(String insurContent) {
		this.insurContent = insurContent;
	}
	
	
	
}
