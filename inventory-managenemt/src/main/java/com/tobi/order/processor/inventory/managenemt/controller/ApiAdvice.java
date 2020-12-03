package com.tobi.order.processor.inventory.managenemt.controller;

import com.tobi.order.processor.commons.model.CustomResponse;
import com.tobi.order.processor.inventory.managenemt.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class ApiAdvice {

    @ExceptionHandler({
            ProductExistException.class,
            ProductNotExistException.class,
            ProductNotAvailableException.class,
            InventoryExistException.class,
            InventoryNotExistException.class,
            InventoryUpdateLessThanOneExistException.class,
    })
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public CustomResponse handleNotFoundException(ProductExistException e, HttpServletRequest request) {
        return new CustomResponse(false, e.getMessage(), null);
    }
}
