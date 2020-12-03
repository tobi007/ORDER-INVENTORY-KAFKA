package com.tobi.order.processor.inventory.managenemt.controller;

import com.tobi.order.processor.commons.model.CustomResponse;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import com.tobi.order.processor.inventory.managenemt.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "State Store Rest API")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation("Create a new Product Item")
    @PostMapping(value = "/product/create")
    public CustomResponse createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return new CustomResponse(true, "", null);
    }

    @ApiOperation("Update a Product Item")
    @PostMapping(value = "/product/update")
    public CustomResponse updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return new CustomResponse(true, "", null);
    }

    @ApiOperation("Get all Products Item")
    @GetMapping("/product/all")
    public CustomResponse getAllProducts() {
        return new CustomResponse(true, "",         productService.getAllProduct());
    }

    @ApiOperation("Get all Available Products Item")
    @GetMapping("/product/all/available")
    public CustomResponse getAllAvailableProducts() {
        return new CustomResponse(true, "", productService.getAllAvailableProducts());
    }

    @ApiOperation("Get Product Item by name")
    @GetMapping("/product")
    public CustomResponse getProductByName(@RequestParam String name) {
        return new CustomResponse(true, "", productService.getProductByName(name));
    }
}
