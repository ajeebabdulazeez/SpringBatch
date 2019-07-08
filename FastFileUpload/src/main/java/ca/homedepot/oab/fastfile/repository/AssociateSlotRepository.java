package ca.homedepot.oab.fastfile.repository;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;

@Repository
public interface AssociateSlotRepository extends JpaRepository<AssociateSlotDTO, Integer> {

	@Query("SELECT slot FROM AssociateSlotDTO slot WHERE slot.typeId = ?1 AND slot.slotLdap =?2 AND slot.slotDate=?3")
	AssociateSlotDTO fetchSlot(String type, String ldap, Date shiftDate);

}
