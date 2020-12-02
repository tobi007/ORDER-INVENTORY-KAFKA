package com.tobi.order.processor.inventory.managenemt.controller;

import com.tobi.order.processor.inventory.managenemt.model.Inventory;
import com.tobi.order.processor.inventory.managenemt.model.InventoryUpdateDTO;
import com.tobi.order.processor.inventory.managenemt.model.Product;
import com.tobi.order.processor.inventory.managenemt.service.InventoryService;
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
public class InventoryController {

    private final InventoryService inventoryService;

    @ApiOperation("Get all Inventories Item")
    @GetMapping("/inventory/all")
    public List<Inventory> getAllInventories() {
        return inventoryService.getAllInventory();
    }

    @ApiOperation("Get inventory Item by product name")
    @GetMapping("/inventory")
    public Inventory getInventoryProductName(@RequestParam String productName) {
        return inventoryService.getInventoryByProductName(productName);
    }

    @ApiOperation("Update inventory Quantity by product name")
    @PostMapping("/inventory/update")
    public void updateInventoryQuantityByName(@RequestBody InventoryUpdateDTO inventoryUpdateDTO) {
        inventoryService.updateInventoryQuantityByName(inventoryUpdateDTO);
    }
}
