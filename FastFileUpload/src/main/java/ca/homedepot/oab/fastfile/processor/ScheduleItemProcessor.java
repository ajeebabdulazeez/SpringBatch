package ca.homedepot.oab.fastfile.processor;

import java.text.ParseException;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.Schedule;
import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.util.ScheduleStringBuilder;
import ca.homedepot.oab.fastfile.util.AssociateSlotDTOMap;

/**
 * @author AXA8NZJ
 *
 */

@Component
public class ScheduleItemProcessor implements ItemProcessor<Schedule, Schedule> {

	@Autowired
	ScheduleStringBuilder scheduleStringBuilder;

	@Override
	public Schedule process(final Schedule slot) throws ParseException {

		String uniqueRecordIdentifier = slot.getActivityFlag() + slot.getLdap() + slot.getShiftDate();
		String scheduleString = scheduleStringBuilder.buildSchedule(slot.getStartInterval(), slot.getEndInterval(),
				slot.getCoverageCheck(), uniqueRecordIdentifier, slot);
		if (scheduleString != null) {
			AssociateSlotDTO slotDTO = scheduleStringBuilder.buildSlotDTOForMap(uniqueRecordIdentifier, slot, scheduleString);
			AssociateSlotDTOMap.getSlotSchedules().put(uniqueRecordIdentifier, slotDTO);
		}
		return null;
	}

}