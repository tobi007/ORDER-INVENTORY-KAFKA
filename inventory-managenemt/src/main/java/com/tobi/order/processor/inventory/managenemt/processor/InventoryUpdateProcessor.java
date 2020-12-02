package com.tobi.order.processor.inventory.managenemt.processor;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import lombok.SneakyThrows;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.To;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;


public class InventoryUpdateProcessor implements Processor<String, InventoryUpdateDTO> {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    private KeyValueStore<String, Product> productStateStore;
    private KeyValueStore<String, Inventory> inventoryStateStore;

    private final String productStateStoreName;
    private final String inventoryStateStoreName;

    private ProcessorContext processorContext;

    public InventoryUpdateProcessor(String productStateStoreName, String inventoryStateStoreName) {
        this.productStateStoreName = productStateStoreName;
        this.inventoryStateStoreName = inventoryStateStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        productStateStore = (KeyValueStore<String, Product>) processorContext.getStateStore(productStateStoreName);
        inventoryStateStore = (KeyValueStore<String, Inventory>) processorContext.getStateStore(inventoryStateStoreName);

        this.processorContext = processorContext;

        Objects.requireNonNull(productStateStore, "State store can't be null");
        Objects.requireNonNull(inventoryStateStore, "State store can't be null");
    }

    @SneakyThrows
    @Override
    public void process(String productName, InventoryUpdateDTO inventoryUpdateDTO) {
        if (productName == null)
          return;
        Product existingProduct = productStateStore.get(productName);
        if (existingProduct == null) {
          throw new ProductNotExistException(productName);
        }

        Inventory inventory = inventoryStateStore.get(productName);
        if (inventory != null) {
            if (inventoryUpdateDTO.getQuantity() < 1) {
                return;
            }


            if (inventoryUpdateDTO.getIsRestock()) {
                inventory.setQuantity(inventory.getQuantity() + inventoryUpdateDTO.getQuantity());
            } else {
                if (inventoryUpdateDTO.getQuantity() > inventory.getQuantity()){
                    return;
                }
                inventory.setQuantity(inventory.getQuantity() - inventoryUpdateDTO.getQuantity());
            }

            inventory.setUpdated(new Date());


            if (inventory.getQuantity() > 0) {
                existingProduct.setIsAvailable(true);
            } else {
                existingProduct.setIsAvailable(false);
            }

            inventoryStateStore.put(productName, inventory);
            logger.info("Inventory Updated: " + inventory);
            processorContext.forward(productName, existingProduct);
        } else {
            logger.info(String.format("Inventory with product name %s Does not Exist,: ", productName));
        }

    }

    @Override
    public void close() {

    }

}
