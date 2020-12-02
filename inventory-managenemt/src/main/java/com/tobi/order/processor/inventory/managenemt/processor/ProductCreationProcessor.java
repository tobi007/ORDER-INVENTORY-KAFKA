package com.tobi.order.processor.inventory.managenemt.processor;

import com.tobi.order.processor.inventory.managenemt.model.Product;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class ProductCreationProcessor implements Processor<String, Product> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private KeyValueStore<String, Product> stateStore;

    private final String stateStoreName;

    public ProductCreationProcessor(String stateStoreName) {
        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        stateStore = (KeyValueStore<String, Product>) processorContext.getStateStore(stateStoreName);

        Objects.requireNonNull(stateStore, "State store can't be null");
    }

    @Override
    public void process(String name, Product product) {
        if (name == null)
          return;

        stateStore.put(name, product);
    }

    @Override
    public void close() {

    }

}
