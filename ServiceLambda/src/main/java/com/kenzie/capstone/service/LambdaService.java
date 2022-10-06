package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ItemDao;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.ItemData;
import com.kenzie.capstone.service.model.ItemRecord;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LambdaService {

    private ItemDao itemDao;

    @Inject
    public LambdaService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<ItemData> getAllItemData() {
        List<ItemRecord> records = itemDao.getAllItemData();
        if (records.size() == 0 || records == null) {
            return null;
        }

        return records.stream().map(this::recordToItemData).collect(Collectors.toList());
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ItemRecord record = itemDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }

    public ItemData getExampleData(String id) {
        return null;
    }

    private ItemData recordToItemData(ItemRecord records){
        return new ItemData(records.getItemId(),
                        records.getDescription(),
                        records.getCurrentQty(),
                        records.getReorderQty(),
                        records.getQtyTrigger(),
                        records.getOrderDate()
                        );

    }
}
