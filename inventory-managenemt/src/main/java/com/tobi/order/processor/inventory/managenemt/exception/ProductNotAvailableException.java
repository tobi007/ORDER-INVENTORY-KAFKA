package com.tobi.order.processor.inventory.managenemt.exception;

public class ProductNotAvailableException extends RuntimeException {

    public ProductNotAvailableException(String productName) {
        super(String.format("Exception occurred:: Product with name %s is not Available", productName));
    }
}
