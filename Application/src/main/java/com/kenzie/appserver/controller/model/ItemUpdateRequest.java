package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Item;
import com.kenzie.appserver.service.model.OrderDate;

import javax.validation.constraints.NotEmpty;

public class ItemUpdateRequest {
    @NotEmpty
    @JsonProperty("itemId")
    private String itemId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("currentQty")
    private String currentQty;
    @JsonProperty("reorderQty")
    private String reorderQty;
    @JsonProperty("qtyTrigger")
    private String qtyTrigger;
    @JsonProperty("orderDate")
    private String orderDate;

    public ItemUpdateRequest(){

    }

    public ItemUpdateRequest(Item item){
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

    public String getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(String currentQty) {
        this.currentQty = currentQty;
    }

    public String getReorderQty() {
        return reorderQty;
    }

    public void setReorderQty(String reorderQty) {
        this.reorderQty = reorderQty;
    }

    public String getQtyTrigger() {
        return qtyTrigger;
    }

    public void setQtyTrigger(String qtyTrigger) {
        this.qtyTrigger = qtyTrigger;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
