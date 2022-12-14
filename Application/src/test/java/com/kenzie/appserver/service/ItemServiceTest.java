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

        ArrayList<ItemRecord> listRecords = new ArrayList<>();
        listRecords.add(newItem);
        Iterable<ItemRecord> listReturned = new ArrayList<>(listRecords);

        when(repository.findAll()).thenReturn(listReturned);

        // WHEN
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
    void getItemById_inputInValidId_ReturnsNull() {
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

        ItemRecord fakeItem = new ItemRecord(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        // WHEN
        ArrayList<ItemRecord> listRecords = new ArrayList<>();
        listRecords.add(fakeItem);
        Iterable<ItemRecord> listReturned = new ArrayList<>(listRecords);

        when(repository.findAll()).thenReturn(listReturned);
        Item returnedItem = itemService.getItemByID(id);

        // THEN
        Assertions.assertNull(returnedItem, "The object is not null");
    }

    @Test
    void getItemById_withLoadedCache_returnCacheItem() {
        // GIVEN
        Item newItem = new Item(
                "456",
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );
        // WHEN

        when(cacheClient.get(newItem.getItemId())).thenReturn(newItem);
        Item cacheItem = itemService.getItemByID(newItem.getItemId());

        // THEN
        Assertions.assertEquals(cacheItem.getItemId(),newItem.getItemId());
    }

    @Test
    void getItemById_itemDoesntExist_returnsNull() {
        //GIVEN
        Iterable<ItemRecord> listReturned = new ArrayList<>();
        when(repository.findAll()).thenReturn(listReturned);

        //WHEN
        Item nullItem = itemService.getItemByID(randomUUID().toString());

        //THEN
        Assertions.assertNull(nullItem, "Item doesn't exist and should be null");
    }

    @Test
    void findItem_invalidId_isNull() {
        //GIVEN and WHEN
        Item nullItem = itemService.getItemByID(randomUUID().toString());

        //THEN
        Assertions.assertNull(nullItem, "Item doesn't exist and should be null");
    }

    @Test
    void updateItem_updateExistingItem_happyCase() {
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

        newItem.setDescription("Description has been updated successfully");
        when(repository.findById(itemId)).thenReturn(Optional.of(newRecord));

        //WHEN
        itemService.updateItem(newItem);

        //THEN
        Assertions.assertNotNull(newRecord, "Item Record is not returned.");
        Assertions.assertEquals("test item", newRecord.getDescription());
    }

    @Test
    void getAllInventoryItems_validInventoryItems_happyCase() {
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

        // WHEN
        when(repository.findAll()).thenReturn(itemList);

        List<Item> itemServiceList = itemService.getAllInventoryItems();
        // THEN
        Assertions.assertNotNull(itemServiceList, "The Item List is returned");
        Assertions.assertEquals(2, itemServiceList.size());
    }

    @Test
    void deleteByItemID_validId_happyCase() {
        //GIVEN
        Item newItem = new Item(
                "300-3000",
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        itemService.addInventoryItem(newItem);

        when(repository.findById(newItem.getItemId())).thenReturn(null);

        //WHEN
        itemService.deleteByItemID(newItem.getItemId());

        //THEN
        verify(repository).deleteById(newItem.getItemId());

    }

    @Test
    void getItemsOfCategory_ValidItems_ReturnsOnlyItemsOfCategory(){
        //GIVEN
        ItemRecord newItem = new ItemRecord(
                "300-3000",
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        ItemRecord newItem2 = new ItemRecord(
                "190-2440",
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );


        List<ItemRecord> responseList = new ArrayList<>();
        responseList.add(newItem);
        responseList.add(newItem2);

        when(repository.findAll()).thenReturn(responseList);

        //WHEN
        List<Item> itemsOfCategory = itemService.getItemsOfCategory("300");

        //THEN
        Assertions.assertNotNull(itemsOfCategory, "The Item List is returned");
        Assertions.assertEquals(1, itemsOfCategory.size());
        Assertions.assertEquals("300-3000", itemsOfCategory.get(0).getItemId());
    }


    @Test
    void getPriorityList_analyzesValidListItems_happyCase(){
        //GIVEN
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

        //WHEN
        List<Item> itemsOfPriority = itemService.getPriorityList();

        //THEN
        Assertions.assertNotNull(itemsOfPriority, "The Item List is returned");
        Assertions.assertEquals(record.getItemId(), itemsOfPriority.get(0).getItemId());
        Assertions.assertEquals(record2.getItemId(), itemsOfPriority.get(1).getItemId());
    }

    @Test
    void addInventoryItem_addValidItem_happyCase() {
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


    // HELPER METHODS --------------------------------------------------------------------------------------------------

    private Item createItem(ItemRecord item) {
        return new Item(
                item.getItemId(),
                item.getDescription(),
                item.getCurrentQty(),
                item.getReorderQty(),
                item.getQtyTrigger(),
                item.getOrderDate());
    }



}
