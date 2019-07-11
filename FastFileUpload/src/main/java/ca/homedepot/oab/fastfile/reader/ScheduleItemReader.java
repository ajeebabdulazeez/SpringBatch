package ca.homedepot.oab.fastfile.reader;

import javax.annotation.PostConstruct;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.stereotype.Component;

//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;

import ca.homedepot.oab.fastfile.model.Schedule;

@Component
public class ScheduleItemReader extends FlatFileItemReader<Schedule> {

	@Value("#{'gs://' +'${gcp.storage.bucket}' + '/' + '${gcp.storage.inputfolder}' + '/' + '${gcp.storage.fileName}'}")
	private String fileLocation;

//	private Storage storage;

	public ScheduleItemReader() {
		super();
//		storage = StorageOptions.getDefaultInstance().getService();
		this.setName("fastFileItemReader");
		this.setLinesToSkip(1);

		//REMOVE THIS LINE
		this.setResource(new ClassPathResource("FASTFileException.csv"));
		
		this.setLineMapper(new DefaultLineMapper<Schedule>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "SHIFT_DATE", "STORE_NUMBER", "STORE_NAME", "DEPT_NO", "DEPT_NAME",
								"LDAP", "LAST_NAME", "FIRST_NAME", "SHIFT_START", "SHIFT_END", "START_INTERVAL",
								"END_INTERVAL", "COVERAGE_CHECK", "COVERAGE_TYPE", "COVERAGE_JOB", "TASK_DETAIL",
								"ACTIVITY_FLAG" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Schedule>() {
					{
						setTargetType(Schedule.class);
					}
				});
			}
		});
		this.setStrict(false);
	}

	@PostConstruct
	private void setResource() {
//		this.setResource(new GoogleStorageResource(storage, fileLocation));
	}

}
