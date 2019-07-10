package ca.homedepot.oab.fastfile.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.repository.AssociateSlotRepository;
import ca.homedepot.oab.fastfile.service.AssociateSlotService;

@Component
public class AssociateSlotServiceImpl implements AssociateSlotService {

	@Autowired
	AssociateSlotRepository repository;

	@Override
	public void createAssociateSlot(AssociateSlotDTO slotDTO) {
		repository.save(slotDTO);
	}

	@Override
	public void updateAssociateSlot(AssociateSlotDTO slotFromDb, String newSchedule) {
		slotFromDb
				.setSlotAvailability(modifyScheduleInMapWithAppointment(slotFromDb.getSlotAvailability(), newSchedule));
		slotFromDb.setSlotUpdatedts(new Timestamp(System.currentTimeMillis()));
		repository.save(slotFromDb);
	}

	@Override
	public AssociateSlotDTO getAssociateSlot(String typeId, String ldap, Date shiftDate) {
		return (AssociateSlotDTO) repository.fetchSlot(typeId, ldap, shiftDate);
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

}
