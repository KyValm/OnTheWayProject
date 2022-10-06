package com.kenzie.capstone.service.client;

import com.amazonaws.services.dynamodbv2.document.Item;
//import com.kenzie.appserver.service.model.Item;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ItemData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class LambdaServiceClient {
    private static final String SET_ITEM_ENDPOINT = "example";
    private static final String GET_ITEM_ENDPOINT = "item/all";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }


    public List<ItemData> getPriorityList(){

        EndpointUtility endpointUtility = new EndpointUtility();
        // AWS type of call - NOT LOCAL
        String response = endpointUtility.getEndpoint(GET_ITEM_ENDPOINT);

        List<ItemData> results = new ArrayList<>();
        try {
            // TODO - NEED TO FIGURE OUT THE JSON READER ON WHY IT'S NOT DESERIALIING RIGHT
            ItemData itemDataList = mapper.readValue(response, new TypeReference<>(){});
            results.add(itemDataList);
        } catch (Exception e) {
            throw new ApiGatewayException("Response output is: " + response + "\n\nUnable to map deserialize JSON: " + e);
        }
        return results;
    }



    public ItemData setItemData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(SET_ITEM_ENDPOINT, data);
        ItemData itemData;
        try {
            itemData = mapper.readValue(response, ItemData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return itemData;
    }
}
