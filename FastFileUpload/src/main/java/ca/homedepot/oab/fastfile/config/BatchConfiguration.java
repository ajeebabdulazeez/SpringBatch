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

import ca.homedepot.oab.fastfile.listener.BuildAssociateSlotStepListener;
import ca.homedepot.oab.fastfile.listener.FastFileImportJobListener;
import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.model.Schedule;
import ca.homedepot.oab.fastfile.processor.ScheduleItemProcessor;
import ca.homedepot.oab.fastfile.reader.AssociateSlotItemReader;
import ca.homedepot.oab.fastfile.reader.ScheduleItemReader;
import ca.homedepot.oab.fastfile.skip.ScheduleSkipper;
import ca.homedepot.oab.fastfile.tasklet.PostProcessingTasklet;
import ca.homedepot.oab.fastfile.tasklet.PreProcessingTasklet;
import ca.homedepot.oab.fastfile.writer.AssociateSlotItemWriter;
import ca.homedepot.oab.fastfile.writer.ScheduleDummyItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public FastFileImportJobListener fastFileImportJobListener;

	@Autowired
	public PreProcessingTasklet checkFileExist;

	@Autowired
	public ScheduleItemReader scheduleItemReader;

	@Autowired
	public ScheduleItemProcessor scheduleItemProcessor;

	@Autowired
	public BuildAssociateSlotStepListener scheduleListener;

	@Autowired
	public ScheduleDummyItemWriter dummyWriter;

	@Autowired
	public AssociateSlotItemReader slotDTOReader;

	@Autowired
	public AssociateSlotItemWriter slotDTOWriter;

	@Autowired
	public PostProcessingTasklet archiveFile;

	@Autowired
	public ScheduleSkipper scheduleSkipper;

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
		return jobBuilderFactory.get("fastFileImportJob").incrementer(new RunIdIncrementer())
				.listener(fastFileImportJobListener).start(fileCheckStep()).on("*").to(postFileExistCheck())
				.from(postFileExistCheck()).on("FAILED").end("NOOP").from(postFileExistCheck()).on("COMPLETED")
				.to(buildAssociateSlotRecords()).from(buildAssociateSlotRecords()).on("*").to(performDbOperation())
				.from(performDbOperation()).on("*").to(fileArchive()).end().build();
	}

	@Bean
	public Step fileCheckStep() {
		return stepBuilderFactory.get("fileCheckStep").tasklet(checkFileExist).build();
	}

	@Bean
	public Step fileArchive() {
		return stepBuilderFactory.get("fileArchive").tasklet(archiveFile).build();
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
		return stepBuilderFactory.get("buildAssociateSlotRecords").<Schedule, Schedule>chunk(10)
				.reader(scheduleItemReader).processor(scheduleItemProcessor).writer(dummyWriter).faultTolerant()
				.skipPolicy(scheduleSkipper).listener(scheduleListener).build();
	}

	/**
	 * @return
	 * 
	 *         Step to read entries from Slot Map and perform Database operations.
	 */
	@Bean
	public Step performDbOperation() {
		return stepBuilderFactory.get("performDbOperation").<AssociateSlotDTO, AssociateSlotDTO>chunk(1)
				.reader(slotDTOReader).writer(slotDTOWriter).build();
	}

}