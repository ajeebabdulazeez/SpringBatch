package ca.homedepot.oab.fastfile.tasklet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

@Component
public class PostProcessingTasklet implements Tasklet {

	@Value("${gcp.storage.bucket}")
	private String bucket;

	@Value("${gcp.storage.inputfolder}/${gcp.storage.fileName}")
	private String filePath;

	@Value("${gcp.storage.fileName}")
	private String fileName;

	@Value("${gcp.storage.archivefolder}")
	private String archivePath;

	private static final Logger logger = LogManager.getLogger(PostProcessingTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//		try {
//
//			Storage storage = StorageOptions.getDefaultInstance().getService();
//			Blob processedFile = storage.get(bucket, filePath);
//			processedFile.copyTo(bucket, archivePath + "/" + getFileNameWithTimeStamp(fileName));
//
//			if (processedFile.delete())
//				chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.COMPLETED);
//			else
//				chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.FAILED);
//
//		} catch (Exception e) {
//			logger.error("Error encountered moving file", e);
//			chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.FAILED);
//			return RepeatStatus.FINISHED;
//		}
		return RepeatStatus.FINISHED;
	}

	private String getFileNameWithTimeStamp(String csvFile) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String timePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(timestamp);
		return String.format("%s-%s", timePrefix, csvFile);
	}

}
