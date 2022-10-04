package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ItemDao;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.ItemData;
import com.kenzie.capstone.service.model.ItemRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {

    private ItemDao itemDao;

    @Inject
    public LambdaService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public ItemData getExampleData(String id) {
        List<ItemRecord> records = itemDao.getItemData();
        if (records.size() > 0) {
            return new ItemData(records.get(0).getItemId(),
                    records.get(0).getDescription(),
                    records.get(0).getCurrentQty(),
                    records.get(0).getReorderQty(),
                    records.get(0).getQtyTrigger(),
                    records.get(0).getOrderDate()
                    );
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ItemRecord record = itemDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }
}
