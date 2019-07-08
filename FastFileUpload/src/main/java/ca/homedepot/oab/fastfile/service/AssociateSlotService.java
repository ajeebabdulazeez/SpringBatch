package ca.homedepot.oab.fastfile.service;

import java.sql.Date;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;

public interface AssociateSlotService {

	public void createAssociateSlot(AssociateSlotDTO slotDTO);

	public void updateAssociateSLot(AssociateSlotDTO slotDTO, String newSchedule);

	public AssociateSlotDTO getAssociateSlot(String typeId, String ldap, Date shiftDate);

}
