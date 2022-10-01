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
        itemService = new ItemService(repository, lambdaServiceClient, cacheClient);
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
        ItemRecord record = new ItemRecord();
        record.setItemId(randomUUID().toString());
        record.setDescription("not updated");
        record.setCurrentQty("2");
        record.setReorderQty("1");
        record.setQtyTrigger("1");
        record.setOrderDate(LocalDateTime.now().toString());

        //WHEN
        when(repository.findById(record.getItemId())).thenReturn(Optional.of(record));
        when(repository.existsById(record.getItemId())).thenReturn(true);

        Item item = itemService.getItemByID(record.getItemId());
        item.setDescription("Description has been updated successfully");
        itemService.updateItem(item);

        ItemRecord updatedRecord = new ItemRecord();
        record.setItemId(item.getItemId());
        record.setDescription(item.getDescription());
        record.setCurrentQty(item.getCurrentQty());
        record.setReorderQty(item.getReorderQty());
        record.setQtyTrigger(item.getQtyTrigger());
        record.setOrderDate(item.getOrderDate());

        when(repository.findById(updatedRecord.getItemId())).thenReturn(Optional.of(updatedRecord));

        Assertions.assertNotEquals(record.getDescription(), updatedRecord.getDescription());
        Assertions.assertEquals("Description has been updated successfully", updatedRecord.getDescription());
    }
}
