package ca.homedepot.oab.fastfile.listener;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.AssociateSlotDTO;
import ca.homedepot.oab.fastfile.skip.ScheduleSkipper;

@Component
public class FastFileImportJobListener implements JobExecutionListener {

	@Autowired
	ScheduleSkipper scheduleSkipper;

	private static final Logger logger = LogManager.getLogger(FastFileImportJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("Fast File Import Job Started");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		{
			if (scheduleSkipper.getSkippedRecordCount() > 0) {
				logger.error("skipped record counts " + scheduleSkipper.getSkippedRecordCount(), "");
				scheduleSkipper.getSkipRecordMap().forEach((recordIdentifier, exception) -> {
					logger.error("Skipped Record Identifier " + recordIdentifier + "  -> Reason to Skip: " + exception);
				});

			} else {
				logger.info("No records Skipped");
			}
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				logger.info("Fast File Import Job Finished!");
			}

		}
	}

}
