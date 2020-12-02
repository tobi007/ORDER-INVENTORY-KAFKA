package com.tobi.order.processor.inventory.managenemt.processor;

import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;


public class InventoryCreationProcessor implements Processor<String, Product> {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    private KeyValueStore<String, Product> productStateStore;
    private KeyValueStore<String, Inventory> inventoryStateStore;

    private final String productStateStoreName;
    private final String inventoryStateStoreName;

    public InventoryCreationProcessor(String productStateStoreName, String inventoryStateStoreName) {
        this.productStateStoreName = productStateStoreName;
        this.inventoryStateStoreName = inventoryStateStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        productStateStore = (KeyValueStore<String, Product>) processorContext.getStateStore(productStateStoreName);
        inventoryStateStore = (KeyValueStore<String, Inventory>) processorContext.getStateStore(inventoryStateStoreName);

        Objects.requireNonNull(productStateStore, "State store can't be null");
        Objects.requireNonNull(inventoryStateStore, "State store can't be null");
    }

    @Override
    public void process(String name, Product product) {
        if (name == null)
          return;
        Product existingProduct = productStateStore.get(name);
        if (existingProduct == null) {
          throw new ProductNotExistException(product.getName());
        }

        Inventory inventory = inventoryStateStore.get(name);
        if (inventory == null) {
            inventory = new Inventory(product.getName(), 0L, new Date(), new Date());
            inventoryStateStore.put(name, inventory);
            logger.info("Inventory Created: " + inventory);
        } else {
            logger.info("Inventory Already Exist: " + inventory);
        }

    }

    @Override
    public void close() {

    }

}
