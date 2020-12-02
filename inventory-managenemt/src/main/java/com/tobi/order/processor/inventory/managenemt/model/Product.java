package com.tobi.order.processor.inventory.managenemt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isAvailable = false;

    @Override
    public String toString() {
        return "Product [name=" + name + ", price="
                + price.toString() + ", isAvailable="
                + isAvailable + "]";
    }
}
