package com.crewmeister.challenge;

import com.crewmeister.challenge.service.CSVProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
@Component
public class ChallengeApplication {

	@Autowired
	private CSVProcessingService csvProcessingService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ChallengeApplication.class, args);

		// Get your bean from Spring's context
		CSVProcessingService csvService = context.getBean(CSVProcessingService.class);
		csvService.processCSV();
	}
}
