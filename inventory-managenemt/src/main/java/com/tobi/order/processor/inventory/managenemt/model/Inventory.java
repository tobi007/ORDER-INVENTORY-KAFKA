package com.tobi.order.processor.inventory.managenemt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    private String productName;
    private Long quantity;
    private Date created;
    private Date updated;

    @Override
    public String toString() {
        return "Inventory [productName=" + productName + ", quantity="
                + quantity.toString() + "]";
    }
}
