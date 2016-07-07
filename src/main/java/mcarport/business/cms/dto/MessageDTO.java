package mcarport.business.cms.dto;

public class MessageDTO {
		
	  public static final String NOTICE="notice";
	  
	  public static final String PERSON="person";
	  
		private Long id;
		private String time;
		private String title;
		private String description;
		private String detail;
		private String messageType;
		private String url;
		public MessageDTO(){
			
		}
		public MessageDTO(long id,String title,String detail,String msgType){
			this.id=id;
			this.title=title;
			this.detail=detail;
			this.messageType=msgType;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * @return the messageType
		 */
		public String getMessageType() {
			return messageType;
		}
		/**
		 * @param messageType the messageType to set
		 */
		public void setMessageType(String messageType) {
			this.messageType = messageType;
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
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MessageDTO [id=" + id + ", time=" + time + ", title=" + title + ", description=" + description
					+ ", detail=" + detail + ", messageType=" + messageType + ", url=" + url + "]";
		}
		
	
		
}
