package mcarport.business.cms.dto;

import java.math.BigDecimal;
import java.util.List;

public class PackageOrder {
	
	public static enum status{
		NOPAY,PAIED
	}
	// private long packageId;
	private String packageName;
	private long userId;
	private long vehicleId;
	private BigDecimal packagePrice;
	private BigDecimal insurPrice;
	private String insurContent;
	private String packageBeginDate;
	private String packageEndDate;
	private BigDecimal actualPrice;
	private String packageStatus;
	private List<PackageOrderDetail> itemList;
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
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
	 * @param vehicleId
	 *            the vehicleId to set
	 */
	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	/**
	 * @return the packagePrice
	 */
	public BigDecimal getPackagePrice() {
		return packagePrice;
	}

	/**
	 * @param packagePrice
	 *            the packagePrice to set
	 */
	public void setPackagePrice(BigDecimal packagePrice) {
		this.packagePrice = packagePrice;
	}

	/**
	 * @return the insurPrice
	 */
	public BigDecimal getInsurPrice() {
		return insurPrice;
	}

	/**
	 * @param insurPrice
	 *            the insurPrice to set
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
	 * @param insurContent
	 *            the insurContent to set
	 */
	public void setInsurContent(String insurContent) {
		this.insurContent = insurContent;
	}

	/**
	 * @return the packageBeginDate
	 */
	public String getPackageBeginDate() {
		return packageBeginDate;
	}

	/**
	 * @param packageBeginDate
	 *            the packageBeginDate to set
	 */
	public void setPackageBeginDate(String packageBeginDate) {
		this.packageBeginDate = packageBeginDate;
	}

	/**
	 * @return the packageEndDate
	 */
	public String getPackageEndDate() {
		return packageEndDate;
	}

	/**
	 * @param packageEndDate
	 *            the packageEndDate to set
	 */
	public void setPackageEndDate(String packageEndDate) {
		this.packageEndDate = packageEndDate;
	}

	/**
	 * @return the actualPrice
	 */
	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	/**
	 * @param actualPrice
	 *            the actualPrice to set
	 */
	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	/**
	 * @return the packageStatus
	 */
	public String getPackageStatus() {
		return packageStatus;
	}

	/**
	 * @param packageStatus
	 *            the packageStatus to set
	 */
	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}

	/**
	 * @return the itemList
	 */
	public List<PackageOrderDetail> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(List<PackageOrderDetail> itemList) {
		this.itemList = itemList;
	}
	
}
