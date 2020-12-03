package com.tobi.order.processor.inventory.managenemt.controller;

import com.tobi.order.processor.commons.model.CustomResponse;
import com.tobi.order.processor.inventory.managenemt.model.OrderCreateDTO;
import com.tobi.order.processor.inventory.managenemt.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(value = "State Store Rest API")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @ApiOperation("Create a new Order Item")
    @PostMapping(value = "/order/create")
    public CustomResponse createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        orderService.createOrder(orderCreateDTO);
        return new CustomResponse(true, "", null);
    }
}
