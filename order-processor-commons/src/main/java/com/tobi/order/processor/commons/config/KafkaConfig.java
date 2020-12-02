package com.tobi.order.processor.commons.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.*;

public class KafkaConfig {

    private final KafkaStreamsInfrastructureCustomizer kafkaStreamsInfrastructureCustomizer;

    private KafkaStreamsConfiguration kafkaStreamsConfigConfiguration;


    public KafkaConfig (String bootstrapServers, String applicationId, KafkaStreamsInfrastructureCustomizer kafkaStreamsInfrastructureCustomizer) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(APPLICATION_ID_CONFIG, applicationId);
        configs.put(DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(NUM_STREAM_THREADS_CONFIG, 1);
        configs.put(PROCESSING_GUARANTEE_CONFIG, "exactly_once");
        configs.put(consumerPrefix(SESSION_TIMEOUT_MS_CONFIG), 10000);
        configs.put(consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");
        // PROD CONFS
        configs.put(DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        configs.put(REPLICATION_FACTOR_CONFIG, 1);
        configs.put(CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024L); // 10MB cache
        configs.put(producerPrefix(ProducerConfig.ACKS_CONFIG), "all");
        configs.put(producerPrefix(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG), 2147483647);
        configs.put(producerPrefix(ProducerConfig.MAX_BLOCK_MS_CONFIG), 9223372036854775807L);


        this.kafkaStreamsConfigConfiguration = new KafkaStreamsConfiguration(configs);


        this.kafkaStreamsInfrastructureCustomizer = kafkaStreamsInfrastructureCustomizer;
    }

    public StreamsBuilderFactoryBean getStreamsBuilderFactoryBean() throws Exception {

        StreamsBuilderFactoryBean streamsBuilderFactoryBean =
            new StreamsBuilderFactoryBean(kafkaStreamsConfigConfiguration);
        streamsBuilderFactoryBean.afterPropertiesSet();
        streamsBuilderFactoryBean.setInfrastructureCustomizer(kafkaStreamsInfrastructureCustomizer);
        streamsBuilderFactoryBean.setCloseTimeout(10); //10 seconds
        return streamsBuilderFactoryBean;
    }
}
