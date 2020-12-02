package com.tobi.order.processor.inventory.managenemt.controller;

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
    public void createProduct(@RequestBody Product product) {
        productService.createProduct(product);
    }

    @ApiOperation("Update a Product Item")
    @PostMapping(value = "/product/update")
    public void updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
    }

    @ApiOperation("Get all Products Item")
    @GetMapping("/product/all")
    public List<Product> getAllProducts() {
        return productService.getAllProduct();
    }

    @ApiOperation("Get all Available Products Item")
    @GetMapping("/product/all/available")
    public List<Product> getAllAvailableProducts() {
        return productService.getAllAvailableProducts();
    }

    @ApiOperation("Get Product Item by name")
    @GetMapping("/product")
    public Product getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }
}
