package com.tobi.order.processor.inventory.managenemt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateDTO {
    private String productName;
    private Long quantity;
    private Boolean isRestock;
}
