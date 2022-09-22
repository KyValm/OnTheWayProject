package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.kenzie.appserver.controller.model.ItemResponse;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.repositories.model.ItemRepository;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ItemData;
import io.prometheus.client.Collector;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private LambdaServiceClient lambdaServiceClient;

    public ItemService(ItemRepository itemRepository, LambdaServiceClient lambdaServiceClient) {
        this.itemRepository = itemRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Item getItemByID(String itemId) {

//        // Example getting data from the lambda
//        ItemData dataFromLambda = lambdaServiceClient.getItemData(itemId);
//
//        // Example getting data from the local repository
//        Item dataFromDynamo = itemRepository
//                .findById(itemId)
//                .map(item -> new Item(item.getItemId(),
//                    item.getDescription(),
//                    item.getCurrentQty(),
//                    item.getReorderQty(),
//                    item.getQtyTrigger(),
//                    item.getOrderDate()))
//               // .map(example -> new Example(example.getId(), example.getName()))
//                .orElse(null);
//
//        return dataFromDynamo;
        return null;
    }

    public Item addInventoryItem(String description) {
//        // Example sending data to the lambda
//        ItemData dataFromLambda = lambdaServiceClient.setItemData(description);
//
//        // Example sending data to the local repository
//        ItemRecord itemRecord = new ItemRecord();
//        itemRecord.setItemId(dataFromLambda.getItemId());
//        itemRecord.setDescription(dataFromLambda.getDescription());
//        itemRecord.setCurrentQty(dataFromLambda.getCurrentQty());
//        itemRecord.setReorderQty(dataFromLambda.getReorderQty());
//        itemRecord.setQtyTrigger(dataFromLambda.getQtyTrigger());
//        itemRecord.setOrderDate(dataFromLambda.getOrderDate());
//        itemRepository.save(itemRecord);
//
//
//        Item item = new Item(dataFromLambda.getItemId());
//        return item;
        return null;
    }

    public void updateItem(String description) {
//        // Example sending data to the lambda
//        ItemData dataFromLambda = lambdaServiceClient.setItemData(description);
//
//        // Example sending data to the local repository
//        if (itemRepository.existsById(dataFromLambda.getItemId())) {
//            ItemRecord itemRecord = new ItemRecord();
//            itemRecord.setItemId(dataFromLambda.getItemId());
//            itemRecord.setDescription(dataFromLambda.getDescription());
//            itemRecord.setCurrentQty(dataFromLambda.getCurrentQty()); // make String?
//            itemRecord.setReorderQty(dataFromLambda.getReorderQty());
//            itemRecord.setQtyTrigger(dataFromLambda.getQtyTrigger());
//            itemRecord.setOrderDate(dataFromLambda.getOrderDate());
//            itemRepository.save(itemRecord);
//        }
    }


    public List<Item> getAllInventoryItems(){
//        List<Item> itemsFromDynamo = itemRepository.findAll().
//
//                forEach(itemRecord -> {item -> new Item(itemRecord.getItemId(),
//                   itemRecord.getDescription(),
//                   itemRecord.getCurrentQty(),
//                   itemRecord.getReorderQty(),
//                   itemRecord.getQtyTrigger(),
//                   itemRecord.getOrderDate()))
//                   .itemsFromDynamo
//
//                });

//        return itemsFromDynamo;
        return null;
    }

    public void deleteByItemID(String itemId) {

    }

    public List<Item> createSampleItemList() {

        return null;
    }

}
