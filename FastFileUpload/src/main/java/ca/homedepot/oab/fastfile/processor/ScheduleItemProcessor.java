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
	public Schedule process(final Schedule schedule) throws ParseException {

		String uniqueRecordIdentifier = schedule.getActivityFlag() + schedule.getLdap() + schedule.getShiftDate();
		String scheduleString = scheduleStringBuilder.buildSchedule(schedule.getStartInterval(), schedule.getEndInterval(),
				schedule.getCoverageCheck(), uniqueRecordIdentifier, schedule);
		if (scheduleString != null) {
			AssociateSlotDTO associateSlotDTO = scheduleStringBuilder.buildSlotDTOForMap(uniqueRecordIdentifier, schedule, scheduleString);
			AssociateSlotDTOMap.getSlotSchedules().put(uniqueRecordIdentifier, associateSlotDTO);
		}
		return null;
	}

}