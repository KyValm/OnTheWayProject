package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.ItemDao;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.ItemData;
import com.kenzie.capstone.service.model.ItemRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private ItemDao itemDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.itemDao = mock(ItemDao.class);
        this.lambdaService = new LambdaService(itemDao);
    }

    @Test
    void storeItemData() {
        ArgumentCaptor<ItemRecord> itemCaptor = ArgumentCaptor.forClass(ItemRecord.class);

        //GIVEN
        String data = "some data";
        //WHEN
    }

    @Test
    void getPriorityList_withUnsortedList_happyCase(){
        // GIVEN
        List<ItemRecord> itemRecordList = new ArrayList<>();
        ItemRecord dontReorder = new ItemRecord();
        dontReorder.setItemId("143");
        dontReorder.setDescription("reorder Item");
        dontReorder.setCurrentQty("143");
        dontReorder.setReorderQty("100");
        dontReorder.setQtyTrigger("100");

        ItemRecord reorder = new ItemRecord();
        reorder.setItemId("456");
        reorder.setDescription("reorder Item");
        reorder.setCurrentQty("100");
        reorder.setReorderQty("100");
        reorder.setQtyTrigger("143");

        itemRecordList.add(dontReorder);
        itemRecordList.add(reorder);

        // WHEN

        when(itemDao.getAllItemData()).thenReturn(itemRecordList);
        List<ItemData> returnList = lambdaService.getPriorityList();

        // THEN

        Assertions.assertEquals(1, returnList.size());

    }

}