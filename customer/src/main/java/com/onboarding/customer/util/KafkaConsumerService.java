package com.onboarding.customer.util;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.onboarding.customer.dto.ApplicationEvent;
import com.onboarding.customer.dto.ApplicationQueuePayload;
import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.entity.Application;
import com.onboarding.customer.repo.RegistrationRepository;

@Component
public class KafkaConsumerService {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@KafkaListener(topics = "${kafka.topic.registration}", groupId = "onboarding-group")
	public void consumeApplication(ApplicationQueuePayload payload) {
		log.info("application received to the consumer: " + payload.getApplicationId());
		// perform business logic to verify the data.
		// for the time being update the date.
		try {
			Optional<Application> optionalApplication = registrationRepository.findById(payload.getApplicationId());
			if (optionalApplication.isPresent()) {
				Application application = optionalApplication.get();
				application.setAppStatus(AppStatus.PENDING);
				application.setUpdatedAt(Date.from(Instant.now()));
				registrationRepository.save(application);
				log.info("updated successfully");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@KafkaListener(topics = "${kafka.topic.applicant-submissions-event}", groupId = "processor-group")
	public void handleKafkaMessage(ApplicationEvent event) {
		log.info("application notification received to the consumer: " + event.getApplicationId());
		messagingTemplate.convertAndSend("/topic/applicants", event);
		log.info("application notification sent");
	}
}
