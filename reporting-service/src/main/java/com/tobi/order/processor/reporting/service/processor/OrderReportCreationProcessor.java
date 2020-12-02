package com.tobi.order.processor.reporting.service.processor;

import com.tobi.order.processor.commons.model.Order;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class OrderReportCreationProcessor implements Processor<String, Order> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private KeyValueStore<String, Order> stateStore;

    private final String stateStoreName;

    public OrderReportCreationProcessor(String stateStoreName) {
        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        stateStore = (KeyValueStore<String, Order>) processorContext.getStateStore(stateStoreName);

        Objects.requireNonNull(stateStore, "State store can't be null");
    }

    @Override
    public void process(String name, Order order) {
        if (name == null)
          return;

        if (order.getIsProcessed()) {
            stateStore.put(name, order);
        }
    }

    @Override
    public void close() {

    }

}
