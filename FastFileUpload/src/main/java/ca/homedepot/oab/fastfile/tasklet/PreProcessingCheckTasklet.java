package ca.homedepot.oab.fastfile.tasklet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


/**
 * Tasklet to checks mandatory requirements before starting the execution.
 * This checks whether input file is available to start processing.
 */
@Component
public class PreProcessingCheckTasklet implements Tasklet
{


//	@Value("#{'gs://' + '${gcp.bucket.path}' + '/' + '${gcp.bucket.fileName}'}")
	private Resource csvImportFile;

	private static final Logger logger = LogManager.getLogger(PreProcessingCheckTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
	{
		//change this
		csvImportFile = new ClassPathResource("FASTFILE1.csv");
		if (csvImportFile.exists())
		{
			chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.COMPLETED);
			logger.debug("Job Execution requirements meet");
		}else
		{
			chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.NOOP);
			logger.debug("Job Execution requirements not meet - File missing");
		}
		return RepeatStatus.FINISHED;
	}
}
