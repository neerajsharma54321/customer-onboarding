package com.onboarding.customer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.onboarding.customer.dto.ApplicationEvent;

@Service
public class ProcessorNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "applicant-submissions", groupId = "processor-group")
    public void handleKafkaMessage(ApplicationEvent event) {
        // Send message to frontend via WebSocket
        messagingTemplate.convertAndSend("/topic/application-events", event);
    }
}
