package ca.homedepot.oab.FastFileUpload.util;

import java.util.HashMap;
import java.util.Map;

import ca.homedepot.oab.FastFileUpload.model.SlotDTO;

public class SlotMap {

	private static Map<String, SlotDTO> slots = new HashMap<String, SlotDTO>();

	/**
	 * @return the slotSchedules
	 */
	public static Map<String, SlotDTO> getSlotSchedules() {
		return slots;
	}

	/**
	 * @param slotSchedules the slotSchedules to set
	 */
	public static void setSlotSchedules(Map<String, SlotDTO> slots) {
		SlotMap.slots = slots;
	}

}
