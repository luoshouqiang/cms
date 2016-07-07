package mcarport.business.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the area_parking database table.
 * 
 */
@Entity
@Table(name = "area_parking")
@NamedQuery(name = "AreaParking.findAll", query = "SELECT a FROM AreaParking a")
public class AreaParking  extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "parking_id")
	private long parkingId;

	@Column(name = "area_id")
	private long areaId;

	@Column(name = "custom_code")
	private String customCode;

	@Column(name = "parking_code")
	private String parkingCode;
	
	@Column(name = "parking_lots_id")
	private long parkingLotsId;

	@Column(name = "parking_desc")
	private String parkingDesc;

	@Column(name = "parking_floor")
	private int parkingFloor;

	@Column(name = "parking_location")
	private String parkingLocation;

	@Column(name = "parking_space")
	private String parkingSpace;

	@Column(name = "parking_type")
	private int parkingType;

	public AreaParking() {
	}

	public long getParkingId() {
		return parkingId;
	}

	public void setParkingId(long parkingId) {
		this.parkingId = parkingId;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getCustomCode() {
		return this.customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getParkingCode() {
		return this.parkingCode;
	}

	public void setParkingCode(String parkingCode) {
		this.parkingCode = parkingCode;
	}

	public String getParkingDesc() {
		return this.parkingDesc;
	}

	public void setParkingDesc(String parkingDesc) {
		this.parkingDesc = parkingDesc;
	}

	public int getParkingFloor() {
		return this.parkingFloor;
	}

	public void setParkingFloor(int parkingFloor) {
		this.parkingFloor = parkingFloor;
	}

	public String getParkingLocation() {
		return this.parkingLocation;
	}

	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}

	public String getParkingSpace() {
		return this.parkingSpace;
	}

	public void setParkingSpace(String parkingSpace) {
		this.parkingSpace = parkingSpace;
	}

	public int getParkingType() {
		return this.parkingType;
	}

	public void setParkingType(int parkingType) {
		this.parkingType = parkingType;
	}

	public long getParkingLotsId() {
		return parkingLotsId;
	}

	public void setParkingLotsId(long parkingLotsId) {
		this.parkingLotsId = parkingLotsId;
	}

}