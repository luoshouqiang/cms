package mcarport.business.cms.push;

/**
 * 百度推送IOS消息格式
 * @author luosq
 *
 */
public class BaiduIosPushMessage {
	
	private APS aps;
	private String msgJson;
	
	/**
	 * @return the aps
	 */
	public APS getAps() {
		return aps;
	}

	/**
	 * @param aps the aps to set
	 */
	public void setAps(APS aps) {
		this.aps = aps;
	}

	/**
	 * @return the msgJson
	 */
	public String getMsgJson() {
		return msgJson;
	}

	/**
	 * @param msgJson the msgJson to set
	 */
	public void setMsgJson(String msgJson) {
		this.msgJson = msgJson;
	}

	class APS{
		private String alert;
		private String sound;
		private int badge;
		/**
		 * @return the alert
		 */
		public String getAlert() {
			return alert;
		}
		/**
		 * @param alert the alert to set
		 */
		public void setAlert(String alert) {
			this.alert = alert;
		}
		/**
		 * @return the sound
		 */
		public String getSound() {
			return sound;
		}
		/**
		 * @param sound the sound to set
		 */
		public void setSound(String sound) {
			this.sound = sound;
		}
		/**
		 * @return the badge
		 */
		public int getBadge() {
			return badge;
		}
		/**
		 * @param badge the badge to set
		 */
		public void setBadge(int badge) {
			this.badge = badge;
		}
		
	}
}
