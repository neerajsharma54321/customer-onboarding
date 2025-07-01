package com.onboarding.customer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.onboarding.customer.dto.ApplicationEvent;
import com.onboarding.customer.dto.ApplicationQueuePayload;

@Component
public class KafkaProducerService {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
	
	@Autowired
    private KafkaTemplate<String, Object> kafkaApplicationQueueTemplate;

    @Value("${kafka.topic.registration}")
    private String topicName;
    
    @Value("${kafka.topic.applicant-submissions-event}")
    private String notificationTopicName;


    public void sendApplication(ApplicationQueuePayload payload) {
    	log.info("application received to the producer: "+ payload.getApplicationId());
        kafkaApplicationQueueTemplate.send(topicName, payload.getApplicationId(), payload);
    }
    
    public void sendNotification(ApplicationEvent payload) {
    	log.info("notification received to the producer: "+ payload.getApplicationId());
        kafkaApplicationQueueTemplate.send(notificationTopicName, payload.getApplicationId(), payload);
    }
}