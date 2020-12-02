package com.tobi.order.processor.inventory.managenemt.service;

import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.inventory.managenemt.exception.InventoryNotExistException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotAvailableException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import com.tobi.order.processor.inventory.managenemt.messaging.OrderProducer;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.OrderCreateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class OrderService {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final OrderProducer orderProducer;


    public void createOrder(OrderCreateDTO orderCreateDTO) {
        //TODO verify customer

        Product existingProduct = productService.getProductByName(orderCreateDTO.getProductName());
        if (existingProduct == null) {
            throw new ProductNotExistException(orderCreateDTO.getProductName());
        }

        if (!existingProduct.getIsAvailable()) {
            throw new ProductNotAvailableException(orderCreateDTO.getProductName());
        }

        Inventory existingInventory = inventoryService.getInventoryByProductName(orderCreateDTO.getProductName());
        if (existingInventory == null) {
            throw new InventoryNotExistException(orderCreateDTO.getProductName());
        }

        if (existingInventory.getQuantity() < 1 || existingInventory.getQuantity() < orderCreateDTO.getProductQuantity() ) {
            throw new ProductNotAvailableException(orderCreateDTO.getProductName());
        }

        Order order = new Order();
        order.setCustomerEmail(orderCreateDTO.getCustomerEmail());
        order.setProductName(orderCreateDTO.getProductName());
        order.setProductPrice(existingProduct.getPrice());
        order.setProductQuantity(orderCreateDTO.getProductQuantity());
        order.setOrderedOn(new Date());

        orderProducer.createOrder(order);
    }

}
