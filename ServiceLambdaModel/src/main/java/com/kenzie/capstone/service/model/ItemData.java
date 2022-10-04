package com.kenzie.capstone.service.model;

import java.util.Objects;

public class ItemData {
    private final String itemId;
    private final String description;
    private String currentQty;
    private String reorderQty; // Purchase Order Qty
    private String qtyTrigger; // ReOrder Point
    private String orderDate;  // status if order request triggered -> "last reorder 09/21/2022"

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

    public String getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
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


