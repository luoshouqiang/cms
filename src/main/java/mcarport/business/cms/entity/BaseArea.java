package mcarport.business.cms.entity;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the base_area database table.
 * 
 */
@Entity
@Table(name="base_area")
@NamedQuery(name="BaseArea.findAll", query="SELECT b FROM BaseArea b")
public class BaseArea extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="area_id")
	private long areaId;

	@Column(name="allow_rent_to_change")
	private int allowRentToChange;

	@Column(name="allow_temp")
	private int allowTemp;

	@Column(name="amount_of_parking")
	private int amountOfParking;

	@Column(name="amount_of_parkinglots")
	private int amountOfParkinglots;

	@Column(name="amount_of_regular")
	private int amountOfRegular;

	@Column(name="amount_of_rent")
	private int amountOfRent;

	@Column(name="amount_of_spare")
	private int amountOfSpare;

	@Column(name="amount_of_temp")
	private int amountOfTemp;

	@Column(name="area_desc")
	private String areaDesc;

	@Column(name="area_name")
	private String areaName;

	@Column(name="city_id")
	private long cityId;

	private String contacts;

	@Column(name="county_id")
	private long countyId;

	@Column(name="cpu_id")
	private String cpuId;

	@Column(name="login_name")
	private String loginName;

	@Column(name="login_password")
	private String loginPassword;

	private String mobile;

	private BigInteger period;

	@Column(name="province_id")
	private long provinceId;

	@Column(name="supplier_id")
	private long supplierId;
	
	@Column(name="weixin_id")
	private int weid;

	public BaseArea() {
	}

	public long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public int getAllowRentToChange() {
		return this.allowRentToChange;
	}

	public void setAllowRentToChange(int allowRentToChange) {
		this.allowRentToChange = allowRentToChange;
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

	public int getAmountOfParkinglots() {
		return this.amountOfParkinglots;
	}

	public void setAmountOfParkinglots(int amountOfParkinglots) {
		this.amountOfParkinglots = amountOfParkinglots;
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

	public String getAreaDesc() {
		return this.areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public long getCityId() {
		return this.cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getContacts() {
		return this.contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public long getCountyId() {
		return this.countyId;
	}

	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}

	public String getCpuId() {
		return this.cpuId;
	}

	public void setCpuId(String cpuId) {
		this.cpuId = cpuId;
	}


	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return this.loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigInteger getPeriod() {
		return this.period;
	}

	public void setPeriod(BigInteger period) {
		this.period = period;
	}

	public long getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public long getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public int getWeid() {
		return this.weid;
	}

	public void setWeid(int weid) {
		this.weid = weid;
	}

}