package com.tobi.order.processor.inventory.managenemt.config;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

class TopologyConfigurerBuliderConfigTest {

    private TopologyTestDriver testDriver;

    private TestInputTopic<String, Product> productCreationInputTopic;
    private TestInputTopic<String, Product> productUpdateInputTopic;
    private TestInputTopic<String, InventoryUpdateDTO> inventoryUpdateInputTopic;
    private TestInputTopic<String, Order> orderCreationInputTopic;
    private TestOutputTopic<String, Order> orderReportCreationOutTopic;

    private KeyValueStore<String, Product> productStateStore;
    private KeyValueStore<String, Inventory> inventoryStateStore;
    private KeyValueStore<String, Order> orderStateStore;

    private Product defaultProduct;
    private Inventory defaultInventory;
    private Order defaultOrder;

    @BeforeEach
    void beforeEach() throws Exception {
        Topology topology = new Topology();
        TopologyConfigurerBuliderConfig.buildTopology(topology);

        Properties config = new Properties();
        config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "maxAggregation");
        config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());

        testDriver = new TopologyTestDriver(topology, config);

        defaultProduct = new Product("Salt", "For your tast", BigDecimal.TEN, true);

        defaultInventory = new Inventory(defaultProduct.getName(), 34L, new Date(), new Date());

        defaultOrder = new Order();
        defaultOrder.setCustomerEmail("eeee@fff.com");
        defaultOrder.setProductName(defaultProduct.getName());
        defaultOrder.setProductPrice(defaultProduct.getPrice());
        defaultOrder.setProductQuantity(10L);
        defaultOrder.setOrderedOn(new Date());
        defaultOrder.setIsProcessed(true);

        // pre-populate store
        productStateStore = testDriver.getKeyValueStore(MessagingConstant.PRODUCTS_K_TABLE_NAME);
        productStateStore.put(defaultProduct.getName(), defaultProduct);

        inventoryStateStore = testDriver.getKeyValueStore(MessagingConstant.INVENTORIES_K_TABLE_NAME);
        inventoryStateStore.put(defaultInventory.getProductName(), defaultInventory);

        orderStateStore = testDriver.getKeyValueStore(MessagingConstant.ORDERS_K_TABLE_NAME);
        orderStateStore.put(defaultOrder.getId(), defaultOrder);

        productCreationInputTopic = testDriver.createInputTopic(MessagingConstant.PRODUCT_CREATION_TOPIC_NAME, new StringSerializer(), new JsonSerializer<Product>());
        productUpdateInputTopic = testDriver.createInputTopic(MessagingConstant.PRODUCT_UPDATE_TOPIC_NAME, new StringSerializer(), new JsonSerializer<Product>());
        inventoryUpdateInputTopic = testDriver.createInputTopic(MessagingConstant.INVENTORY_UPDATE_TOPIC_NAME, new StringSerializer(), new JsonSerializer<InventoryUpdateDTO>());
        orderCreationInputTopic = testDriver.createInputTopic(MessagingConstant.ORDER_CREATION_TOPIC_NAME, new StringSerializer(), new JsonSerializer<Order>());

        orderReportCreationOutTopic = testDriver.createOutputTopic(MessagingConstant.ORDER_REPORT_CREATION_TOPIC_NAME, new StringDeserializer(), new JsonDeserializer<>(Order.class));    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void stateStore_is_initialized_with_default() {
        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(defaultProduct).usingRecursiveComparison().isEqualTo(productStateStore.get(defaultProduct.getName()));

        Assertions.assertThat(inventoryStateStore.get(defaultInventory.getProductName())).isNotNull();
        Assertions.assertThat(defaultInventory).usingRecursiveComparison().isEqualTo(inventoryStateStore.get(defaultInventory.getProductName()));

        Assertions.assertThat(orderStateStore.get(defaultOrder.getId())).isNotNull();
        Assertions.assertThat(defaultOrder).usingRecursiveComparison().isEqualTo(orderStateStore.get(defaultOrder.getId()));
    }

    @Test
    public void assert_that_no_product_is_created_if_it_exist() {

        productCreationInputTopic.pipeInput(defaultProduct.getName(), defaultProduct);

        List<Product> products = getAllItemsFromStore(productStateStore);

        Assertions.assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void assert_that_product_and_inventory_are_created_if_it_not_exist() {

        Product product = new Product("Rice", "Feed the Nation", BigDecimal.valueOf(2334L), false);

        productCreationInputTopic.pipeInput(product.getName(), product);

        Assertions.assertThat(productStateStore.get(product.getName())).isNotNull();
        Assertions.assertThat(product).usingRecursiveComparison().isEqualTo(productStateStore.get(product.getName()));

        Inventory inventory = inventoryStateStore.get(product.getName());

        Assertions.assertThat(inventory).isNotNull();
        Assertions.assertThat(inventory.getQuantity()).isEqualTo(0);
    }

    @Test
    public void assert_that_product_can_be_updated_without_affecting_order() {
        defaultProduct.setPrice(BigDecimal.valueOf(2345L));
        defaultProduct.setDescription("just a random one");

        productUpdateInputTopic.pipeInput(defaultProduct.getName(), defaultProduct);

        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(defaultProduct).usingRecursiveComparison().isEqualTo(productStateStore.get(defaultProduct.getName()));

        Order order = orderStateStore.get(defaultOrder.getId());

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(productStateStore.get(defaultProduct.getName()).getPrice()).isNotEqualTo(order.getProductPrice());
    }

    @Test
    public void assert_that_inventory_can_be_updated_and_product_availability_toggled() {
        Long stockQuantity = 0L;

        inventoryUpdateInputTopic.pipeInput(defaultInventory.getProductName(), new InventoryUpdateDTO(defaultInventory.getProductName(), stockQuantity, true));

        Assertions.assertThat(inventoryStateStore.get(defaultInventory.getProductName())).isNotNull();
        Assertions.assertThat(defaultInventory).usingRecursiveComparison().isEqualTo(inventoryStateStore.get(defaultInventory.getProductName()));

        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(defaultProduct).usingRecursiveComparison().isEqualTo(productStateStore.get(defaultProduct.getName()));

        stockQuantity = 100L;

        inventoryUpdateInputTopic.pipeInput(defaultInventory.getProductName(), new InventoryUpdateDTO(defaultInventory.getProductName(), stockQuantity, true));

        Assertions.assertThat(inventoryStateStore.get(defaultInventory.getProductName())).isNotNull();
        Assertions.assertThat(defaultInventory.getQuantity() + stockQuantity).usingRecursiveComparison().isEqualTo(inventoryStateStore.get(defaultInventory.getProductName()).getQuantity());

        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(defaultProduct).usingRecursiveComparison().isEqualTo(productStateStore.get(defaultProduct.getName()));


        stockQuantity = defaultInventory.getQuantity() + stockQuantity;

        inventoryUpdateInputTopic.pipeInput(defaultInventory.getProductName(), new InventoryUpdateDTO(defaultInventory.getProductName(), stockQuantity, false));

        Assertions.assertThat(inventoryStateStore.get(defaultInventory.getProductName())).isNotNull();
        Assertions.assertThat(0L).usingRecursiveComparison().isEqualTo(inventoryStateStore.get(defaultInventory.getProductName()).getQuantity());

        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(false).isEqualTo(productStateStore.get(defaultProduct.getName()).getIsAvailable());
    }

    @Test
    public void assert_that_order_cannot_be_processed_if_inventory_is_less() {
        Order order = new Order();
        order.setCustomerEmail("dddddd");
        order.setProductName(defaultProduct.getName());
        order.setProductPrice(defaultProduct.getPrice());
        order.setProductQuantity(defaultInventory.getQuantity() * 2);
        order.setOrderedOn(new Date());
        order.setIsProcessed(false);

        orderCreationInputTopic.pipeInput(order.getId(), order);

        Assertions.assertThat(orderStateStore.get(order.getId())).isNull();
    }

    @Test
    public void assert_that_order_can_be_processed_if_inventory_is_greater_or_equal() {
        Order order = new Order();
        order.setCustomerEmail("dddddd");
        order.setProductName(defaultProduct.getName());
        order.setProductPrice(defaultProduct.getPrice());
        order.setProductQuantity(defaultInventory.getQuantity() / 2);
        order.setOrderedOn(new Date());
        order.setIsProcessed(false);

        orderCreationInputTopic.pipeInput(order.getId(), order);

        Assertions.assertThat(orderStateStore.get(order.getId())).isNotNull();
        Assertions.assertThat(true).isEqualTo(orderStateStore.get(order.getId()).getIsProcessed());

        Inventory inventory = inventoryStateStore.get(defaultProduct.getName());

        Assertions.assertThat(inventory).isNotNull();
        Assertions.assertThat(defaultInventory.getQuantity() - order.getProductQuantity()).isEqualTo(inventory.getQuantity());

        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(true).isEqualTo(productStateStore.get(defaultProduct.getName()).getIsAvailable());

        order.setProductQuantity(inventory.getQuantity());

        orderCreationInputTopic.pipeInput(order.getId(), order);

        Assertions.assertThat(orderStateStore.get(order.getId())).isNotNull();
        Assertions.assertThat(true).isEqualTo(orderStateStore.get(order.getId()).getIsProcessed());

        inventory = inventoryStateStore.get(defaultProduct.getName());

        Assertions.assertThat(inventory).isNotNull();
        Assertions.assertThat(0L).isEqualTo(inventory.getQuantity());

        Assertions.assertThat(productStateStore.get(defaultProduct.getName())).isNotNull();
        Assertions.assertThat(false).isEqualTo(productStateStore.get(defaultProduct.getName()).getIsAvailable());
    }

    @Test
    public void assert_that_reporting_service_can_receive_order() {
        Order order = new Order();
        order.setCustomerEmail("dddddd");
        order.setProductName(defaultProduct.getName());
        order.setProductPrice(defaultProduct.getPrice());
        order.setProductQuantity(defaultInventory.getQuantity() / 2);
        order.setOrderedOn(new Date());
        order.setIsProcessed(false);

        orderCreationInputTopic.pipeInput(order.getId(), order);

        order.setIsProcessed(true);
        Order processedOrder = orderReportCreationOutTopic.readKeyValue().value;

        Assertions.assertThat(processedOrder).isNotNull();
        Assertions.assertThat(order).usingRecursiveComparison().isEqualTo(processedOrder);
    }



    private static <T> List<T> getAllItemsFromStore(KeyValueStore<String, T> stateStore) {
        List<T> items = new ArrayList<>();
        stateStore.all().forEachRemaining(keyValue -> items.add(keyValue.value));
        return items;
    }
}