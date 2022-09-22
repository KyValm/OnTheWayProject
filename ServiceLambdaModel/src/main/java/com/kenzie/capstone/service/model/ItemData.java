package com.kenzie.capstone.service.model;

import java.util.Objects;

public class ItemData {
    private String itemId;
    private String description;
    private int currentQty;
    private int reorderQty; // Purchase Order Qty
    private int qtyTrigger; // ReOrder Point
//    private OrderDate orderDate;

    public ItemData(String itemId,
                String description,
                int currentQty,
                int reorderQty,
                int qtyTrigger
//                OrderDate orderDate // OrderDate Class needs creating
    ) {
        this.itemId = itemId;
        this.description = description;
        this.currentQty = currentQty;
        this.reorderQty = reorderQty;
        this.qtyTrigger = qtyTrigger;
//        this.orderDate = orderDate;
    }

    public String getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
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

//    public OrderDate getOrderDate() {
//        return orderDate;
//    }
//
//    public void setOrderDate(OrderDate orderDate) {
//        this.orderDate = orderDate;
//    }
}


