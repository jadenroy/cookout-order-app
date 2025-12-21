package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderId;
    private int cookoutId;
    private int attendeeId;
    private String attendeeUsername;
    private boolean finished;

    private List<OrderItem> items = new ArrayList<>();

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCookoutId() {
        return cookoutId;
    }

    public void setCookoutId(int cookoutId) {
        this.cookoutId = cookoutId;
    }

    public int getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(int attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getAttendeeUsername() {
        return attendeeUsername;
    }

    public void setAttendeeUsername(String attendeeUsername) {
        this.attendeeUsername = attendeeUsername;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }
}
