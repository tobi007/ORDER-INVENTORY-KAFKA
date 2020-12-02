package com.tobi.order.processor.inventory.managenemt.service;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.inventory.managenemt.exception.InventoryNotExistException;
import com.tobi.order.processor.inventory.managenemt.exception.InventoryUpdateLessThanOneExistException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductExistException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import com.tobi.order.processor.inventory.managenemt.messaging.InventoryProducer;
import com.tobi.order.processor.inventory.managenemt.messaging.ProductProducer;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {


    private final InventoryProducer inventoryProducer;
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private final ProductService productService;


    public Inventory getInventoryByProductName(String productName) {
        return getReadOnlyStore().get(productName);
    }

    public List<Inventory> getAllInventory() {
        List<Inventory> inventories = new ArrayList<>();
        System.out.println(getReadOnlyStore().all().hasNext());
        getReadOnlyStore().all().forEachRemaining(keyValue -> inventories.add(keyValue.value));
        return inventories;
    }

    public void updateInventoryQuantityByName(InventoryUpdateDTO inventoryUpdateDTO) {
        Product existingProduct = productService.getProductByName(inventoryUpdateDTO.getProductName());
        if (existingProduct == null) {
            throw new ProductNotExistException(inventoryUpdateDTO.getProductName());
        }

        Inventory existingInventory = getInventoryByProductName(inventoryUpdateDTO.getProductName());
        if (existingInventory == null) {
            throw new InventoryNotExistException(inventoryUpdateDTO.getProductName());
        }

        if (inventoryUpdateDTO.getIsRestock() && inventoryUpdateDTO.getQuantity() < 1) {
            throw new InventoryUpdateLessThanOneExistException(inventoryUpdateDTO.getProductName());
        }

        inventoryProducer.updateInventoryQuantityByName(inventoryUpdateDTO);
    }

    private ReadOnlyKeyValueStore<String, Inventory> getReadOnlyStore() {
        return streamsBuilderFactoryBean.getKafkaStreams().store(MessagingConstant.INVENTORIES_K_TABLE_NAME, QueryableStoreTypes.keyValueStore());
    }
}
