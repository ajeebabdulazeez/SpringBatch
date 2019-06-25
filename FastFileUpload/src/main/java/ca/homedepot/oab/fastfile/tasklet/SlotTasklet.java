package ca.homedepot.oab.fastfile.tasklet;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import ca.homedepot.oab.fastfile.model.SlotDTO;
import ca.homedepot.oab.fastfile.repository.SlotRepository;
import ca.homedepot.oab.fastfile.util.SlotMap;

public class SlotTasklet implements Tasklet {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

	@Autowired
	SlotRepository slotRepository;

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Map<String, SlotDTO> scheduleMap = SlotMap.getSlotSchedules();

		scheduleMap.forEach((K, V) -> {
			String scheduleFromMap = V.getSlotAvailability();
			SlotDTO slotFromDb = getScheduleFromDB(V.getTypeId(), V.getSlotLdap(), V.getSlotDate());

			if (slotFromDb == null) {
				createSlot(V);

			} else if (!slotFromDb.getSlotAvailability().equals(scheduleFromMap)
					&& !(slotFromDb.getSlotAvailability().replace("B", "A").equals(scheduleFromMap)
							|| slotFromDb.getSlotAvailability().replace("B", "U").equals(scheduleFromMap))) {

				updateSlot(slotFromDb, scheduleFromMap);
			}

		});
		return RepeatStatus.FINISHED;
	}

	/**
	 * @param scheduleFromDb
	 * @param scheduleFromMap
	 * @return
	 * 
	 *         Method to modify the schedule String in Map by replacing string with
	 *         character B as in Database record before updating in table.
	 * 
	 *         This it to update the slot details in fast file with already booked
	 *         appointments.
	 */
	private String modifyScheduleInMapWithAppointment(String scheduleFromDb, String scheduleFromMap) {
		char[] charArrayScheduleFromDb = scheduleFromDb.toCharArray();
		StringBuilder updatedScheduleInMap = new StringBuilder(scheduleFromMap);
		List<Integer> listAppointmentSlotPositions = getAppointmentSlotPositions(charArrayScheduleFromDb);

		if (!listAppointmentSlotPositions.isEmpty()) {
			for (Integer position : listAppointmentSlotPositions) {
				updatedScheduleInMap.setCharAt(position, 'B');
			}
		}

		return updatedScheduleInMap.toString();
	}

	/**
	 * @param charArrayScheduleFromDb
	 * @return List of positions of appointment character in Schedule string from Db
	 */
	private List<Integer> getAppointmentSlotPositions(char[] charArrayScheduleFromDb) {
		int position = 0;
		List<Integer> appointmentSlotPositionList = new ArrayList<Integer>();
		for (char scheduleChar : charArrayScheduleFromDb) {

			if (Character.toString(scheduleChar).equals("B")) {
				appointmentSlotPositionList.add(position);
			}
			position++;
		}

		return appointmentSlotPositionList;
	}

	/**
	 * @param slotDTO
	 * 
	 *                Method to insert the record in Slot table.
	 */
	private void createSlot(SlotDTO slotDTO) {
		slotRepository.save(slotDTO);
	}

	/**
	 * @param slotDtoFromDb
	 * @param scheduleFromMap
	 * 
	 *                        Method to update the record in Slot table.
	 */
	private void updateSlot(SlotDTO slotFromDb, String scheduleFromMap) {
		slotFromDb.setSlotAvailability(
				modifyScheduleInMapWithAppointment(slotFromDb.getSlotAvailability(), scheduleFromMap));
		slotFromDb.setSlotUpdatedts(new Timestamp(System.currentTimeMillis()));
		slotRepository.save(slotFromDb);

	}

	/**
	 * @return
	 * 
	 *         Method to fetch the Schedule from DB of an associate for a particular
	 *         date and appointment type.
	 */
	private SlotDTO getScheduleFromDB(String typeId, String ldap, Date shiftDate) {
		return (SlotDTO) slotRepository.fetchSlot(typeId, ldap, shiftDate);
	}

}
