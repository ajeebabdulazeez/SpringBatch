package ca.homedepot.oab.fastfile.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.SlotDTO;

@Component
public class SlotWriter implements ItemWriter<SlotDTO> {

	@Override
	public void write(List items) throws Exception {
	}

}