package mcarport.business.cms.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the base_vehicle database table.
 * 
 */
@Entity
@Table(name = "base_vehicle")
@NamedQueries({
		@NamedQuery(name = "BaseVehicle.findAll", query = "SELECT b FROM BaseVehicle b"),
		@NamedQuery(name="selectCarInfoList",query="select bv,u,bu"
			+ " from BaseUserVehicle as u, BaseVehicle as bv"
			+ ",BaseUser as bu "
			+ " where u.userId=bu.userId and u.isvalid=0 and u.vehicleId=bv.vehicleId and u.userId=?1")
})
public class BaseVehicle extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vehicle_id")
	private long vehicleId;

	@Column(name = "brand_id")
	private long brandId;

	@Column(name = "color_name")
	private String colorName;

	@Column(name = "is_lock_by_manager")
	private int isLockByManager;

	@Column(name = "is_lock_by_user")
	private int isLockByUser;


	@Column(name = "lock_area_id_by_user")
	private long lockAreaIdByUser;

	@Column(name = "model_id")
	private long modelId;

	@Column(name = "model_size")
	private String modelSize;

	@Column(name = "plat_no")
	private String platNo;

	@Column(name = "series_id")
	private long seriesId;
	
	@Column(name="manual_model")
	private String manualModel;
	
	@Column(name="friend_name")
	private String friendName;
	
	@Column(name="friend_mobile")
	private String friendMobile;
	

	public BaseVehicle() {
	}

	public long getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getColorName() {
		return this.colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public int getIsLockByManager() {
		return this.isLockByManager;
	}

	public void setIsLockByManager(int isLockByManager) {
		this.isLockByManager = isLockByManager;
	}

	public int getIsLockByUser() {
		return this.isLockByUser;
	}

	public void setIsLockByUser(int isLockByUser) {
		this.isLockByUser = isLockByUser;
	}


	public long getLockAreaIdByUser() {
		return this.lockAreaIdByUser;
	}

	public void setLockAreaIdByUser(long lockAreaIdByUser) {
		this.lockAreaIdByUser = lockAreaIdByUser;
	}

	public long getModelId() {
		return this.modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
	}

	public String getModelSize() {
		return this.modelSize;
	}

	public void setModelSize(String modelSize) {
		this.modelSize = modelSize;
	}

	public String getPlatNo() {
		return this.platNo;
	}

	public void setPlatNo(String platNo) {
		this.platNo = platNo;
	}

	public long getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesId(long seriesId) {
		this.seriesId = seriesId;
	}

	public String getManualModel() {
		return manualModel;
	}

	public void setManualModel(String manualModel) {
		this.manualModel = manualModel;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendMobile() {
		return friendMobile;
	}

	public void setFriendMobile(String friendMobile) {
		this.friendMobile = friendMobile;
	}


}