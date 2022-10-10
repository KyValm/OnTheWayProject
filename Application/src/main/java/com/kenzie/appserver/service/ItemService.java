package com.kenzie.appserver.service;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
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
import java.util.Optional;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private LambdaServiceClient lambdaServiceClient;
    private CacheClient cacheClient;
    private DynamoDBMapper mapper;

    public boolean hasPulledFromAWS; // may want to make this private, though it will break a test

    public ItemService(ItemRepository itemRepository, LambdaServiceClient lambdaServiceClient, CacheClient cacheClient, DynamoDBMapper mapper) { // CacheClient cacheClient
        this.itemRepository = itemRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.cacheClient = cacheClient;
        this.mapper = mapper;
        this.hasPulledFromAWS = false;
    }
    // Frontend #1 -----------------------------------------------------------------------------------------------------
    public Item getItemByID(String itemId) {
        // Ensure that the AWS table has been pulled
        if (!hasPulledFromAWS) {
            pullFromAWS();
        }

        // Check Cache if it has it
        Item cacheItem = cacheClient.get(itemId);
        if (cacheItem != null) {
            return cacheItem;
        }

        // If it's not in the cache, call it from API then add it to the cache
        Iterable<ItemRecord> response = itemRepository.findAll();

        if (response == null) {
            return null;
        }

        for (ItemRecord entry : response) {
            if (entry.getItemId().equals(itemId)) {
                Item answer = createItem(entry);
                cacheClient.add(answer.getItemId(), answer);
                return answer;
            }
        }

        // If it gets here, that means the repo did not have the item
        return null;
    }
    // Frontend #2  ----------------------------------------------------------------------------------------------------
    public List<Item> getPriorityList() {
        // throws to Lambda to analyze given list
        List<ItemData> priorityItemDataList = lambdaServiceClient.getPriorityList();

        // Process it to become item objects, so we can return it + update the local table with such objects.
        // This ensures the data stays the same from AWS to this repo in case AWS changes.
        List<Item> priorityList = new ArrayList<>();
        for (ItemData itemData : priorityItemDataList) {
            Item item = itemDataToItem(itemData);
            updateItem(item);
            priorityList.add(item);
        }

        return priorityList;
    }
    // Frontend #3  ----------------------------------------------------------------------------------------------------
    public List<Item> getAllInventoryItems() {
        // Ensure that the AWS table has been pulled
        if (!hasPulledFromAWS) {
            pullFromAWS();
        }

        // Pull it from the local repo
        Iterable<ItemRecord> response = itemRepository.findAll();

        List<Item> results = new ArrayList<>();
        response.forEach(itemRecord -> results.add(createItem(itemRecord)));

        // Return it
        return results;
    }

    // Frontend #4 -----------------------------------------------------------------------------------------------------
    public List<Item> getItemsOfCategory(String filter){
        // Ensure that the AWS table has been pulled
        if (!hasPulledFromAWS) {
            pullFromAWS();
        }

        // Get all Inventory times
        List<Item> response = getAllInventoryItems();

        // Filter it out so that we only get the parts that start with the entry
        List<Item> results = new ArrayList<>();
        for(Item entry : response){
            String substring = entry.getItemId().substring(0,3);
            if(substring.equals(filter)){
                results.add(entry);
            }
        }

        // return it
        return results;
    }
    // Methods that are not going to be built because it's only local repo based. May be updated for AWS access --------
    public Item addInventoryItem(Item item) {
        // Action it
        itemRepository.save(createItemRecord(item));

        // Return the parameter to let the calling program know it was successful
        return item;
    }
    public void updateItem(Item item) {
        // Clear Cache
        if (cacheClient.get(item.getItemId()) != null) {
            cacheClient.evict(item.getItemId());
        }
        Optional<ItemRecord> recordExists = itemRepository.findById(item.getItemId());
        // Action it
        if (recordExists.isPresent()) {
            ItemRecord record = createItemRecord(item);
            itemRepository.save(record);
        }
    }
    public void deleteByItemID(String itemId) {
        // Clear Cache
        if (cacheClient.get(itemId) != null) {
            cacheClient.evict(itemId);
        }

        // Action it
        itemRepository.deleteById(itemId);
    }

    // This method isn't really needed anymore since we're pulling from the AWS table
    public List<Item> createSampleItemList() {
        // pulling from preloaded data table csv
        return HelperItemCreation.createSampleSongList();
    }

    // Helper Methods ##################################################################################################
    Item createItem(ItemRecord item) {
        return new Item(
                item.getItemId(),
                item.getDescription(),
                item.getCurrentQty(),
                item.getReorderQty(),
                item.getQtyTrigger(),
                item.getOrderDate());
    }

    private ItemRecord createItemRecord(Item item) {
        ItemRecord results = new ItemRecord();
        results.setItemId(item.getItemId());
        results.setDescription(item.getDescription());
        results.setCurrentQty(String.valueOf(item.getCurrentQty()));
        results.setReorderQty(String.valueOf(item.getReorderQty()));
        results.setQtyTrigger(String.valueOf(item.getQtyTrigger()));
        results.setOrderDate(item.getOrderDate());
        return results;

    }

    Item itemDataToItem(ItemData item) {
        return new Item(
                item.getItemId(),
                item.getDescription(),
                item.getCurrentQty(),
                item.getReorderQty(),
                item.getQtyTrigger(),
                item.getOrderDate());
    }

    public void pullFromAWS() { // "Day of" inventory pull
        // Grab all the things from the AWS cloud
        List<ItemRecord> response = mapper.scan(ItemRecord.class, new DynamoDBScanExpression());
        response.stream().map(this::createItem).forEach(this::addInventoryItem);

        // Local Songs
        // List<Item> response = createSampleItemList();
        //response.stream().forEach(this::addInventoryItem);

        // Update the boolean that we have in fact pulled from AWS
        hasPulledFromAWS = true;
    }
}
