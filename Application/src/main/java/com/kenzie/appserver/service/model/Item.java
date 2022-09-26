package com.kenzie.appserver.service.model;

import java.util.Date;

public class Item {
    private final String itemId;
    private final String description;
    private int currentQty;
    private int reorderQty; // Purchase Order Qty
    private int qtyTrigger; // ReOrder Point
    private String orderDate;  // status if order request triggered -> "last reorder 09/21/2022"

    public Item(String itemId,
                String description,
                int currentQty,
                int reorderQty,
                int qtyTrigger,
                String orderDate // OrderDate Class needs creating
    ) {
        this.itemId = itemId;
        this.description = description;
        this.currentQty = currentQty;
        this.reorderQty = reorderQty;
        this.qtyTrigger = qtyTrigger;
        this.orderDate = orderDate;
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

    public String getOrderDate() {
        return orderDate;
    } // planned order date

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}





























