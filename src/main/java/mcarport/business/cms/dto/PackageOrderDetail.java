package mcarport.business.cms.dto;

public class PackageOrderDetail {
	private long packageId;
	private long packageItemId;
	private int totalTimes;
	private int usedTimes;
	/**
	 * @return the packageId
	 */
	public long getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}
	/**
	 * @return the packageItemId
	 */
	public long getPackageItemId() {
		return packageItemId;
	}
	/**
	 * @param packageItemId the packageItemId to set
	 */
	public void setPackageItemId(long packageItemId) {
		this.packageItemId = packageItemId;
	}
	/**
	 * @return the totalTimes
	 */
	public int getTotalTimes() {
		return totalTimes;
	}
	/**
	 * @param totalTimes the totalTimes to set
	 */
	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}
	/**
	 * @return the usedTimes
	 */
	public int getUsedTimes() {
		return usedTimes;
	}
	/**
	 * @param usedTimes the usedTimes to set
	 */
	public void setUsedTimes(int usedTimes) {
		this.usedTimes = usedTimes;
	}
	
}
