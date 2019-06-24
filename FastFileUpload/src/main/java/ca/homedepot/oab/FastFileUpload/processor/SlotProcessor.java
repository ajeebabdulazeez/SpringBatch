package ca.homedepot.oab.FastFileUpload.processor;

import java.text.ParseException;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import ca.homedepot.oab.FastFileUpload.model.Slot;
import ca.homedepot.oab.FastFileUpload.model.SlotDTO;
import ca.homedepot.oab.FastFileUpload.util.ScheduleStringBuilder;
import ca.homedepot.oab.FastFileUpload.util.SlotMap;

/**
 * @author AXA8NZJ
 *
 */
public class SlotProcessor implements ItemProcessor<Slot, SlotDTO> {

	@Autowired
	ScheduleStringBuilder scheduleStringBuilder;

	@Override
	public SlotDTO process(final Slot slot) throws ParseException {

		String uniqueRecordIdentifier = slot.getActivityFlag() + slot.getLdap() + slot.getShiftDate();
		System.out.println(uniqueRecordIdentifier);
		String scheduleString = scheduleStringBuilder.buildSchedule(slot.getStartInterval(), slot.getEndInterval(),
				slot.getCoverageCheck(), uniqueRecordIdentifier, slot);
		if (scheduleString != null) {
			SlotDTO slotDTO = scheduleStringBuilder.buildSlotDTOForMap(uniqueRecordIdentifier, slot, scheduleString);
			SlotMap.getSlotSchedules().put(uniqueRecordIdentifier, slotDTO);
		}
		return null;
	}

}