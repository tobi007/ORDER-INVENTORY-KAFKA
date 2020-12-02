package com.tobi.order.processor.reporting.service.service;

import com.tobi.order.processor.commons.constant.MessagingConstant;
import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.reporting.service.model.OrderReport;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {


    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;


    public Order getOrderById(String id) {
        return getReadOnlyStore().get(id);
    }

    public List<Order> getAllOrder() {
        List<Order> orders = new ArrayList<>();
        getReadOnlyStore().all().forEachRemaining(keyValue -> orders.add(keyValue.value));
        return orders;
    }

    public OrderReport getAllOrderGroupByDate() {

        return groupOrderByDate(getAllOrder());
    }


    public OrderReport getAllOrderByDateRangeGroupByDate(String from, String to) throws ParseException {
        Date fromDate = convertDateStringToFormattedDate(from);
        Date toDate = convertDateStringToFormattedDate(to);
        List<Order> orders = new ArrayList<>();

        getReadOnlyStore().all().forEachRemaining(keyValue -> {
            Order order = keyValue.value;

            if(fromDate.before(order.getOrderedOn()) && toDate.after(order.getOrderedOn()))
                orders.add(order);
        });

        return groupOrderByDate(orders);
    }

    private OrderReport groupOrderByDate(List<Order> orders) {
        OrderReport orderReport = new OrderReport();
        Map<String, List<Order>> orderGroup = new HashMap<>();

        Long orderCount = 0L;
        BigDecimal orderAmount = BigDecimal.ZERO;

        for (Order order: orders) {
            String dateString = convertDateToStringFormat(order.getOrderedOn());
            if (orderGroup.containsKey(dateString)) {
                List<Order> dateOrders = new ArrayList<>();
                dateOrders.addAll(orderGroup.get(dateString));
                dateOrders.add(order);
                orderGroup.put(dateString, dateOrders);
            } else {
                orderGroup.put(dateString, Collections.singletonList(order));
            }
            orderCount +=1;
            orderAmount = orderAmount.add(order.getProductPrice().multiply(BigDecimal.valueOf(order.getProductQuantity())));
        }
        orderReport.setOrders(orderGroup);
        orderReport.setTotalOrderCount(orderCount);
        orderReport.setTotalOrderAmount(orderAmount);

        return orderReport;
    }

    private String convertDateToStringFormat(Date originalDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(originalDate);
    }

    private Date convertDateStringToFormattedDate(String dateString) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        start.setTime(df.parse(dateString));
        return start.getTime();
    }


    private ReadOnlyKeyValueStore<String, Order> getReadOnlyStore() {
        return streamsBuilderFactoryBean.getKafkaStreams().store(MessagingConstant.ORDERS_REPORT_K_TABLE_NAME, QueryableStoreTypes.keyValueStore());
    }
}
