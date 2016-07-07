package mcarport.business.cms.push;

import java.util.List;

/**
 * 百度推送客户信息，
 * 用来bind系统和推送信息
 * @author luosq
 *
 */
public class PushClientUser {
	/**百度push的userID*/
	private String userId;
	/**百度push的channelId*/
	private long channelId;
	/**自己系统的账户*/
	private long sysUserId;
	/**客户的手机系统*/
	private  int  deviceType;
	
	/**用户所属的tag*/
	private List<String> tag;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the channelId
	 */
	public long getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the sysUserId
	 */
	public long getSysUserId() {
		return sysUserId;
	}
	/**
	 * @param sysUserId the sysUserId to set
	 */
	public void setSysUserId(long sysUserId) {
		this.sysUserId = sysUserId;
	}
	/**
	 * @return the deviceType
	 */
	public int getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the tag
	 */
	public List<String> getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	
	
	
}
