package ca.homedepot.oab.fastfile.reader;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;

@Component
public class AssociateSlotItemReader implements ItemReader<AssociateSlotDTO> {

	private int index;
	private List<AssociateSlotDTO> associateSlotDTOList;

	public void setSlotDTO(List<AssociateSlotDTO> associateSlotDTOList) {
		this.associateSlotDTOList = associateSlotDTOList;
	}

	public AssociateSlotItemReader() {
	}

	@Override
	public AssociateSlotDTO read() throws Exception {
		AssociateSlotDTO nextAssociateSlotDTO = null;
		if (index < associateSlotDTOList.size()) {
			nextAssociateSlotDTO = associateSlotDTOList.get(index);
			index++;
		}
		return nextAssociateSlotDTO;
	}
}