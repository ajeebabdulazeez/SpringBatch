package ca.homedepot.oab.FastFileUpload.reader;


import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import ca.homedepot.oab.FastFileUpload.model.Slot;

public class SlotReader extends FlatFileItemReader<Slot> {
	
//	@Value("${gcp.bucket.path}/${gcp.bucket.fileName}")
	private Resource csvResource;

	public SlotReader() {
		super();

		this.setName("fastFileItemReader");
		this.setResource(new ClassPathResource("File11.csv"));
		this.setLinesToSkip(1);

		this.setLineMapper(new DefaultLineMapper<Slot>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "SHIFT_DATE", "STORE_NUMBER", "STORE_NAME", "DEPT_NO", "DEPT_NAME",
								"LDAP", "LAST_NAME", "FIRST_NAME", "SHIFT_START", "SHIFT_END", "START_INTERVAL",
								"END_INTERVAL", "COVERAGE_CHECK", "COVERAGE_TYPE", "COVERAGE_JOB", "TASK_DETAIL",
								"ACTIVITY_FLAG" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Slot>() {
					{
						setTargetType(Slot.class);
					}
				});
			}
		});

	}

}
