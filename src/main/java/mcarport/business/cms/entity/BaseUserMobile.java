package mcarport.business.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the base_user_mobile database table.
 * 
 */
@Entity
@Table(name="base_user_mobile")
@NamedQuery(name="BaseUserMobile.findAll", query="SELECT b FROM BaseUserMobile b")
public class BaseUserMobile extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(name="device_id")
	private String deviceId;

	@Column(name="mobile_numer")
	private String mobileNumer;

	@Column(name="os_type")
	private String osType;

	@Column(name="user_id")
	private long userId;

	public BaseUserMobile() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMobileNumer() {
		return this.mobileNumer;
	}

	public void setMobileNumer(String mobileNumer) {
		this.mobileNumer = mobileNumer;
	}

	public String getOsType() {
		return this.osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}