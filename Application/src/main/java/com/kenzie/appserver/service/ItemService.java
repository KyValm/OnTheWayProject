package com.kenzie.appserver.service;


import com.kenzie.appserver.config.CacheClient;
import com.kenzie.appserver.controller.helper.HelperItemCreation;
import com.kenzie.appserver.repositories.ItemRepository;

import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ItemData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private LambdaServiceClient lambdaServiceClient;
    private CacheClient cacheClient;

    public ItemService(ItemRepository itemRepository, LambdaServiceClient lambdaServiceClient, CacheClient cacheClient) { // CacheClient cacheClient
        this.itemRepository = itemRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.cacheClient = cacheClient;
    }

    public Item getItemByID(String itemId) {
        // Check Cache if it has it
        Item cacheItem = cacheClient.get(itemId);

        if(cacheItem != null) {
            return cacheItem;
        }



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
        // Action it
        itemRepository.save(createItemRecord(item));

        // Return the parameter to let the calling program know it was successful
        return item;
    }

    public void updateItem(Item item) {
        // Clear Cache
        if(cacheClient.get(item.getItemId()) != null){
            cacheClient.evict(item.getItemId());
        }

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
        if(cacheClient.get(itemId) != null){
            cacheClient.evict(itemId);
        }

        // Action it
        itemRepository.deleteById(itemId);
    }

    public List<Item> createSampleItemList() {
        // pulling from preloaded data table csv
        return HelperItemCreation.createSampleSongList();
    }

    public List<Item> getPriorityList(){
        // throws to Lambda to analyze given list
        List<Item> priorityList = new ArrayList<>();
        List<Item> itemList = getAllInventoryItems();
        List<ItemData> itemDataListForLambda = new ArrayList<>();

        //create Data list to be processed by Lambda
        itemList.forEach(item -> itemDataListForLambda.add(itemToItemData(item)));

        List<ItemData> priorityItemDataList = lambdaServiceClient.getPriorityListFromLambda(itemDataListForLambda);
        // return priority list for ordering party getting priority

        for(ItemData itemData : priorityItemDataList) {
            Item item = itemDataToItem(itemData);
            updateItem(item);
            priorityList.add(item);
        }

        return priorityList;
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
    private Item itemDataToItem(ItemData item){
        return new Item(
                item.getItemId(),
                item.getDescription(),
                item.getCurrentQty(),
                item.getReorderQty(),
                item.getQtyTrigger(),
                item.getOrderDate());
    }

    private ItemData itemToItemData(Item item){
        return new ItemData(
                item.getItemId(),
                item.getDescription(),
                item.getCurrentQty(),
                item.getReorderQty(),
                item.getQtyTrigger(),
                item.getOrderDate());
    }
}
