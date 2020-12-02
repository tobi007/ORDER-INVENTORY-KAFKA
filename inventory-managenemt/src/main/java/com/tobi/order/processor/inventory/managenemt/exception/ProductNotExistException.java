package com.tobi.order.processor.inventory.managenemt.exception;

public class ProductNotExistException extends RuntimeException {

    public ProductNotExistException(String name) {
        super(String.format("Exception occurred:: Product with name %s does not exist", name));
    }

}
