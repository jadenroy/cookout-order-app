package com.techelevator.model;

public class OrderItem {
    private int id;
    private int orderId;
    private int itemId;
    private int quantity;

    public OrderItem() {}

    public OrderItem(int id, int orderId, int itemId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
