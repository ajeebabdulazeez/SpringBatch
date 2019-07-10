package ca.homedepot.oab.fastfile.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.service.AssociateSlotService;

@Component
public class AssociateSlotItemWriter implements ItemWriter<AssociateSlotDTO> {

	@Autowired
	AssociateSlotService associateSlotService;

	private AssociateSlotDTO slotDTO;

	@Override
	public void write(List<? extends AssociateSlotDTO> slotDTO) throws Exception {
		this.slotDTO = slotDTO.iterator().next();

		String scheduleFromMap = this.slotDTO.getSlotAvailability();
		AssociateSlotDTO slotFromDb = associateSlotService.getAssociateSlot(this.slotDTO.getTypeId(), this.slotDTO.getSlotLdap(),
				this.slotDTO.getSlotDate());

		if (slotFromDb == null) {
			associateSlotService.createAssociateSlot(this.slotDTO);

		} else if (!slotFromDb.getSlotAvailability().equals(scheduleFromMap)
				&& !(slotFromDb.getSlotAvailability().replace("B", "A").equals(scheduleFromMap)
						|| slotFromDb.getSlotAvailability().replace("B", "U").equals(scheduleFromMap))) {

			associateSlotService.updateAssociateSlot(slotFromDb, scheduleFromMap);
		}

	}

}
