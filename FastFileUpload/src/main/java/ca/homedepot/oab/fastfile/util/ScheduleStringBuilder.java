package ca.homedepot.oab.fastfile.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.Slot;
import ca.homedepot.oab.fastfile.model.SlotDTO;

@Component
public class ScheduleStringBuilder {

	private final static int START_HOUR = 0;
	private final static String DEFAULT_SLOT = "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU";
	private final static String AVAILABILITY_IDENTIFIER = "A";

	/**
	 * @param shiftStart
	 * @param shiftEnd
	 * @param coverageCheck
	 * @param uniqueRecordIdentifier
	 * @param slot
	 * @return Schedule String
	 * @throws ParseException
	 * 
	 *                        Method to build the Slot schedule string for an
	 *                        associate on a specific shift date.
	 *
	 */
	public String buildSchedule(String shiftStart, String shiftEnd, String coverageCheck, String uniqueRecordIdentifier,
			Slot slot) throws ParseException {

		Calendar shiftStartTime = Calendar.getInstance();
		Calendar shiftEndTime = Calendar.getInstance();
		String modifiedSchedule = null;

		if (coverageCheck.equalsIgnoreCase("On Coverage")) {

			shiftStartTime.setTime(dateFormatter("yyyy-MM-dd HH:mm", shiftStart));
			shiftEndTime.setTime(dateFormatter("yyyy-MM-dd HH:mm", shiftEnd));

			roundShiftTime(shiftStartTime);
			roundShiftTime(shiftEndTime);

			float shiftDuration = getShiftDurationInHours(shiftStartTime, shiftEndTime);
			modifiedSchedule = updateScheduleInMap(shiftDuration, shiftStartTime, uniqueRecordIdentifier);

		}
		return modifiedSchedule;

	}

	/**
	 * @param key
	 * @param slot
	 * @param scheduleString
	 * @return SlotDTO
	 * @throws ParseException
	 * 
	 *                        Method to build the Slot DTO objects. This object is
	 *                        added to Schedule Map.
	 */
	public SlotDTO buildSlotDTOForMap(String uniqueRecordIdentifier, Slot slot, String scheduleString)
			throws ParseException {
		SlotDTO slotDTO;
		if (SlotMap.getSlotSchedules().get(uniqueRecordIdentifier) == null) {
			slotDTO = new SlotDTO();
			Date parsedDate = dateFormatter("yyyy-MM-dd", slot.getShiftDate());
			slotDTO.setSlotDate(new java.sql.Date(parsedDate.getTime()));
			slotDTO.setSlotStoreNo(Integer.parseInt(slot.getStoreNumber()));
			slotDTO.setSlotLdap(slot.getLdap());
			slotDTO.setSlotAvailability(scheduleString);
			slotDTO.setTypeId(slot.getActivityFlag());
			slotDTO.setSlotCreatedts(new Timestamp(System.currentTimeMillis()));
			return slotDTO;
		}

		else {
			slotDTO = SlotMap.getSlotSchedules().get(uniqueRecordIdentifier);
			slotDTO.setSlotAvailability(scheduleString);
		}

		return slotDTO;

	}

	/**
	 * @param dateFormat
	 * @param dateToBeFormatted
	 * @return formatted date
	 * @throws ParseException
	 * 
	 *                        Method to parse a string date and format it.
	 */
	private Date dateFormatter(String dateFormat, String dateToBeFormatted) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.parse(dateToBeFormatted);
	}

	/**
	 * @param shiftTime
	 * 
	 *                  Round the shift time to next half hour or hour.
	 */
	private void roundShiftTime(Calendar shiftTime) {
		int divideMinutesByThirty = shiftTime.get(Calendar.MINUTE) % 30;
		shiftTime.add(Calendar.MINUTE, (divideMinutesByThirty) != 0 ? (30 - (divideMinutesByThirty)) : 0);
	}

	/**
	 * @param shiftStartTime
	 * @param shiftEndTime
	 * @return the shift duration in hours.
	 * 
	 *         Calculate the shift duration in hours
	 */
	private float getShiftDurationInHours(Calendar shiftStartTime, Calendar shiftEndTime) {
		long diff = shiftEndTime.getTime().getTime() - shiftStartTime.getTime().getTime();
		return (float) TimeUnit.MILLISECONDS.toMinutes(diff) / 60;
	}

	/**
	 * @param uniqueRecordIdentifier
	 * @return existing schedule string from Map
	 * 
	 *         Method to return the Schedule String from Map against a particular
	 *         associate slot. If not found, default schedule string is returned.
	 */
	private String getScheduleFromMap(String uniqueRecordIdentifier) {
		return (SlotMap.getSlotSchedules().containsKey(uniqueRecordIdentifier))
				? SlotMap.getSlotSchedules().get(uniqueRecordIdentifier).getSlotAvailability()
				: DEFAULT_SLOT;
	}

	/**
	 * @param shiftDuration
	 * @param shiftStartTime
	 * @param uniqueRecordIdentifier
	 * @return updated Schedule String
	 * 
	 *         Method to update the schedule String in the Map against a particular
	 *         associate slot based on the incoming shift details from fast file.
	 */
	private String updateScheduleInMap(float shiftDuration, Calendar shiftStartTime, String uniqueRecordIdentifier) {

		String appendingString = String.join("",
				Collections.nCopies((int) (shiftDuration * 2), AVAILABILITY_IDENTIFIER));

		int startstringpos = ((shiftStartTime.get(Calendar.HOUR_OF_DAY) - START_HOUR) * 2)
				+ (shiftStartTime.get(Calendar.MINUTE) != 0 ? new Integer(1) : new Integer(0));
		int endstringpos = (int) ((shiftDuration * 2) + startstringpos);

		return getScheduleFromMap(uniqueRecordIdentifier).substring(0, startstringpos).concat(appendingString)
				.concat(getScheduleFromMap(uniqueRecordIdentifier).substring(endstringpos));
	}
}
