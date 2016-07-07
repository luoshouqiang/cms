package mcarport.business.cms.dto;

import com.mysql.jdbc.StringUtils;

/**
 *  租赁关系
 * @author Administrator
 *
 */
public enum RelationType {
	
	SELF("购买"),LOAN("租赁"),TEMP("无");
	
	private String status;
	
	private RelationType(String status){
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public static  RelationType getType(String status){
		if(StringUtils.isNullOrEmpty(status)){
			return null;
		}
		RelationType[] rts =	RelationType.values();
		for (RelationType relationType : rts) {
			if(relationType.getStatus().equals(status)){
				return relationType;
			}
		}	
		return null;
	}
	
	
}
