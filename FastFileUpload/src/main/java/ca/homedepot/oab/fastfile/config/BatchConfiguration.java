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

import ca.homedepot.oab.fastfile.listener.ScheduleListener;
import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.model.Schedule;
import ca.homedepot.oab.fastfile.processor.ScheduleItemProcessor;
import ca.homedepot.oab.fastfile.reader.AssociateSlotItemReader;
import ca.homedepot.oab.fastfile.reader.ScheduleItemReader;
import ca.homedepot.oab.fastfile.tasklet.PreProcessingCheckTasklet;
import ca.homedepot.oab.fastfile.writer.AssociateSlotItemWriter;
import ca.homedepot.oab.fastfile.writer.DummyItemWriter;

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
	public ScheduleItemReader scheduleItemReader;

	@Autowired
	public ScheduleItemProcessor scheduleItemProcessor;

	@Autowired
	public ScheduleListener scheduleListener;

	@Autowired
	public DummyItemWriter dummyWriter;

	@Autowired
	public AssociateSlotItemReader slotDTOReader;

	@Autowired
	public AssociateSlotItemWriter slotDTOWriter;

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
				.from(postFileExistCheck()).on("COMPLETED").to(buildAssociateSlotRecords())
				.from(buildAssociateSlotRecords()).on("*").to(performDbOperation()).end().build();
	}

	@Bean
	public Step fileCheckStep() {
		return stepBuilderFactory.get("fileCheckStep").tasklet(checkFileExist).build();
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
	public Step buildAssociateSlotRecords() throws Exception {
		return stepBuilderFactory.get("buildRecordsForDbOperation").<Schedule, Schedule>chunk(1)
				.reader(scheduleItemReader).processor(scheduleItemProcessor).writer(dummyWriter)
				.listener(scheduleListener).build();
	}

	/**
	 * @return
	 * 
	 *         Step to read entries from Slot Map and perform Database operations.
	 */
	@Bean
	public Step performDbOperation() {
		return stepBuilderFactory.get("test").<AssociateSlotDTO, AssociateSlotDTO>chunk(1).reader(slotDTOReader)
				.writer(slotDTOWriter).build();
	}

}