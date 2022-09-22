package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.appserver.service.model.OrderDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {
    @JsonProperty("itemId")
    private String itemId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("currentQty")
    private int currentQty;
    @JsonProperty("reorderQty")
    private int reorderQty;
    @JsonProperty("qtyTrigger")
    private int qtyTrigger;
    @JsonProperty("orderDate")
    private OrderDate orderDate;

    public ItemResponse(Item item){
        this.itemId = item.getItemId();
        this.description = item.getDescription();
        this.currentQty = item.getCurrentQty();
        this.reorderQty = item.getReorderQty();
        this.qtyTrigger = item.getQtyTrigger();
        this.orderDate = item.getOrderDate();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(int currentQty) {
        this.currentQty = currentQty;
    }

    public int getReorderQty() {
        return reorderQty;
    }

    public void setReorderQty(int reorderQty) {
        this.reorderQty = reorderQty;
    }

    public int getQtyTrigger() {
        return qtyTrigger;
    }

    public void setQtyTrigger(int qtyTrigger) {
        this.qtyTrigger = qtyTrigger;
    }

    public OrderDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(OrderDate orderDate) {
        this.orderDate = orderDate;
    }
}
