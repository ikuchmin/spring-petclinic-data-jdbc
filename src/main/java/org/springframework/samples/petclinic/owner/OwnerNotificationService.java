package org.springframework.samples.petclinic.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.samples.petclinic.visit.VisitChangedEvent;
import org.springframework.stereotype.Service;

@Service
public class OwnerNotificationService {
    private static final Logger log = LoggerFactory.getLogger(OwnerNotificationService.class);

    @KafkaListener(topics = "visit", containerFactory = "visitChangedEventListenerFactory")
    public void consumeVisitChangedEvent(VisitChangedEvent visitChangedEvent) {
        log.info("Notify owner about visit is changed {}", visitChangedEvent.owner());
    }
}
