package com.tobi.order.processor.inventory.managenemt.exception;

public class InventoryUpdateLessThanOneExistException extends RuntimeException {

    public InventoryUpdateLessThanOneExistException(String productName) {
        super(String.format("Exception occurred:: Inventory with productName %s cannot be restock with negative quantity", productName));
    }
}
