package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.controller.model.ItemUpdateRequest;
import com.kenzie.appserver.service.ItemService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.service.model.Item;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ItemService itemService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createItem_validInput_Exists() throws Exception {
        //GIVEN
        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString()
        );

        ItemCreateRequest request = new ItemCreateRequest();
        request.setItemId(newItem.getItemId());
        request.setDescription(newItem.getDescription());
        request.setCurrentQty(newItem.getCurrentQty());
        request.setReorderQty(newItem.getReorderQty());
        request.setQtyTrigger(newItem.getQtyTrigger());
        request.setOrderDate(newItem.getOrderDate());
        //WHEN
        mvc.perform(post("/item")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                //WHEN
                .andExpect(jsonPath("itemId")
                        .exists())
                .andExpect(jsonPath("description")
                        .value(is(newItem.getDescription())))
                .andExpect(jsonPath("currentQty")
                        .value(is(newItem.getCurrentQty())))
                .andExpect(jsonPath("reorderQty")
                        .value(is(newItem.getReorderQty())))
                .andExpect(jsonPath("qtyTrigger")
                        .value(is(newItem.getQtyTrigger())))
                .andExpect(jsonPath("orderDate")
                        .value(is(newItem.getOrderDate())))
                .andExpect(status().isCreated());

    }
    @Test
    public void updateItem_successful() throws Exception {
        //GIVEN
        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString());

        Item item = itemService.addInventoryItem(newItem);
        String updatedDescription = "Item is updated for testing purposes";

        ItemUpdateRequest request = new ItemUpdateRequest();
        request.setItemId(newItem.getItemId());
        request.setDescription(updatedDescription);
        request.setCurrentQty(newItem.getCurrentQty());
        request.setReorderQty(newItem.getReorderQty());
        request.setQtyTrigger(newItem.getQtyTrigger());
        request.setOrderDate(newItem.getOrderDate());
        //WHEN
        mvc.perform(MockMvcRequestBuilders.put("/item")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //THEN
                .andExpect(jsonPath("itemId")
                        .exists())
                .andExpect(jsonPath("description")
                        .value(is("Item is updated for testing purposes")))
                .andExpect(jsonPath("currentQty")
                        .value(is(newItem.getCurrentQty())))
                .andExpect(jsonPath("reorderQty")
                        .value(is(newItem.getReorderQty())))
                .andExpect(jsonPath("qtyTrigger")
                        .value(is(newItem.getQtyTrigger())))
                .andExpect(jsonPath("orderDate")
                        .value(is(newItem.getOrderDate())))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteItem_DeleteSuccessful() throws Exception {
        // GIVEN
        Item newItem = new Item(
                randomUUID().toString(),
                "test item",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString());

       Item persistedItem = itemService.addInventoryItem(newItem);

        // WHEN
        mvc.perform(delete("/{id}", persistedItem.getItemId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());
        assertThat(itemService.getItemByID(persistedItem.getItemId())).isNull();
    }
    @Test
    public void getAll_Contains_items() throws Exception {

        String id = UUID.randomUUID().toString();

        Item newItem = new Item(
                randomUUID().toString(),
                "test item 1",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString());

        Item newItem2 = new Item(
                randomUUID().toString(),
                "test item 2",
                "1",
                "1",
                "0",
                LocalDateTime.now().toString());

       Item persistedItem = itemService.addInventoryItem(newItem);
       Item persistedItem2 = itemService.addInventoryItem(newItem2);

        mvc.perform(get("/item/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}