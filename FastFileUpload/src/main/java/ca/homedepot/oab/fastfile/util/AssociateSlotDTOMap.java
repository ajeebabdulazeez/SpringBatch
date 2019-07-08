package ca.homedepot.oab.fastfile.util;

import java.util.HashMap;
import java.util.Map;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;

public class AssociateSlotDTOMap {

	private static Map<String, AssociateSlotDTO> slots = new HashMap<String, AssociateSlotDTO>();

	/**
	 * @return the slotSchedules
	 */
	public static Map<String, AssociateSlotDTO> getSlotSchedules() {
		return slots;
	}

	/**
	 * @param slotSchedules the slotSchedules to set
	 */
	public static void setSlotSchedules(Map<String, AssociateSlotDTO> slots) {
		AssociateSlotDTOMap.slots = slots;
	}

}
