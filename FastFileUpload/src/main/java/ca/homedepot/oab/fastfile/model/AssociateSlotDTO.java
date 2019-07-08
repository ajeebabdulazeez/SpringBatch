package ca.homedepot.oab.fastfile.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "associate_slot_bkp")
public class AssociateSlotDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "slot_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int slotId;

	@Column(name = "slot_date")
	private Date slotDate;

	@Column(name = "slot_store_no")
	private int slotStoreNo;

	@Column(name = "type_id")
	private String typeId;

	@Column(name = "slot_ldap")
	private String slotLdap;

	@Column(name = "slot_availability")
	private String slotAvailability;

	@Column(name = "slot_createdts")
	private Timestamp slotCreatedts;

	@Column(name = "slot_updatedts")
	private Timestamp slotUpdatedts;

	/**
	 * @return the slotId
	 */
	public int getSlotId() {
		return slotId;
	}

	/**
	 * @param slotId the slotId to set
	 */
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	/**
	 * @return the slotDate
	 */
	public Date getSlotDate() {
		return slotDate;
	}

	/**
	 * @param slotDate the slotDate to set
	 */
	public void setSlotDate(Date slotDate) {
		this.slotDate = slotDate;
	}

	/**
	 * @return the slotStoreNo
	 */
	public int getSlotStoreNo() {
		return slotStoreNo;
	}

	/**
	 * @param slotStoreNo the slotStoreNo to set
	 */
	public void setSlotStoreNo(int slotStoreNo) {
		this.slotStoreNo = slotStoreNo;
	}

	/**
	 * @return the typeId
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the slotLdap
	 */
	public String getSlotLdap() {
		return slotLdap;
	}

	/**
	 * @param slotLdap the slotLdap to set
	 */
	public void setSlotLdap(String slotLdap) {
		this.slotLdap = slotLdap;
	}

	/**
	 * @return the slotAvailability
	 */
	public String getSlotAvailability() {
		return slotAvailability;
	}

	/**
	 * @param slotAvailability the slotAvailability to set
	 */
	public void setSlotAvailability(String slotAvailability) {
		this.slotAvailability = slotAvailability;
	}

	/**
	 * @return the slotCreatedts
	 */
	public Timestamp getSlotCreatedts() {
		return slotCreatedts;
	}

	/**
	 * @param slotCreatedts the slotCreatedts to set
	 */
	public void setSlotCreatedts(Timestamp slotCreatedts) {
		this.slotCreatedts = slotCreatedts;
	}

	/**
	 * @return the slotUpdatedts
	 */
	public Timestamp getSlotUpdatedts() {
		return slotUpdatedts;
	}

	/**
	 * @param slotUpdatedts the slotUpdatedts to set
	 */
	public void setSlotUpdatedts(Timestamp slotUpdatedts) {
		this.slotUpdatedts = slotUpdatedts;
	}

}
