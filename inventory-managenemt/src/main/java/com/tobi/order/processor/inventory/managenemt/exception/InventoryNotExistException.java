package com.tobi.order.processor.inventory.managenemt.exception;

public class InventoryNotExistException extends RuntimeException {

    public InventoryNotExistException(String name) {
        super(String.format("Exception occurred:: Inventory with productName %s does not exist", name));
    }

}
