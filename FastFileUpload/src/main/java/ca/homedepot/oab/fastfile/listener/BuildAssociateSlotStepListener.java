package ca.homedepot.oab.fastfile.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.reader.AssociateSlotItemReader;
import ca.homedepot.oab.fastfile.util.AssociateSlotDTOMap;

@Component
public class BuildAssociateSlotStepListener implements StepExecutionListener {

	@Autowired
	AssociateSlotItemReader slotDTOReader;

	private List<AssociateSlotDTO> slotDTOList = new ArrayList<AssociateSlotDTO>();

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		Map<String, AssociateSlotDTO> scheduleMap = AssociateSlotDTOMap.getSlotSchedules();

		scheduleMap.forEach((Key, slotDTO) -> {
			slotDTOList.add(slotDTO);
		});
		slotDTOReader.setSlotDTO(slotDTOList);

		return ExitStatus.COMPLETED;
	}
}