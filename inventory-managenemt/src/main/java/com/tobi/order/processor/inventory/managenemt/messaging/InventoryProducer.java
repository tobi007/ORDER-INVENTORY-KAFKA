package com.tobi.order.processor.inventory.managenemt.messaging;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final KafkaTemplate<String, Object> kafkaTemplateObject;

    public void updateInventoryQuantityByName(InventoryUpdateDTO inventoryUpdateDTO) {
        logger.info(String.format("Inventory update sent to kafka -> ProductName: %s, increase by %s", inventoryUpdateDTO.getProductName(), inventoryUpdateDTO.getQuantity()));
        this.kafkaTemplateObject.send(MessagingConstant.INVENTORY_UPDATE_TOPIC_NAME, inventoryUpdateDTO.getProductName(), inventoryUpdateDTO);
    }
}
