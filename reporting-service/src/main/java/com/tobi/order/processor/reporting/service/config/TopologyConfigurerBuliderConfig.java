package com.tobi.order.processor.reporting.service.config;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.reporting.service.processor.OrderReportCreationProcessor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.To;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class TopologyConfigurerBuliderConfig implements KafkaStreamsInfrastructureCustomizer {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Deserializer<String> KEY_JSON_DE = new StringDeserializer();


    private static final Serde<Order> ORDER_SERDE = new JsonSerde<>(Order.class).ignoreTypeHeaders();
    private static final Deserializer<Order> ORDER_DESERIALIZER =
            new JsonDeserializer<>(Order.class).ignoreTypeHeaders();



  @Override
  public void configureBuilder(StreamsBuilder builder) {
    Topology topology = builder.build();
    buildTopology(topology);
  }

  public static void buildTopology(Topology topology) {
      StoreBuilder<KeyValueStore<String, Order>> orderReportStateStoreBuilder =
              Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(MessagingConstant.ORDERS_REPORT_K_TABLE_NAME), STRING_SERDE, ORDER_SERDE);

      topology.addSource(MessagingConstant.ORDER_REPORT_CREATION_SOURCE_NAME, KEY_JSON_DE, ORDER_DESERIALIZER, MessagingConstant.ORDER_REPORT_CREATION_TOPIC_NAME)
              .addProcessor(MessagingConstant.ORDER_REPORT_CREATION_PROCESSOR_NAME, () -> new OrderReportCreationProcessor(MessagingConstant.ORDERS_REPORT_K_TABLE_NAME), MessagingConstant.ORDER_REPORT_CREATION_SOURCE_NAME)
              .addStateStore(orderReportStateStoreBuilder, MessagingConstant.ORDER_REPORT_CREATION_PROCESSOR_NAME);
  }

}
