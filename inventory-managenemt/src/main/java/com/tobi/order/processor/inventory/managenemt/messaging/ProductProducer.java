package com.tobi.order.processor.inventory.managenemt.messaging;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final KafkaTemplate<String, Object> kafkaTemplateObject;

    public void createProduct(Product product) {
        logger.info(String.format("Product creation sent to kafka -> %s", product));
        this.kafkaTemplateObject.send(MessagingConstant.PRODUCT_CREATION_TOPIC_NAME, product.getName(), product);
    }

    public void updateProduct(Product product) {
        logger.info(String.format("Product update sent to kafka -> %s", product));
        this.kafkaTemplateObject.send(MessagingConstant.PRODUCT_UPDATE_TOPIC_NAME, product.getName(), product);
    }
}
