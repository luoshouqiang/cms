package mcarport.business.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

/**
 * The persistent class for the parking_vehicle_rel database table.
 * 
 */

@Entity
@Table(name = "parking_vehicle_rel")
@NamedQuery(name = "ParkingVehicleRel.findAll", query = "SELECT p FROM ParkingVehicleRel p")
public class ParkingVehicleRel extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "area_id")
	private Long areaId;

	@Column(name = "parking_id")
	private Long parkingId;

	@Column(name = "vehicle_id")
	private Long vehicleId;

	public ParkingVehicleRel() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the areaId
	 */
	public Long getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	/**
	 * @return the parkingId
	 */
	public Long getParkingId() {
		return parkingId;
	}

	/**
	 * @param parkingId
	 *            the parkingId to set
	 */
	public void setParkingId(Long parkingId) {
		this.parkingId = parkingId;
	}

	/**
	 * @return the vehicleId
	 */
	public Long getVehicleId() {
		return vehicleId;
	}

	/**
	 * @param vehicleId
	 *            the vehicleId to set
	 */
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

}