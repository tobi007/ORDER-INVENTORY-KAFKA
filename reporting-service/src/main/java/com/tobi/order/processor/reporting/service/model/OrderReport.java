package com.tobi.order.processor.reporting.service.model;

import com.tobi.order.processor.commons.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderReport {
    private Long totalOrderCount;
    private BigDecimal totalOrderAmount;
    private Map<String, List<Order>> orders = new HashMap<>();
}
