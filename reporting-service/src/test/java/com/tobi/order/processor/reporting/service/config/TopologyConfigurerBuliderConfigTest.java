package com.tobi.order.processor.reporting.service.config;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;

class TopologyConfigurerBuliderConfigTest {

    private TopologyTestDriver testDriver;

    private TestInputTopic<String, Order> orderCreationInputTopic;//
    private KeyValueStore<String, Order> stateStore;

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

        defaultOrder = new Order();
        defaultOrder.setCustomerEmail("eeee@fff.com");
        defaultOrder.setProductName("Salt");
        defaultOrder.setProductPrice(BigDecimal.TEN);
        defaultOrder.setProductQuantity(555L);
        defaultOrder.setOrderedOn(new Date());
        defaultOrder.setIsProcessed(true);

        // pre-populate store
        stateStore = testDriver.getKeyValueStore(MessagingConstant.ORDERS_REPORT_K_TABLE_NAME);
        stateStore.put(defaultOrder.getId(), defaultOrder);

        orderCreationInputTopic = testDriver.createInputTopic(MessagingConstant.ORDER_REPORT_CREATION_TOPIC_NAME, new StringSerializer(), new JsonSerializer<Order>());
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void stateStore_is_initialized_with_default_order() {
        Assertions.assertThat(stateStore.get(defaultOrder.getId())).isNotNull();
        Assertions.assertThat(defaultOrder).usingRecursiveComparison().isEqualTo(stateStore.get(defaultOrder.getId()));
    }

    @Test
    public void assert_that_order_is_not_created_from_input_topic_when_processed() {
        Order order = new Order();
        order.setCustomerEmail("dddddd");
        order.setProductName("dddddddd");
        order.setProductPrice(BigDecimal.TEN);
        order.setProductQuantity(2L);
        order.setOrderedOn(new Date());
        order.setIsProcessed(false);

        orderCreationInputTopic.pipeInput(order.getId(), order);

        Assertions.assertThat(stateStore.get(order.getId())).isNull();
    }

    @Test
    public void assert_that_order_is_created_from_input_topic_when_processed() {
        Order order = new Order();
        order.setCustomerEmail("dddddd");
        order.setProductName("dddddddd");
        order.setProductPrice(BigDecimal.TEN);
        order.setProductQuantity(2L);
        order.setOrderedOn(new Date());
        order.setIsProcessed(true);

        orderCreationInputTopic.pipeInput(order.getId(), order);

        Assertions.assertThat(stateStore.get(order.getId())).isNotNull();
        Assertions.assertThat(order).usingRecursiveComparison().isEqualTo(stateStore.get(order.getId()));
    }
}