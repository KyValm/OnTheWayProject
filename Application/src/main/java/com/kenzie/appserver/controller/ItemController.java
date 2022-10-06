package com.kenzie.appserver.controller;


import com.amazonaws.Response;
import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.controller.model.ItemResponse;
import com.kenzie.appserver.controller.model.ItemUpdateRequest;
import com.kenzie.appserver.service.ItemService;

import com.kenzie.appserver.service.model.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // Get all the songs from the service item
        List<Item> response = itemService.getAllInventoryItems();

        // Sniff check the response
        if (response == null || response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Otherwise, convert the list of Item into the ItemResponse
        List<ItemResponse> results = Optional.ofNullable(response)
                .orElse(new ArrayList<>())
                .stream()
                .map(this::convertItemToItemResponse)
                .collect(Collectors.toList());

        // Return it
        return ResponseEntity.ok(results);
    }

    @PutMapping
    public ResponseEntity<ItemResponse> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        // Create the new item to plug into the itemService
        Item itemToUpdate = createItemObject(itemUpdateRequest);

        // Update the item
        itemService.updateItem(itemToUpdate);

        // Create a itemResponse from the itemToUpdate for returning
        ItemResponse results = new ItemResponse(itemToUpdate);

        // Return it
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> addInventoryItem(@RequestBody ItemCreateRequest itemCreateRequest) {
        // Create the new item to plug into the itemService
        Item itemToAdd = createItemObject(itemCreateRequest);

        // Add it into the itemService
        itemService.addInventoryItem(itemToAdd);

        // Create the response object
        ItemResponse results = new ItemResponse(itemToAdd);

        // Return it
        return ResponseEntity.created(URI.create("/item/" + results.getItemId())).body(results);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteByItemID(@PathVariable("id") String id) {
        // Delete it in the service
        itemService.deleteByItemID(id);

        // Return it did so
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{itemID}")
    public ResponseEntity<ItemResponse> getItemByID(@PathVariable("itemID") String id) {
        // Grab it from the service
        Item item = itemService.getItemByID(id);

        // Sniff check it
        if(item == null){
            return ResponseEntity.notFound().build();
        }

        // If it's clear, then create response
        ItemResponse results = new ItemResponse(item);

        // return it
        return ResponseEntity.ok(results);
    }

    @PostMapping("/sampleItems")
    public ResponseEntity<List<ItemResponse>> createSampleItemList() {
        // Grab the list of items from the service
        List<Item> response = itemService.createSampleItemList();

        // Loop through the response and add each Item through the itemService.addInventoryItem call
        List<ItemResponse> results = new ArrayList<>();
        for(Item entry : response){
            itemService.addInventoryItem(entry);
            results.add(new ItemResponse(entry));
        }

        // Return it
        return ResponseEntity.ok(results);
    }

    @GetMapping("/reorderNeed")
    public ResponseEntity<List<ItemResponse>> getPriorityList(){
        // Grab that list of items that need to be reordered
        List<Item> response = itemService.getPriorityList();

        // Sniff check it
        if(response == null || response.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        // Turn it into a list of ItemResponses
        List<ItemResponse> results = Optional.ofNullable(response)
                .orElse(new ArrayList<>())
                .stream()
                .map(this::convertItemToItemResponse)
                .collect(Collectors.toList());
        System.out.println("The Results: " + results);
        // Return it
        return ResponseEntity.ok(results);
    }


    // #################################################################################################################
    // Helper methods go here
    // -----------------------------------------------------------------------------------------------------------------
    // This method supports optional method usage, otherwise it's not needed if optionals not used
    private ItemResponse convertItemToItemResponse(Item item) {
        return new ItemResponse(item);
    }

    // OVERLOADED METHOD - createItemObject - #1
    private Item createItemObject(ItemUpdateRequest itemRequest) {
        return new Item(
                itemRequest.getItemId(),
                itemRequest.getDescription(),
                itemRequest.getCurrentQty(),
                itemRequest.getReorderQty(),
                itemRequest.getQtyTrigger(),
                itemRequest.getOrderDate());
    }

    // OVERLOADED METHOD - createItemObject - #2
    private Item createItemObject(ItemCreateRequest itemRequest) {
        return new Item(
                itemRequest.getItemId(),
                itemRequest.getDescription(),
                itemRequest.getCurrentQty(),
                itemRequest.getReorderQty(),
                itemRequest.getQtyTrigger(),
                itemRequest.getOrderDate());
    }
}
