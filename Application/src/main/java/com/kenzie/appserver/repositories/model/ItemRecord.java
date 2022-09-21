package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Items")
public class ItemRecord {
    private String itemId;
    private String description;
    private String currentQty;
    private String reorderQty;
    private String qtyTrigger;
    private String onTheWay; // status if ordered triggered


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

    public void setDescription(String currentQty) {
        this.currentQty = currentQty;
    }

    @DynamoDBAttribute(attributeName = "ReorderQty")
    public String getReorderQty() {
        return reorderQty;
    }

    public void setDescription(String reorderQty) {
        this.reorderQty = reorderQty;
    }

    @DynamoDBAttribute(attributeName = "QtyTrigger")
    public String getQtyTrigger() {
        return qtyTrigger;
    }

    public void setDescription(String qtyTrigger) {
        this.qtyTrigger = qtyTrigger;
    }

    @DynamoDBAttribute(attributeName = "OnTheWay")
    public String getOnTheWay() {
        return onTheWay;
    }

    public void setDescription(String onTheWay) {
        this.onTheWay = onTheWay;
    }

}
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        ExampleRecord exampleRecord = (ExampleRecord) o;
//        return Objects.equals(id, exampleRecord.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//}

