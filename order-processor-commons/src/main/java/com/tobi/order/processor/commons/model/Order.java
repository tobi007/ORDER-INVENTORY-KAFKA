package com.tobi.order.processor.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private String id = UUID.randomUUID().toString();
    private String customerEmail;
    private String productName;
    private BigDecimal productPrice;
    private Long productQuantity;
    private Date orderedOn;
    private Boolean isProcessed = false;
}
