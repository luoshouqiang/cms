package mcarport.business.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the area_parkinglots database table.
 * 
 */
@Entity
@Table(name="area_parkinglots")
@NamedQuery(name="AreaParkinglot.findAll", query="SELECT a FROM AreaParkinglot a")
public class AreaParkinglot extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="parking_lots_id")
	private long parkingLotsId;

	@Column(name="allow_temp")
	private int allowTemp;

	@Column(name="amount_of_parking")
	private int amountOfParking;

	@Column(name="amount_of_regular")
	private int amountOfRegular;

	@Column(name="amount_of_rent")
	private int amountOfRent;

	@Column(name="amount_of_spare")
	private int amountOfSpare;

	@Column(name="amount_of_temp")
	private int amountOfTemp;

	@Column(name="area_id")
	private long areaId;

	@Column(name="parking_lots_desc")
	private String parkingLotsDesc;

	@Column(name="parking_lots_name")
	private String parkingLotsName;

	public AreaParkinglot() {
	}

	public long getParkingLotsId() {
		return this.parkingLotsId;
	}

	public void setParkingLotsId(long parkingLotsId) {
		this.parkingLotsId = parkingLotsId;
	}

	public int getAllowTemp() {
		return this.allowTemp;
	}

	public void setAllowTemp(int allowTemp) {
		this.allowTemp = allowTemp;
	}

	public int getAmountOfParking() {
		return this.amountOfParking;
	}

	public void setAmountOfParking(int amountOfParking) {
		this.amountOfParking = amountOfParking;
	}

	public int getAmountOfRegular() {
		return this.amountOfRegular;
	}

	public void setAmountOfRegular(int amountOfRegular) {
		this.amountOfRegular = amountOfRegular;
	}

	public int getAmountOfRent() {
		return this.amountOfRent;
	}

	public void setAmountOfRent(int amountOfRent) {
		this.amountOfRent = amountOfRent;
	}

	public int getAmountOfSpare() {
		return this.amountOfSpare;
	}

	public void setAmountOfSpare(int amountOfSpare) {
		this.amountOfSpare = amountOfSpare;
	}

	public int getAmountOfTemp() {
		return this.amountOfTemp;
	}

	public void setAmountOfTemp(int amountOfTemp) {
		this.amountOfTemp = amountOfTemp;
	}

	public long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getParkingLotsDesc() {
		return this.parkingLotsDesc;
	}

	public void setParkingLotsDesc(String parkingLotsDesc) {
		this.parkingLotsDesc = parkingLotsDesc;
	}

	public String getParkingLotsName() {
		return this.parkingLotsName;
	}

	public void setParkingLotsName(String parkingLotsName) {
		this.parkingLotsName = parkingLotsName;
	}

}