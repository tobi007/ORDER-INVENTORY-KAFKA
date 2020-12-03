package com.tobi.order.processor.reporting.service.controller;

import com.tobi.order.processor.commons.model.CustomResponse;
import com.tobi.order.processor.commons.model.Order;
import com.tobi.order.processor.reporting.service.model.OrderReport;
import com.tobi.order.processor.reporting.service.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(value = "State Store Rest API")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ApiOperation("Get all Orders Item")
    @GetMapping("/order/all")
    public CustomResponse getAllInventories() {
        return new CustomResponse(true, "", orderService.getAllOrder());
    }

    @ApiOperation("Get all Orders Item Group By Date")
    @GetMapping("/order/group/date/all")
    public CustomResponse getAllOrderGroupByDate() {
        return new CustomResponse(true, "", orderService.getAllOrderGroupByDate());
    }

    @ApiOperation("Get all Orders Item By Date Range and Group By Date")
    @GetMapping("/order/group/daterange")
    public CustomResponse getAllOrderByDateRangeGroupByDate(@RequestParam String from, @RequestParam String to) throws ParseException {
        return new CustomResponse(true, "", orderService.getAllOrderByDateRangeGroupByDate(from, to));
    }

    @ApiOperation("Get Order Item by id")
    @GetMapping("/order")
    public CustomResponse getInventoryProductName(@RequestParam String id) {
        return new CustomResponse(true, "", orderService.getOrderById(id));
    }
}
