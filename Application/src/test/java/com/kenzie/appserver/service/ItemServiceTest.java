package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kenzie.appserver.config.CacheClient;
import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.repositories.ItemRepository;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ItemData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    private ItemRepository repository;
    private ItemService itemService;
    private LambdaServiceClient lambdaServiceClient;
    private DynamoDBMapper mapper;

    private CacheClient cacheClient;

    @BeforeEach
    void setup() {
        repository = mock(ItemRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cacheClient = mock(CacheClient.class);
        mapper = mock(DynamoDBMapper.class);
        itemService = new ItemService(repository, lambdaServiceClient, cacheClient, mapper);
    }
    /** ------------------------------------------------------------------------
     *  itemService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void getItemById() {
        // GIVEN

        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        ArgumentCaptor<ItemRecord> argumentCaptor = ArgumentCaptor.forClass(ItemRecord.class);

        // WHEN
        Item returnedItem = itemService.addInventoryItem(newItem);

        // THEN
        Assertions.assertNotNull(returnedItem, "The object is returned");

        verify(repository).save(argumentCaptor.capture());

        ItemRecord record = argumentCaptor.getValue();

        Assertions.assertNotNull(record, "The Item record is returned");
        Assertions.assertEquals(newItem.getItemId(), record.getItemId() , "The id matches");
        Assertions.assertEquals(newItem.getDescription(), record.getDescription());
        Assertions.assertEquals(newItem.getCurrentQty(), record.getCurrentQty());
        Assertions.assertEquals(newItem.getReorderQty(), record.getReorderQty());
        Assertions.assertEquals(newItem.getQtyTrigger(), record.getQtyTrigger());
        Assertions.assertEquals(newItem.getOrderDate(), record.getOrderDate());
    }
    @Test
    void findItem_invalidId_isNull() {
        //GIVEN
//        Item newItem = new Item(
//                randomUUID().toString(),
//                "test item",
//                "1",
//                "1",
//                "0",
//                LocalDateTime.now().toString()
//        );

        //WHEN and THEN
        Item nullItem = itemService.getItemByID(randomUUID().toString());
        Assertions.assertNull(nullItem, "Item doesnt exist and should be null");
    }

    @Test
    void updateItem() {
        //GIVEN
        String itemId = randomUUID().toString();

        Item newItem = new Item(itemId,
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        ItemRecord newRecord = new ItemRecord();
        newRecord.setItemId(newItem.getItemId());
        newRecord.setDescription(newItem.getDescription());
        newRecord.setCurrentQty(newItem.getCurrentQty());
        newRecord.setReorderQty(newItem.getReorderQty());
        newRecord.setQtyTrigger(newItem.getQtyTrigger());
        newRecord.setOrderDate(newItem.getOrderDate());

        when(repository.findById(itemId)).thenReturn(Optional.of(newRecord));

        ArgumentCaptor<ItemRecord> itemRecordArgumentCaptor = ArgumentCaptor.forClass(ItemRecord.class);
        //WHEN

        newItem.setDescription("Description has been updated successfully");
        itemService.updateItem(newItem);

        //THEN
        verify(repository).save(itemRecordArgumentCaptor.capture());

        ItemRecord record = itemRecordArgumentCaptor.getValue();

        //THEN
        Assertions.assertNotNull(record, "Item Record is returned.");
        Assertions.assertEquals("Description has been updated successfully", record.getDescription());
    }
    @Test
    void getAllItems() {
        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        Item newItem2 = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        Item returnedItem = itemService.addInventoryItem(newItem);
        Item returnedItem2 = itemService.addInventoryItem(newItem2);

        ItemRecord record = new ItemRecord();
        record.setItemId(newItem.getItemId());
        record.setDescription(newItem.getDescription());
        record.setCurrentQty(newItem.getCurrentQty());
        record.setReorderQty(newItem.getReorderQty());
        record.setQtyTrigger(newItem.getQtyTrigger());
        record.setOrderDate(newItem.getOrderDate());

        ItemRecord record2 = new ItemRecord();
        record.setItemId(newItem2.getItemId());
        record.setDescription(newItem2.getDescription());
        record.setCurrentQty(newItem2.getCurrentQty());
        record.setReorderQty(newItem2.getReorderQty());
        record.setQtyTrigger(newItem2.getQtyTrigger());
        record.setOrderDate(newItem2.getOrderDate());

        List<ItemRecord> itemList = new ArrayList<>();
        itemList.add(record);
        itemList.add(record2);
        when(repository.findAll()).thenReturn(itemList);

        List<Item> itemServiceList = itemService.getAllInventoryItems();

        Assertions.assertNotNull(itemServiceList, "The Item List is returned");
        Assertions.assertEquals(2, itemServiceList.size());
    }
    @Test
    void deleteItem() {
        //GIVEN
        String messageId = randomUUID().toString();

        //WHEN
        itemService.deleteByItemID(messageId);
        //THEN
        verify(repository).deleteById(messageId);

    }

    @Test
    void getOnlyItemsOfCategory(){
        Item newItem = new Item(
                "300-3000",
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        Item newItem2 = new Item(
                "190-2440",
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        Item returnedItem = itemService.addInventoryItem(newItem);
        Item returnedItem2 = itemService.addInventoryItem(newItem2);

        ItemRecord record = new ItemRecord();
        record.setItemId(newItem.getItemId());
        record.setDescription(newItem.getDescription());
        record.setCurrentQty(newItem.getCurrentQty());
        record.setReorderQty(newItem.getReorderQty());
        record.setQtyTrigger(newItem.getQtyTrigger());
        record.setOrderDate(newItem.getOrderDate());

        ItemRecord record2 = new ItemRecord();
        record.setItemId(newItem2.getItemId());
        record.setDescription(newItem2.getDescription());
        record.setCurrentQty(newItem2.getCurrentQty());
        record.setReorderQty(newItem2.getReorderQty());
        record.setQtyTrigger(newItem2.getQtyTrigger());
        record.setOrderDate(newItem2.getOrderDate());

        List<ItemRecord> itemList = new ArrayList<>();
        itemList.add(record);
        itemList.add(record2);

        when(repository.findAll()).thenReturn(itemList);

        List<Item> itemsOfCategory = itemService.getItemsOfCategory("300");

        Assertions.assertNotNull(itemsOfCategory, "The Item List is returned");
        Assertions.assertEquals(1, itemsOfCategory.size());
    }


    @Test
    void getPriorityList(){
        Item newItem = new Item(
                "300-3000",
                "test item",
                "100",
                "100",
                "101",
                LocalDateTime.now().toString()
        );

        Item newItem2 = new Item(
                "190-2440",
                "test item",
                "10",
                "100",
                "101",
                LocalDateTime.now().toString()
        );

        ItemData record = new ItemData();
        record.setItemId(newItem.getItemId());
        record.setDescription(newItem.getDescription());
        record.setCurrentQty(newItem.getCurrentQty());
        record.setReorderQty(newItem.getReorderQty());
        record.setQtyTrigger(newItem.getQtyTrigger());
        record.setOrderDate(newItem.getOrderDate());

        ItemData record2 = new ItemData();
        record.setItemId(newItem2.getItemId());
        record.setDescription(newItem2.getDescription());
        record.setCurrentQty(newItem2.getCurrentQty());
        record.setReorderQty(newItem2.getReorderQty());
        record.setQtyTrigger(newItem2.getQtyTrigger());
        record.setOrderDate(newItem2.getOrderDate());

        List<ItemData> itemList = new ArrayList<>();
        itemList.add(record);
        itemList.add(record2);

        when(lambdaServiceClient.getPriorityList()).thenReturn(itemList);

        List<Item> itemsOfPriority = itemService.getPriorityList();

        Assertions.assertNotNull(itemsOfPriority, "The Item List is returned");
        Assertions.assertEquals(record.getItemId(), itemsOfPriority.get(0).getItemId());
        Assertions.assertEquals(record2.getItemId(), itemsOfPriority.get(1).getItemId());
    }

    @Test
    void addItem() {
        //GIVEN
        String itemId = randomUUID().toString();

        Item newItem = new Item(itemId,
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        ItemRecord newRecord = new ItemRecord();
        newRecord.setItemId(newItem.getItemId());
        newRecord.setDescription(newItem.getDescription());
        newRecord.setCurrentQty(newItem.getCurrentQty());
        newRecord.setReorderQty(newItem.getReorderQty());
        newRecord.setQtyTrigger(newItem.getQtyTrigger());
        newRecord.setOrderDate(newItem.getOrderDate());

        when(repository.findById(itemId)).thenReturn(Optional.of(newRecord));

        ArgumentCaptor<ItemRecord> itemRecordArgumentCaptor = ArgumentCaptor.forClass(ItemRecord.class);
        //WHEN

        newItem.setDescription("Description has been updated successfully");
        itemService.addInventoryItem(newItem);

        //THEN
        verify(repository).save(itemRecordArgumentCaptor.capture());

        ItemRecord record = itemRecordArgumentCaptor.getValue();

        //THEN

        Assertions.assertNotNull(record, "The Item record is returned");
//        Assertions.assertNotNull(record.getItemId(), "The customer id exists");
//        Assertions.assertEquals(record.getName(), customerName, "The customer name matches");
//        Assertions.assertNotNull(record.getDateCreated(), "The customer date exists");
//        Assertions.assertNull(record.getReferrerId(), "The referrerId is null");
    }


//    getItemByID               X
//    getPriorityList           X
//    getAllInventoryItems      X
//    getItemsOfCategory        X
//    addInventoryItem
//    updateItem                X
//    deleteByItemID
//    createSampleItemList
//    createItem
//    createItemRecord
//    itemDataToItem
//    pullFromAWS



//    @Test
//    void getItemByID(String itemId)
//
//    @Test
//    void getAllInventoryItems()
//
//    @Test
//    void addInventoryItem(Item item)
//
//    @Test
//    void deleteByItemID(String itemId)
//
//    @Test
//    void createSampleItemList()
//
//    @Test
//    void createItem(ItemRecord item)
//
//    @Test
//    void itemDataToItem(ItemData item)


//
//    @Test
//    void pullFromAWS()


}
