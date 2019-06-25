package ca.homedepot.oab.fastfile.repository;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.homedepot.oab.fastfile.model.SlotDTO;

@Repository
public interface SlotRepository extends JpaRepository<SlotDTO, Integer> {

	@Query("SELECT slot FROM SlotDTO slot WHERE slot.typeId = ?1 AND slot.slotLdap =?2 AND slot.slotDate=?3")
	SlotDTO fetchSlot(String type, String ldap, Date shiftDate);

}
