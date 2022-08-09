package com.pnshvets.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import com.pnshvets.interview.config.ApplicationProperties;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
@Log4j2
public class InterviewApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(InterviewApplication.class);

		Environment environment = app.run(args).getEnvironment();
		logStartup(environment);
	}

	private static void logStartup(Environment env) {
		log.info(
				"\n\n\t\tWelcome! Application by {}"
						+ "\n\t\t is running http://localhost:{}/",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port"));
	}

}
