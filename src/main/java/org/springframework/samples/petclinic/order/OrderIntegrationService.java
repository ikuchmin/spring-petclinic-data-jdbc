package org.springframework.samples.petclinic.order;

import org.springframework.context.event.EventListener;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderIntegrationService {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    private final OrderMapper orderMapper;

    public OrderIntegrationService(KafkaTemplate<String, OrderDto> kafkaTemplate,
                                   OrderMapper orderMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderMapper = orderMapper;
    }


    @Async
    @EventListener
    public void handleAfterSaveEvent(AfterSaveEvent<Order> event) {
        kafkaTemplate.send("orders", orderMapper.toOrderGetOneDto(event.getEntity()));
    }
}
