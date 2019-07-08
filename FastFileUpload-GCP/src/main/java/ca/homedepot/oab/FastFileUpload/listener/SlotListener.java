package ca.homedepot.oab.FastFileUpload.listener;

import java.util.Calendar;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.FastFileUpload.model.SlotDTO;
import ca.homedepot.oab.FastFileUpload.util.SlotMap;

@Component
public class SlotListener implements StepExecutionListener {

	@Value("#{'gs://' + '${gcp.bucket.path}' + '/' + '${gcp.bucket.fileName}'}")
	private Resource csvResource;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("START TIME " + Calendar.getInstance().getTime());
		System.out.println(csvResource);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		Map<String, SlotDTO> scheduleMap = SlotMap.getSlotSchedules();

		scheduleMap.forEach((K, V) -> {
			System.out.println("Key " + K + " string " + V.getSlotAvailability());
		});
		System.out.println("END TIME " + Calendar.getInstance().getTime());

		return ExitStatus.COMPLETED;
	}
}