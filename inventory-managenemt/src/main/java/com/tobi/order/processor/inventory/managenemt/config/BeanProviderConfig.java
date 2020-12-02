package com.tobi.order.processor.inventory.managenemt.config;

import com.tobi.order.processor.commons.config.KafkaConfig;
import com.tobi.order.processor.commons.constant.MessagingConstant;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class BeanProviderConfig {

    private final TopologyConfigurerBuliderConfig topologyConfigurerBuliderConfig;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    private String applicationId = MessagingConstant.INVENTORY_MANAGEMENT_GROUP_ID;

    @Bean("nopainStreamsBuilderFactoryBean")
    @Primary
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean() throws Exception {
        return new KafkaConfig(bootstrapServers, applicationId, topologyConfigurerBuliderConfig)
                .getStreamsBuilderFactoryBean();
    }

    @PostConstruct
    private void createTopics() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        try (AdminClient client = AdminClient.create(properties)) {
            List<NewTopic> topics = Arrays.asList(
                    new NewTopic(MessagingConstant.PRODUCT_CREATION_TOPIC_NAME, 1, (short) 1),
                    new NewTopic(MessagingConstant.PRODUCT_UPDATE_TOPIC_NAME, 1, (short) 1),
                    new NewTopic(MessagingConstant.INVENTORY_UPDATE_TOPIC_NAME, 1, (short) 1),
                    new NewTopic(MessagingConstant.ORDER_CREATION_TOPIC_NAME, 1, (short) 1),
                    new NewTopic(MessagingConstant.ORDER_REPORT_CREATION_TOPIC_NAME, 1, (short) 1)
            );

            for (NewTopic newTopic : topics) {
                client.createTopics(Collections.singleton(newTopic));
            }

        }
    }
}
