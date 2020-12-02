package com.tobi.order.processor.inventory.managenemt.exception;

public class ProductExistException extends RuntimeException {

    public ProductExistException(String name) {
        super(String.format("Exception occurred:: Product with name %s already exist", name));
    }

}
