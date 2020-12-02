package com.tobi.order.processor.inventory.managenemt.messaging;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final KafkaTemplate<String, Object> kafkaTemplateObject;

    public void createOrder(Order order) {
        logger.info(String.format("Order creation sent to kafka -> %s", order));
        this.kafkaTemplateObject.send(MessagingConstant.ORDER_CREATION_TOPIC_NAME, order.getId(), order);
    }
}
