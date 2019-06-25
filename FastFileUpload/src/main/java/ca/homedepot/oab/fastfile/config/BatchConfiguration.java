package ca.homedepot.oab.fastfile.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.oab.fastfile.listener.SlotListener;
import ca.homedepot.oab.fastfile.listener.SlotSkipListener;
import ca.homedepot.oab.fastfile.model.Slot;
import ca.homedepot.oab.fastfile.model.SlotDTO;
import ca.homedepot.oab.fastfile.processor.SlotProcessor;
import ca.homedepot.oab.fastfile.reader.SlotReader;
import ca.homedepot.oab.fastfile.tasklet.PreProcessingCheckTasklet;
import ca.homedepot.oab.fastfile.tasklet.SlotDbOperationTasklet;
import ca.homedepot.oab.fastfile.writer.SlotWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public PreProcessingCheckTasklet checkFileExist;

	@Autowired
	public SlotReader slotReader;

	@Autowired
	public SlotProcessor slotProcessor;

	@Autowired
	public SlotDbOperationTasklet performDbOperation;

	@Autowired
	public SlotListener slotListener;

	@Autowired
	public SlotSkipListener slotSkipListener;

	@Autowired
	public SlotWriter slotWriter;

	@Bean
	public JobExecutionDecider postFileExistCheck() {

		return (jobExecution, stepExecution) -> {

			if (stepExecution.getExitStatus().equals(ExitStatus.NOOP)) {
				jobExecution.setExitStatus(ExitStatus.NOOP);
				return FlowExecutionStatus.FAILED;
			}
			return FlowExecutionStatus.COMPLETED;
		};
	}

	@Bean
	public Job fastFileImportJob() throws Exception {
		return jobBuilderFactory.get("fastFileImportJob").incrementer(new RunIdIncrementer()).start(fileCheckStep())
				.on("*").to(postFileExistCheck()).from(postFileExistCheck()).on("FAILED").end("NOOP")
				.from(postFileExistCheck()).on("COMPLETED").to(buildRecordsForDbOperation())
				.from(buildRecordsForDbOperation()).on("*").to(performDbOperation()).end().build();
	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 *                   Step to read records from Fast file and create SlotDTO
	 *                   objects and fill in Slot Map. This will be further used to
	 *                   do the DB operations
	 * 
	 */
	@Bean
	public Step buildRecordsForDbOperation() throws Exception {
		return stepBuilderFactory.get("buildRecordsForDbOperation").<Slot, SlotDTO>chunk(1).reader(slotReader)
				.processor(slotProcessor).writer(slotWriter).listener(slotSkipListener).build();
	}

	/**
	 * @return
	 * 
	 *         Step to read entries from Slot Map and perform Database operations.
	 */
	@Bean
	public Step performDbOperation() {
		return stepBuilderFactory.get("performDbOperation").tasklet(performDbOperation).build();
	}

	@Bean
	public Step fileCheckStep() {
		return stepBuilderFactory.get("fileCheckStep").tasklet(checkFileExist).build();
	}

}