package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;
@DynamoDBTable(tableName = "LambdaItems")
public class ItemData {
    private String itemId;
    private String description;
    private String currentQty;
    private String reorderQty; // Purchase Order Qty
    private String qtyTrigger; // ReOrder Point
    private String orderDate;  // status if order request triggered -> "last reorder 09/21/2022"

    public ItemData(){

    }

    public ItemData(String itemId,
                String description,
                String currentQty,
                String reorderQty,
                String qtyTrigger,
                String orderDate
    ) {
        this.itemId = itemId;
        this.description = description;
        this.currentQty = currentQty;
        this.reorderQty = reorderQty;
        this.qtyTrigger = qtyTrigger;
        this.orderDate = orderDate;
    }
    @DynamoDBHashKey(attributeName = "ItemId")
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "CurrentQty")
    public String getCurrentQty() {
        return currentQty;
    }
    public void setCurrentQty(String currentQty) {
        this.currentQty = currentQty;
    }

    @DynamoDBAttribute(attributeName = "ReorderQty")
    public String getReorderQty() {
        return reorderQty;
    }
    public void setReorderQty(String reorderQty) {
        this.reorderQty = reorderQty;
    }

    @DynamoDBAttribute(attributeName = "QtyTrigger")
    public String getQtyTrigger() {
        return qtyTrigger;
    }
    public void setQtyTrigger(String qtyTrigger) {
        this.qtyTrigger = qtyTrigger;
    }

    @DynamoDBAttribute(attributeName = "OrderDate")
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}


