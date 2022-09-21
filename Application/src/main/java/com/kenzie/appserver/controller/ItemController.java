package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.controller.model.ItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {
    // Does the cache come here?
    // How does lamdba work?

    public ItemController(String REPLACE_ME){
        // we need to input a itemService object class here

    }

    @PostMapping
    public ResponseEntity<ItemResponse> addInventoryItem(@RequestBody ItemCreateRequest itemCreateRequest){

        return null;
    }

    @GetMapping("/{itemID}")
    public ResponseEntity<ItemResponse> getItemByID(@PathVariable("itemID") String id){
        return null;
    }
}
