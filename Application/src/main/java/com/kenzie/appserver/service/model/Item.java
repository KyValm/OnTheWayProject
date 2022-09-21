package com.kenzie.appserver.service.model;

public class Item {
    private final String itemId;
    private final String description;
    private int currentQty;
    private int reorderQty;
    private int qtyTrigger;
    private String onTheWay;

    public Item(String itemId,
                String description,
                currentQty,
                reorderQty,
                qtyTrigger,
                onTheWay
                ) {
        this.itemId = itemId;
        this.description = description;
        this.currentQty = currentQty;
        this.reorderQty = reorderQty;
        this.qtyTrigger = qtyTrigger;
        this.onTheWay = onTheWay;
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

    public String getOnTheWay() {
        return onTheWay;
    }

    public void setOnTheWay(String onTheWay) {
        this.onTheWay = onTheWay;
    }
}

