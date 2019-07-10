package ca.homedepot.oab.fastfile.tasklet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;

/**
 * Tasklet to checks mandatory requirements before starting the execution. This
 * checks whether input file is available to start processing.
 */
@Component
public class PreProcessingTasklet implements Tasklet {

	@Value("${gcp.storage.bucket}")
	private String bucket;

	@Value("${gcp.storage.inputfolder}/${gcp.storage.fileName}")
	private String filePath;

	private static final Logger logger = LogManager.getLogger(PreProcessingTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//
//		Storage storage = StorageOptions.getDefaultInstance().getService();
//		Blob processedFile = storage.get(bucket, filePath);
//
//		if (processedFile.exists()) {
//			chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.COMPLETED);
//			logger.debug("Job Execution requirements meet");
//		} else {
//			chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.NOOP);
//			logger.debug("Job Execution requirements not meet - File missing");
//		}
		return RepeatStatus.FINISHED;
	}
}
