package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheClient;
import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.repositories.ItemRepository;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.capstone.service.client.LambdaServiceClient;
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

    private CacheClient cacheClient;

    @BeforeEach
    void setup() {
        repository = mock(ItemRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        itemService = new ItemService(repository, lambdaServiceClient); // cacheClient)
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
        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );
        //WHEN and THEN
        Item nullItem = itemService.getItemByID(newItem.getItemId());
        Assertions.assertNull(nullItem, "Item doesnt exist and should be null");
    }
    @Test
    void updateItem() {
        //GIVEN
        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        Item returnedItem = itemService.addInventoryItem(newItem);
        Assertions.assertNotNull(returnedItem);

        ItemRecord record = new ItemRecord();
        record.setItemId(newItem.getItemId());
        record.setDescription(newItem.getDescription());
        record.setCurrentQty(newItem.getCurrentQty());
        record.setReorderQty(newItem.getReorderQty());
        record.setQtyTrigger(newItem.getQtyTrigger());
        record.setOrderDate(newItem.getOrderDate());

        //WHEN
//        Item item = itemService.getItemByID(record.getItemId());
        record.setDescription("Description has been updated successfully");

        Assertions.assertNotEquals(record.getDescription(), newItem.getDescription());
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

        Assertions.assertEquals(2, itemList.size());
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
}
