package ca.homedepot.oab.fastfile.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class DummyItemWriter  implements ItemWriter<Object> {
	
	@Override
	public void write(List<? extends Object> item) throws Exception {
	}
}