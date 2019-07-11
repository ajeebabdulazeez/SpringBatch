package ca.homedepot.oab.fastfile.skip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.Schedule;
import ca.homedepot.oab.fastfile.processor.ScheduleItemProcessor;

@Component
public class ScheduleSkipper implements SkipPolicy {

	@Autowired
	private ScheduleItemProcessor scheduleItemProcessor;

	private Map<String, String> skipRecordMap = new HashMap<String, String>();
	private int skippedRecordCount;

	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {

		Schedule skippedRecord = scheduleItemProcessor.getScheduleRecordInFastFile();
		skipRecordMap.put("LDAP:" + skippedRecord.getLdap() + " Shift Date:" + skippedRecord.getShiftDate()
				+ " Department:" + skippedRecord.getActivityFlag(), exception.getMessage());
		this.skippedRecordCount = ++skipCount;
		return true;
	}

	public int getSkippedRecordCount() {
		return skippedRecordCount;
	}

	public Map<String, String> getSkipRecordMap() {
		return skipRecordMap;
	}
}
