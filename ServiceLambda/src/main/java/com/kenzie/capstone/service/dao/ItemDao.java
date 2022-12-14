package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.ItemData;
import com.kenzie.capstone.service.model.ItemRecord;

import java.util.List;

public class ItemDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public ItemDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public ItemData storeItemData(ItemData itemData) {
        try {
            mapper.save(itemData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "ItemId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return itemData;
    }

    public List<ItemRecord> getAllItemData() {
        return mapper.scan(ItemRecord.class, new DynamoDBScanExpression());
    }

    public ItemRecord setExampleData(String id, String data) {
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setItemId(id);
        itemRecord.setItemData(data);

        try {
            mapper.save(itemRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return itemRecord;
    }
}
