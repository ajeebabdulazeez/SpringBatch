package ca.homedepot.oab.FastFileUpload.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.oab.FastFileUpload.listener.SlotListener;
import ca.homedepot.oab.FastFileUpload.model.Slot;
import ca.homedepot.oab.FastFileUpload.model.SlotDTO;
import ca.homedepot.oab.FastFileUpload.processor.SlotProcessor;
import ca.homedepot.oab.FastFileUpload.reader.SlotReader;
import ca.homedepot.oab.FastFileUpload.tasklet.SlotTasklet;
import ca.homedepot.oab.FastFileUpload.writer.SlotWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public SlotReader reader() {
		return new SlotReader();
	}

	@Bean
	public SlotProcessor processor() {
		return new SlotProcessor();
	}

	@Bean
	public Tasklet slotTasklet() {
		return new SlotTasklet();
	}

	@Bean
	public SlotListener listener() {
		return new SlotListener();
	}

	@Bean
	public ItemWriter<SlotDTO> writer() throws Exception {
		return new SlotWriter();
	}

	@Bean
	public Job importUserJob() throws Exception {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).start(step1())
				.build();
	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 *                   Step to read records from Fast file and create SlotDTO
	 *                   objects and fill in Slot Map
	 * 
	 */
	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("step1").<Slot, SlotDTO>chunk(10).reader(reader()).processor(processor())
				.writer(writer()).listener(listener()).build();
	}

	/**
	 * @return
	 * 
	 *         Step to read entries from Slot Map and perform Database operations.
	 */
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").tasklet(slotTasklet()).build();
	}

}