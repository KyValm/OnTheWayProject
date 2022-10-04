package com.kenzie.appserver.service;


import com.kenzie.appserver.config.CacheClient;
import com.kenzie.appserver.controller.helper.HelperItemCreation;
import com.kenzie.appserver.repositories.ItemRepository;

import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private LambdaServiceClient lambdaServiceClient;
    private CacheClient cacheClient;

@   Autowired
    public ItemService(ItemRepository itemRepository, LambdaServiceClient lambdaServiceClient) { // CacheClient cacheClient
        this.itemRepository = itemRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        //this.cacheClient = cacheClient;
    }

    public Item getItemByID(String itemId) {
        // Check Cache if it has it
        //Item cacheItem = cacheClient.get(itemId);

        //if(cacheItem != null) {
        //    return cacheItem;
        //}

        // If it's not in the cache, call it from API then add it to the cache
       Optional<ItemRecord> response = itemRepository.findById(itemId);

        if (response == null) {
            return null;
        }
        Item item = response.map(this::createItem).orElse(null);

//        for(ItemRecord entry : response){
//            if(entry.getItemId().equals(itemId)){
//                Item results = createItem(entry);
//                cacheClient.add(results.getItemId(),results);
//                return results;
//            }
//        }

        // If it gets here, that means the repo did not have the item
        return item;
    }

    public Item addInventoryItem(Item item) {
        // Action it
        itemRepository.save(createItemRecord(item));

        // Return the parameter to let the calling program know it was successful
        return item;
    }

    public void updateItem(Item item) {
        // Clear Cache
        //if(cacheClient.get(item.getItemId()) != null){
       //     cacheClient.evict(item.getItemId());
        //}

        // Action it
        if(itemRepository.existsById(item.getItemId())){
            itemRepository.save(createItemRecord(item));
        }
    }


    public List<Item> getAllInventoryItems(){
        // Action it and add it to the cache
        Iterable<ItemRecord> response = itemRepository.findAll();

        List<Item> results = new ArrayList<>();
        response.forEach(itemRecord -> results.add(createItem(itemRecord)));

        // Return it
        return results;
    }

    public void deleteByItemID(String itemId) {
        // Clear Cache
        //if(cacheClient.get(itemId) != null){
        //    cacheClient.evict(itemId);
        //}

        // Action it
        itemRepository.deleteById(itemId);
    }

    public List<Item> createSampleItemList() {
        // pulling from preloaded data table csv
        return HelperItemCreation.createSampleSongList();
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
