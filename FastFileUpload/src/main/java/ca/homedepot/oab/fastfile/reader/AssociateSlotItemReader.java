package ca.homedepot.oab.fastfile.reader;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;

@Component
public class AssociateSlotItemReader implements ItemReader<AssociateSlotDTO> {

	private int index;
	private List<AssociateSlotDTO> slotDTOList;

	public void setSlotDTO(List<AssociateSlotDTO> slotDTOList) {
		this.slotDTOList = slotDTOList;
	}

	public AssociateSlotItemReader() {
	}

	@Override
	public AssociateSlotDTO read() throws Exception {
		AssociateSlotDTO nextSlotDTO = null;
		if (index < slotDTOList.size()) {
			nextSlotDTO = slotDTOList.get(index);
			index++;
		}
		return nextSlotDTO;
	}
}