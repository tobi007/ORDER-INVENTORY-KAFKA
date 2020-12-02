package com.tobi.order.processor.inventory.managenemt.exception;

public class InventoryExistException extends RuntimeException {

    public InventoryExistException(String name) {
        super(String.format("Exception occurred:: Inventory with product name %s already exist", name));
    }

}
