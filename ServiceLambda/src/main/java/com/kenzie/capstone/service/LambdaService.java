package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ItemDao;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.ItemData;
import com.kenzie.capstone.service.model.ItemRecord;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class LambdaService {

    private ItemDao itemDao;

    @Inject
    public LambdaService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    private List<ItemData> getAllItemData() {
        List<ItemRecord> records = itemDao.getAllItemData(); // calls DynamoDB
        if (records.size() == 0 || records == null) {
            return null;
        }

        return records.stream().map(this::recordToItemData).collect(Collectors.toList());
    }


    public List<ItemData> getPriorityList() {
        List<ItemData> prioritizedList = new ArrayList<>();
        List<ItemData> allItemDataList = getAllItemData();
        HashMap<ItemData, Integer> unsortedMap = new HashMap<>(); // Integer is multiplier to represent priority
        double poAccelerator = .20; // purchase trigger for purchasing more than one lot
        // ex. for each 20% current qty is below qty trigger there will be another lot (purchase order qty) added, and
        // the "priority multiplier" in <unsortedMap> is effectively increased by one for priority sorting later,
        // to be sorted by current items decreasing at the fastest relative rate from their respective single lot triggers
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();

// this section for updating hashmap with appropriate order values

        for (ItemData itemData : allItemDataList) {
            double diff = Double.parseDouble(itemData.getQtyTrigger()) - Double.parseDouble(itemData.getCurrentQty());

            if (diff >= 0) {
                double localAccelerator = Double.parseDouble(itemData.getQtyTrigger()) * poAccelerator;
                // effectively this calculates that every <x> percentage below qty trigger will be one purchase qty

                int multiplier = (int) ((diff / localAccelerator) + 0.5); // localAccelerator as nearest int for multiplying and prioritizing
                int purchaseTotal = (int) (Double.parseDouble(itemData.getReorderQty()) * multiplier);
                itemData.setOrderDate("PO Request " + dtf.format(localDate) + " Qty: " + purchaseTotal); // for everyone to see what's in the pipeline
                unsortedMap.put(itemData, multiplier);
            }
        }
// this section for sorting unsorted HashMap and adding it to return list
        HashMap<ItemData,Integer> sortedMap =
                unsortedMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));


        for (HashMap.Entry<ItemData,Integer> mapElement : sortedMap.entrySet()) {
            prioritizedList.add(mapElement.getKey());
            Collections.reverse(prioritizedList);

            // 1. takes in all Items

            // 2. reduces list to only things that need to be ordered today

            // 3. PO qty instances (ex. inventory really low, multiple of PO qty)

            // 4. sort qty array by instances (effectively giving priority to most depleted by % rate of qty trigger)

            // 5. *update item in database upon return to ItemService
        }

        return prioritizedList;
    }

//    public ExampleData setExampleData(String data) {
//        String id = UUID.randomUUID().toString();
//        ItemRecord record = itemDao.setExampleData(id, data);
//        return new ExampleData(id, data);
//    }
//
//    public ItemData getExampleData(String id) {
//        return null;
//    }

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
