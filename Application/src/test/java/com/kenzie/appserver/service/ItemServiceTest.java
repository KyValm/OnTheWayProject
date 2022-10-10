package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
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
    void getItemById_InputValidItem_ReturnsItem() {
        // GIVEN

        String id = randomUUID().toString();

        ItemRecord newItem = new ItemRecord(
                id,
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        itemService.createItem(newItem);


//        PaginatedScanList<ItemRecord> listItemRecords = new PaginatedScanList;

//        when(mapper.scan(ItemRecord.class, new DynamoDBScanExpression())).thenReturn(listItemRecords);
//

        // WHEN
        ArrayList<ItemRecord> listRecords = new ArrayList<>();
        listRecords.add(newItem);
        Iterable<ItemRecord> listReturned = new ArrayList<>(listRecords);

        when(repository.findAll()).thenReturn(listReturned);
        Item returnedItem = itemService.getItemByID(id);
        // THEN
        Assertions.assertNotNull(returnedItem, "The object is not returned");


        Assertions.assertNotNull(returnedItem, "The Item record is returned");
        Assertions.assertEquals(newItem.getItemId(), returnedItem.getItemId() , "The id matches");
        Assertions.assertEquals(newItem.getDescription(), returnedItem.getDescription());
        Assertions.assertEquals(newItem.getCurrentQty(), returnedItem.getCurrentQty());
        Assertions.assertEquals(newItem.getReorderQty(), returnedItem.getReorderQty());
        Assertions.assertEquals(newItem.getQtyTrigger(), returnedItem.getQtyTrigger());
        Assertions.assertEquals(newItem.getOrderDate(), returnedItem.getOrderDate());
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

//        Item returnedItem = itemService.addInventoryItem(newItem);
//        Item returnedItem2 = itemService.addInventoryItem(newItem2);

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

//        Item returnedItem = itemService.addInventoryItem(newItem);
//        Item returnedItem2 = itemService.addInventoryItem(newItem2);
//
//        ItemRecord record = new ItemRecord();
//        record.setItemId(newItem.getItemId());
//        record.setDescription(newItem.getDescription());
//        record.setCurrentQty(newItem.getCurrentQty());
//        record.setReorderQty(newItem.getReorderQty());
//        record.setQtyTrigger(newItem.getQtyTrigger());
//        record.setOrderDate(newItem.getOrderDate());
//
//        ItemRecord record2 = new ItemRecord();
//        record.setItemId(newItem2.getItemId());
//        record.setDescription(newItem2.getDescription());
//        record.setCurrentQty(newItem2.getCurrentQty());
//        record.setReorderQty(newItem2.getReorderQty());
//        record.setQtyTrigger(newItem2.getQtyTrigger());
//        record.setOrderDate(newItem2.getOrderDate());
//
//        List<ItemRecord> itemList = new ArrayList<>();
//        itemList.add(record);
//        itemList.add(record2);

        List<Item> responseList = new ArrayList<>();
        responseList.add(newItem);
        responseList.add(newItem2);

//        when(repository.findAll()).thenReturn(itemList);
        when(itemService.getAllInventoryItems()).thenReturn(responseList);

        //WHEN

        List<Item> itemsOfCategory = itemService.getItemsOfCategory("300");

        //THEN

        Assertions.assertNotNull(itemsOfCategory, "The Item List is returned");
        Assertions.assertEquals(1, itemsOfCategory.size());
        Assertions.assertEquals("300-3000", itemsOfCategory.get(0).getItemId());
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

        itemService.addInventoryItem(newItem);

        //THEN
        verify(repository).save(itemRecordArgumentCaptor.capture());

        ItemRecord record = itemRecordArgumentCaptor.getValue();

        //THEN

        Assertions.assertNotNull(record, "The Item record is returned");
        Assertions.assertNotNull(record.getItemId(), "The customer id exists");
        Assertions.assertNotNull(record.getDescription(), "The description exists");
        Assertions.assertNotNull(record.getOrderDate(), "The order date exists");
        Assertions.assertNotNull(record.getCurrentQty(), "The current qty exists");
        Assertions.assertNotNull(record.getQtyTrigger(), "The qty trigger exists");
        Assertions.assertNotNull(record.getReorderQty(), "The reorder qty exists");
    }

    @Test
    void itemDataConvertsToItem() {
        //GIVEN
        String itemId = randomUUID().toString();

        ItemData itemData = new ItemData(itemId,
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        //WHEN AND THEN
        Item item = itemService.itemDataToItem(itemData);

        Assertions.assertNotNull(item, "The Item is returned");
        Assertions.assertEquals(item.getItemId(), itemData.getItemId());
        Assertions.assertEquals(item.getDescription(), itemData.getDescription());
        Assertions.assertEquals(item.getOrderDate(), itemData.getOrderDate());
        Assertions.assertEquals(item.getCurrentQty(), itemData.getCurrentQty());
        Assertions.assertEquals(item.getQtyTrigger(), itemData.getQtyTrigger());
        Assertions.assertEquals(item.getReorderQty(), itemData.getReorderQty());
    }

    @Test
    void createItemFromRecord() {
        //GIVEN
        String itemId = randomUUID().toString();

        ItemRecord itemRecord = new ItemRecord(
                itemId,
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        //WHEN AND THEN
        Item item = itemService.createItem(itemRecord);

        Assertions.assertNotNull(item, "The Item is returned");
        Assertions.assertEquals(item.getItemId(), itemRecord.getItemId());
        Assertions.assertEquals(item.getDescription(), itemRecord.getDescription());
        Assertions.assertEquals(item.getOrderDate(), itemRecord.getOrderDate());
        Assertions.assertEquals(item.getCurrentQty(), itemRecord.getCurrentQty());
        Assertions.assertEquals(item.getQtyTrigger(), itemRecord.getQtyTrigger());
        Assertions.assertEquals(item.getReorderQty(), itemRecord.getReorderQty());
    }

    @Test
    void pullFromAWS(){
        // GIVEN

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

        List<Item> createList = new ArrayList<>();
        createList.add(newItem);
        createList.add(newItem2);

        when(itemService.createSampleItemList()).thenReturn(createList);

        // WHEN

        itemService.pullFromAWS();

        // THEN

        Assertions.assertTrue(itemService.hasPulledFromAWS);

    }


}
