package com.kenzie.appserver.service.model;

import java.util.Date;

public class ItemPOJO {
    private final String itemId;
    private final String description;
    private int currentQty;
    private int reorderQty; // Purchase Order Qty
    private int qtyTrigger; // ReOrder Point
    private OrderDate orderDate;  // status if order request triggered -> "last reorder 09/21/2022"

    public ItemPOJO(String itemId,
                String description,
                int currentQty,
                int reorderQty,
                int qtyTrigger,
                OrderDate orderDate
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

    public OrderDate getOnTheWay() {
        return orderDate;
    }

    public void setOnTheWay(OrderDate orderDate) {
        this.orderDate = orderDate;
    }
}





























