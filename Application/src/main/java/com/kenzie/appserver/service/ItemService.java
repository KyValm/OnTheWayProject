package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.repositories.model.ItemRepository;
import com.kenzie.appserver.service.model.Example;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private LambdaServiceClient lambdaServiceClient;

    public ItemService(ItemRepository itemRepository, LambdaServiceClient lambdaServiceClient) {
        this.itemRepository = itemRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Example findById(String id) {

        // Example getting data from the lambda
        ExampleData dataFromLambda = lambdaServiceClient.getExampleData(id);

        // Example getting data from the local repository
        Example dataFromDynamo = itemRepository
                .findById(id)
                .map(example -> new Example(example.getId(), example.getName()))
                .orElse(null);

        return dataFromDynamo;
    }

    public Example addNewExample(String name) {
        // Example sending data to the lambda
        ExampleData dataFromLambda = lambdaServiceClient.setExampleData(name);

        // Example sending data to the local repository
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setId(dataFromLambda.getId());
        itemRecord.setName(dataFromLambda.getData());
        itemRepository.save(itemRecord);

        Item item = new Example(dataFromLambda.getId(), name);
        return item;
    }
}
