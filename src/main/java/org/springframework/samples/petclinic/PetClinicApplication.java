/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.samples.petclinic.order.Order;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitChangedEvent;
import org.springframework.samples.petclinic.visit.VisitMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@EnableJdbcAuditing
@EnableAsync
@EnableTransactionManagement
public class PetClinicApplication {

    private static final Logger log = LoggerFactory.getLogger(PetClinicApplication.class);

    private final VisitMapper visitMapper;

    private final KafkaTemplate<String, VisitChangedEvent> kafkaTemplate;

    public PetClinicApplication(VisitMapper visitMapper,
                                KafkaTemplate<String, VisitChangedEvent> kafkaTemplate) {
        this.visitMapper = visitMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}


    @Async
    @EventListener
    public void sendNotificationWhenVisitChanged(AfterSaveEvent<Visit> event) {
        kafkaTemplate.send("visit",
            visitMapper.toVisitChangedEvent(event.getEntity()));
    }
}
