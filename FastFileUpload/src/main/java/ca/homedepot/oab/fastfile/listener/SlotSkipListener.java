package ca.homedepot.oab.fastfile.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.Schedule;

@Component
public class SlotSkipListener implements SkipListener<Schedule, Schedule> {

	private static final Logger logger = LogManager.getLogger(SlotSkipListener.class);

	@Override
	public void onSkipInRead(Throwable t) {
		System.out.println(t);
		logger.debug("Error in Read due to error" + t.getMessage());
	}

	@Override
	public void onSkipInWrite(Schedule item, Throwable t) {
		logger.debug("Error in writing item ", item);
	}

	@Override
	public void onSkipInProcess(Schedule item, Throwable t) {
		System.out.println("IN PROCESS SKIP");
		logger.debug("Error in processing item " + item.getShiftDate() + " " + item.getLdap() + " "
				+ item.getActivityFlag() + " due to " + t.getMessage(), item);
	}

}
