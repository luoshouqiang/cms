package mcarport.business.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the base_user_vehicle database table.
 * 
 */
@Entity
@Table(name = "base_user_vehicle")
@NamedQuery(name = "BaseUserVehicle.findAll", query = "SELECT b FROM BaseUserVehicle b")
public class BaseUserVehicle extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "begin_date")
	private Date beginDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "relation_status")
	private String relationStatus;

	@Column(name = "relation_type")
	private String relationType;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "vehicle_id")
	private long vehicleId;
	
	@Column(name = "is_allow_auto_pay")
	private int  allowAutoPay;

	public BaseUserVehicle() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRelationStatus() {
		return this.relationStatus;
	}

	public void setRelationStatus(String relationStatus) {
		this.relationStatus = relationStatus;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	/**
	 * @return the allowAutoPay
	 */
	public int getAllowAutoPay() {
		return allowAutoPay;
	}

	/**
	 * @param allowAutoPay the allowAutoPay to set
	 */
	public void setAllowAutoPay(int allowAutoPay) {
		this.allowAutoPay = allowAutoPay;
	}
	

}