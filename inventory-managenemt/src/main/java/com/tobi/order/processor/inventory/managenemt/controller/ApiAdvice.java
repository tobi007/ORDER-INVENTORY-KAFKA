package com.tobi.order.processor.inventory.managenemt.controller;

import com.tobi.order.processor.inventory.managenemt.exception.InventoryExistException;
import com.tobi.order.processor.inventory.managenemt.exception.InventoryNotExistException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductExistException;
import com.tobi.order.processor.inventory.managenemt.exception.ProductNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class ApiAdvice {

    @ExceptionHandler({
            ProductExistException.class,
            ProductNotExistException.class,
            InventoryExistException.class,
            InventoryNotExistException.class,
    })
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public String handleNotFoundException(ProductExistException e, HttpServletRequest request) {
        return e.getMessage();
    }
}
