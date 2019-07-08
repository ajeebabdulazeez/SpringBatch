package ca.homedepot.oab.fastfile.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import ca.homedepot.oab.fastfile.model.Schedule;

@Component
public class ScheduleItemReader extends FlatFileItemReader<Schedule> {

//	@Value("${gcp.bucket.path}/${gcp.bucket.fileName}")
	private Resource csvResource;

	public ScheduleItemReader() {
		super();

		this.setName("fastFileItemReader");
		this.setResource(csvResource);
		this.setResource(new ClassPathResource("FASTFILE1.csv"));
		this.setLinesToSkip(1);

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

}
