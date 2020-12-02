package com.tobi.order.processor.inventory.managenemt.config;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import com.tobi.order.processor.inventory.managenemt.processor.InventoryUpdateProcessor;
import com.tobi.order.processor.inventory.managenemt.processor.OrderCreationProcessor;
import com.tobi.order.processor.inventory.managenemt.processor.ProductCreationProcessor;
import com.tobi.order.processor.inventory.managenemt.processor.InventoryCreationProcessor;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class TopologyConfigurerBuliderConfig implements KafkaStreamsInfrastructureCustomizer {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Deserializer<String> KEY_JSON_DE = new StringDeserializer();

    private static final Serde<Product> PRODUCT_SERDE = new JsonSerde<>(Product.class).ignoreTypeHeaders();
    private static final Deserializer<Product> PRODUCT_DESERIALIZER =
            new JsonDeserializer<>(Product.class).ignoreTypeHeaders();

    private static final Serde<Inventory> INVENTORY_SERDE = new JsonSerde<>(Inventory.class).ignoreTypeHeaders();

    private static final Serde<Order> ORDER_SERDE = new JsonSerde<>(Order.class).ignoreTypeHeaders();
    private static final Deserializer<Order> ORDER_DESERIALIZER =
            new JsonDeserializer<>(Order.class).ignoreTypeHeaders();

    private static final Deserializer<InventoryUpdateDTO> INVENTORY_UPDATE_DTO_DESERIALIZER =
            new JsonDeserializer<>(InventoryUpdateDTO.class).ignoreTypeHeaders();



    @Override
    public void configureBuilder(StreamsBuilder builder) {

        Topology topology = builder.build();
        buildTopology(topology);
    }

    public static void buildTopology(Topology topology) {
        StoreBuilder<KeyValueStore<String, Product>> productStateStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(MessagingConstant.PRODUCTS_K_TABLE_NAME), STRING_SERDE, PRODUCT_SERDE);

        StoreBuilder<KeyValueStore<String, Inventory>> inventoryStateStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(MessagingConstant.INVENTORIES_K_TABLE_NAME), STRING_SERDE, INVENTORY_SERDE);

        StoreBuilder<KeyValueStore<String, Order>> orderStateStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(MessagingConstant.ORDERS_K_TABLE_NAME), STRING_SERDE, ORDER_SERDE);

        topology.addSource(MessagingConstant.PRODUCT_CREATION_SOURCE_NAME, KEY_JSON_DE, PRODUCT_DESERIALIZER, MessagingConstant.PRODUCT_CREATION_TOPIC_NAME)
                .addSource(MessagingConstant.PRODUCT_UPDATE_SOURCE_NAME, KEY_JSON_DE, PRODUCT_DESERIALIZER, MessagingConstant.PRODUCT_UPDATE_TOPIC_NAME)
                .addSource(MessagingConstant.INVENTORY_UPDATE_SOURCE_NAME, KEY_JSON_DE, INVENTORY_UPDATE_DTO_DESERIALIZER, MessagingConstant.INVENTORY_UPDATE_TOPIC_NAME)
                .addSource(MessagingConstant.ORDER_CREATION_SOURCE_NAME, KEY_JSON_DE, ORDER_DESERIALIZER, MessagingConstant.ORDER_CREATION_TOPIC_NAME)
                .addProcessor(MessagingConstant.ORDER_CREATION_PROCESSOR_NAME, () -> new OrderCreationProcessor(MessagingConstant.PRODUCTS_K_TABLE_NAME, MessagingConstant.INVENTORIES_K_TABLE_NAME, MessagingConstant.ORDERS_K_TABLE_NAME), MessagingConstant.ORDER_CREATION_SOURCE_NAME)
                .addProcessor(MessagingConstant.INVENTORY_UPDATE_PROCESSOR_NAME, () -> new InventoryUpdateProcessor(MessagingConstant.PRODUCTS_K_TABLE_NAME, MessagingConstant.INVENTORIES_K_TABLE_NAME), MessagingConstant.INVENTORY_UPDATE_SOURCE_NAME, MessagingConstant.ORDER_CREATION_PROCESSOR_NAME)
                .addProcessor(MessagingConstant.PRODUCT_CREATION_PROCESSOR_NAME, () -> new ProductCreationProcessor(MessagingConstant.PRODUCTS_K_TABLE_NAME), MessagingConstant.PRODUCT_CREATION_SOURCE_NAME)
                .addProcessor(MessagingConstant.PRODUCT_UPDATE_PROCESSOR_NAME, () -> new ProductCreationProcessor(MessagingConstant.PRODUCTS_K_TABLE_NAME), MessagingConstant.PRODUCT_UPDATE_SOURCE_NAME, MessagingConstant.INVENTORY_UPDATE_PROCESSOR_NAME, MessagingConstant.ORDER_CREATION_PROCESSOR_NAME)
                .addProcessor(MessagingConstant.INVENTORY_CREATION_PROCESSOR_NAME, () -> new InventoryCreationProcessor(MessagingConstant.PRODUCTS_K_TABLE_NAME, MessagingConstant.INVENTORIES_K_TABLE_NAME), MessagingConstant.PRODUCT_CREATION_SOURCE_NAME)
                .addStateStore(productStateStoreBuilder, MessagingConstant.PRODUCT_CREATION_PROCESSOR_NAME, MessagingConstant.PRODUCT_UPDATE_PROCESSOR_NAME, MessagingConstant.INVENTORY_CREATION_PROCESSOR_NAME, MessagingConstant.INVENTORY_UPDATE_PROCESSOR_NAME, MessagingConstant.ORDER_CREATION_PROCESSOR_NAME)
                .addStateStore(inventoryStateStoreBuilder, MessagingConstant.INVENTORY_CREATION_PROCESSOR_NAME, MessagingConstant.INVENTORY_UPDATE_PROCESSOR_NAME, MessagingConstant.ORDER_CREATION_PROCESSOR_NAME)
                .addStateStore(orderStateStoreBuilder, MessagingConstant.ORDER_CREATION_PROCESSOR_NAME)
                .addSink("TO_REPORTING_SERVICE_CREATION_SINK", MessagingConstant.ORDER_REPORT_CREATION_TOPIC_NAME, new StringSerializer(), new JsonSerializer<Order>(), MessagingConstant.ORDER_CREATION_PROCESSOR_NAME);
    }
}
