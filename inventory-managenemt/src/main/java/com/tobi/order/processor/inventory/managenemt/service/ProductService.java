package com.tobi.order.processor.inventory.managenemt.service;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.inventory.managenemt.exception.ProductExistException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import com.tobi.order.processor.inventory.managenemt.messaging.ProductProducer;
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
public class ProductService {

    private final ProductProducer productProducer;

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public void createProduct(Product product) {
        Product existingProduct = getProductByName(product.getName());
        if (existingProduct != null) {
            throw new ProductExistException(product.getName());
        }

        productProducer.createProduct(product);
    }

    public void updateProduct(Product product) {
        Product existingProduct = getProductByName(product.getName());
        if (existingProduct == null) {
            throw new ProductNotExistException(product.getName());
        }

        productProducer.updateProduct(product);
    }

    public Product getProductByName(String name) {
        return getReadOnlyStore().get(name);
    }

    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        getReadOnlyStore().all().forEachRemaining(keyValue -> products.add(keyValue.value));
        return products;
    }

    public List<Product> getAllAvailableProducts() {
        List<Product> products = new ArrayList<>();
        getReadOnlyStore().all().forEachRemaining(keyValue -> {
            Product product = keyValue.value;
            if (product.getIsAvailable()) {
                products.add(keyValue.value);
            }
        });
        return products;
    }

    private ReadOnlyKeyValueStore<String, Product> getReadOnlyStore() {
        return streamsBuilderFactoryBean.getKafkaStreams().store(MessagingConstant.PRODUCTS_K_TABLE_NAME, QueryableStoreTypes.keyValueStore());
    }
}
