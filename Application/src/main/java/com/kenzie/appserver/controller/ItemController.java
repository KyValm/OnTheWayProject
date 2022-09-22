package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.controller.model.ItemResponse;
import com.kenzie.appserver.controller.model.ItemUpdateRequest;
import com.kenzie.appserver.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    private ItemService itemService;

    public ItemController(ItemService itemService) {
        // we need to input a itemService object class here
        this.itemService = itemService;
    }

    // All the endpoint methods here
    @GetMapping("/all")
    public ResponseEntity<List<ItemResponse>> getAllInventoryItems() {
        return null;
    }

    @PutMapping
    public ResponseEntity<ItemResponse> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest){
        return null;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> addInventoryItem(@RequestBody ItemCreateRequest itemCreateRequest) {
        return null;
    }

    @DeleteMapping("/{itemName}")
    public ResponseEntity deleteByItemID(@PathVariable("itemName") String itemName){
        return null;
    }

    @GetMapping("/{itemID}")
    public ResponseEntity<ItemResponse> getItemByID(@PathVariable("itemID") String id) {
        return null;
    }

    @PostMapping("/sampleItems")
    public ResponseEntity<List<ItemResponse>> createSampleItemList(){
        return null;
    }
}
