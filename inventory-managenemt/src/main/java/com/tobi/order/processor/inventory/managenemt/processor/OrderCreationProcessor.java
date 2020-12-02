package com.tobi.order.processor.inventory.managenemt.processor;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.To;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;


public class OrderCreationProcessor implements Processor<String, Order> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private KeyValueStore<String, Product> productStateStore;
    private KeyValueStore<String, Inventory> inventoryStateStore;
    private KeyValueStore<String, Order> orderStateStore;

    private ProcessorContext processorContext;

    private final String productStateStoreName;
    private final String inventoryStateStoreName;
    private final String orderStateStoreName;


    public OrderCreationProcessor(String productStateStoreName, String inventoryStateStoreName, String orderStateStoreName) {
        this.productStateStoreName = productStateStoreName;
        this.inventoryStateStoreName = inventoryStateStoreName;
        this.orderStateStoreName = orderStateStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        productStateStore = (KeyValueStore<String, Product>) processorContext.getStateStore(productStateStoreName);
        inventoryStateStore = (KeyValueStore<String, Inventory>) processorContext.getStateStore(inventoryStateStoreName);
        orderStateStore = (KeyValueStore<String, Order>) processorContext.getStateStore(orderStateStoreName);

        this.processorContext = processorContext;

        Objects.requireNonNull(productStateStore, "State store can't be null");
        Objects.requireNonNull(inventoryStateStore, "State store can't be null");
        Objects.requireNonNull(orderStateStore, "State store can't be null");
    }

    @Override
    public void process(String name, Order order) {
        if (name == null)
          return;
        Product existingProduct = productStateStore.get(order.getProductName());
        if (existingProduct == null) {
            throw new ProductNotExistException(order.getProductName());
        }

        Inventory inventory = inventoryStateStore.get(order.getProductName());
        if (inventory != null) {
            if (inventory.getQuantity() > 0 && inventory.getQuantity() >= order.getProductQuantity()) {
                processorContext.forward(order.getProductName(), new InventoryUpdateDTO(order.getProductName(), order.getProductQuantity(), false), To.child(MessagingConstant.INVENTORY_UPDATE_PROCESSOR_NAME));
                order.setIsProcessed(true);
                orderStateStore.put(name, order);
                processorContext.forward(name, order, To.child("TO_REPORTING_SERVICE_CREATION_SINK"));

            } else {
                logger.info(String.format("Could not Fullfill order with product name %s ,: ", name));
            }

        } else {
            logger.info(String.format("Inventory with product name %s Does not Exist,: ", name));
        }
    }

    @Override
    public void close() {

    }

}
