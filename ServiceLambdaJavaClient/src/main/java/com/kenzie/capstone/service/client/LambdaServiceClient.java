package com.kenzie.capstone.service.client;

import com.amazonaws.services.dynamodbv2.document.Item;
//import com.kenzie.appserver.service.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ItemData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class LambdaServiceClient {
    //    private static final String SET_ITEM_ENDPOINT = "example";
    private ObjectMapper mapper;
//    private static final String GET_ITEM_ENDPOINT = "item/all";
    private double poAccelerator = .20; //purchase trigger for purchasing more than one lot
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
    private LocalDate localDate = LocalDate.now();

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public List<ItemData> getPriorityListLambda(List<ItemData> itemDataList) {
        List<ItemData> priorityList = new ArrayList<>();
        HashMap<ItemData,Integer> unsortedMap = new HashMap<>(); //Integer = multiplier to represent priority

// this section for updating hashmap with triggered order values for algorithm
        for(ItemData itemData : itemDataList) {
            double diff = Double.parseDouble(itemData.getQtyTrigger()) - Double.parseDouble(itemData.getCurrentQty());
            if(diff * poAccelerator >= Double.parseDouble(itemData.getQtyTrigger()))
            if(diff >= 0) {
                double localAccelerator = Double.parseDouble(itemData.getQtyTrigger()) * poAccelerator;
                // effectively this calculates that every <x> percentage below qty trigger will be one purchase qty

                int multiplier = (int)((diff / localAccelerator) + 0.5); // getMultiple as nearest int
                int purchaseTotal = (int)(Double.parseDouble(itemData.getReorderQty()) * multiplier);
                itemData.setOrderDate("PO Request " + dtf.format(localDate) + " Qty: " + purchaseTotal);
                unsortedMap.put(itemData, multiplier);
            }
        }
// this section for sorting HashMap based on priority and converting to return type for main application service

        HashMap<ItemData,Integer> sortedMap =
                unsortedMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        for (HashMap.Entry<ItemData,Integer> mapElement : sortedMap.entrySet()) {
            priorityList.add(mapElement.getKey());
        }

        Collections.reverse(priorityList);


        //do math and filter/sort work on allInventoryItems and return priorityList

        // 1. takes in all Items

        // 2. reduces list to only things that need to be ordered today

        // 3. PO qty instances (ex. inventory really low, multiple of PO qty)

        // 4. sort qty array by instances (effectively giving priority status to most depleted)

        // 5. call update item on each item

    return priorityList;
    }

//    public List<ItemData> getAllInventoryItems(){
//
//        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.getEndpoint(GET_ITEM_ENDPOINT);
//
//        List<ItemData> itemDataList = null;
//        try {
//            itemDataList = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, ItemData.class));
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return itemDataList;
//    }



//    public ItemData setItemData(String data) {
//        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.postEndpoint(SET_ITEM_ENDPOINT, data);
//        ItemData itemData;
//        try {
//            itemData = mapper.readValue(response, ItemData.class);
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return itemData;
//    }
}
