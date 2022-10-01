package com.kenzie.appserver.service;


import com.kenzie.appserver.repositories.ItemRepository;

import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private LambdaServiceClient lambdaServiceClient;

    public ItemService(ItemRepository itemRepository, LambdaServiceClient lambdaServiceClient) {
        this.itemRepository = itemRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Item getItemByID(String itemId) {
        // Check Cache if it has it
        // TODO - Implement me!

        // If it's not in the cache, call it from API then add it to the cache
        Iterable<ItemRecord> response = itemRepository.findAll();

        for(ItemRecord entry : response){
            if(entry.getItemId().equals(itemId)){
                return createItem(entry);
            }
        }

        // If it gets here, that means the repo did not have the item
        return null;
    }

    public Item addInventoryItem(Item item) {
        // Clear Cache
        // TODO - Implement me!

        // Action it
        itemRepository.save(createItemRecord(item));

        // Return the parameter to let the calling program know it was successful
        return item;
    }

    public void updateItem(Item item) {
        // Clear Cache
        // TODO - Implement me!

        // Action it
        if(itemRepository.existsById(item.getItemId())){
            itemRepository.save(createItemRecord(item));
        }
    }


    public List<Item> getAllInventoryItems(){
        // Check Cache if it has it
        // TODO - Implement me!

        // Action it and add it to the cache
        Iterable<ItemRecord> response = itemRepository.findAll();

        List<Item> results = new ArrayList<>();
        response.forEach(itemRecord -> results.add(createItem(itemRecord)));

        // Return it
        return results;
    }

    public void deleteByItemID(String itemId) {
        // Clear Cache
        // TODO - Implement me!

        // Action it
        itemRepository.deleteById(itemId);
    }

    public List<Item> createSampleItemList() {
        // pulling from preloaded data table csv

        return null;
    }

    public List<Item>  getPriorityList(){

        //call get all items // throws to Lambda to analyze gives list

        // 1. takes in all Items

        // 2. reduces list to only things that need to be ordered today

        // 3. PO qty instances (ex. inventory really load, multiple of PO qty

        // 4. sort qty array by instances (effectively giving priority status to most depleted)

        // 5. call update item on each item

        // return everything from step 4

        return null;
    }

    // Helper Methods ##################################################################################################
    private Item createItem(ItemRecord item){
        return new Item(
                item.getItemId(),
                item.getDescription(),
                item.getCurrentQty(),
                item.getReorderQty(),
                item.getQtyTrigger(),
                item.getOrderDate());
    }
    private ItemRecord createItemRecord(Item item){
        ItemRecord results = new ItemRecord();
        results.setItemId(item.getItemId());
        results.setDescription(item.getDescription());
        results.setCurrentQty(String.valueOf(item.getCurrentQty()));
        results.setReorderQty(String.valueOf(item.getReorderQty()));
        results.setQtyTrigger(String.valueOf(item.getQtyTrigger()));
        results.setOrderDate(item.getOrderDate());
        return results;

    }
}
