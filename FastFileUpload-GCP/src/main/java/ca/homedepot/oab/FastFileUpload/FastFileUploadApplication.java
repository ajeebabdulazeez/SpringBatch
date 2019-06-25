package ca.homedepot.oab.FastFileUpload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })

public class FastFileUploadApplication {
	public static void main(String[] args) {
		SpringApplication.run(FastFileUploadApplication.class, args);
	}

}
