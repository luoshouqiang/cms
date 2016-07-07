package mcarport.business.cms.push;

/**
 * 百度安卓推送消息格式
 * @author luosq
 *
 */
public class BaiduAndroidPushMessage {
	
	/**通知标题，可以为空；如果为空则设为appid对应的应用名;*/
	private String title;
	/**通知文本内容，不能为空;*/
	private String description;
	/***/
	private int notification_builder_id;
	private int notification_basic_style;
	/**点击通知后的行为(1：打开Url; 2：自定义行为；3：默认打开应用;);*/
	private int open_type;
	/**需要打开的Url地址，open_type为1时才有效*/
	private String url;
	/**open_type为2时才有效，
	 * Android端SDK会把pkg_content字符串转换成Android Intent,
	 * 通过该Intent打开对应app组件，
	 * 所以pkg_content字符串格式必须遵循Intent uri格式，
	 * 最简单的方法可以通过Intent方法toURI()获取*/
	private String pkg_content;
	
	/**自定义*/
	private String custom_content;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the notification_builder_id
	 */
	public int getNotification_builder_id() {
		return notification_builder_id;
	}
	/**
	 * @param notification_builder_id the notification_builder_id to set
	 */
	public void setNotification_builder_id(int notification_builder_id) {
		this.notification_builder_id = notification_builder_id;
	}
	/**
	 * @return the notification_basic_style
	 */
	public int getNotification_basic_style() {
		return notification_basic_style;
	}
	/**
	 * @param notification_basic_style the notification_basic_style to set
	 */
	public void setNotification_basic_style(int notification_basic_style) {
		this.notification_basic_style = notification_basic_style;
	}
	/**
	 * @return the open_type
	 */
	public int getOpen_type() {
		return open_type;
	}
	/**
	 * @param open_type the open_type to set
	 */
	public void setOpen_type(int open_type) {
		this.open_type = open_type;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the pkg_content
	 */
	public String getPkg_content() {
		return pkg_content;
	}
	/**
	 * @param pkg_content the pkg_content to set
	 */
	public void setPkg_content(String pkg_content) {
		this.pkg_content = pkg_content;
	}
	/**
	 * @return the custom_content
	 */
	public String getCustom_content() {
		return custom_content;
	}
	/**
	 * @param custom_content the custom_content to set
	 */
	public void setCustom_content(String custom_content) {
		this.custom_content = custom_content;
	}
	

}
