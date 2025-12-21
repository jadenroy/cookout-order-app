package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private int cookoutId;
    private int attendeeId;

    private List<Integer> itemIds = new ArrayList<>();
    private List<Integer> itemQuantities = new ArrayList<>();

    public OrderDto() {};

    public OrderDto(int cookoutId, int attendeeId, List<Integer> itemIds, List<Integer> itemQuantities) {
        this.cookoutId = cookoutId;
        this.attendeeId = attendeeId;
        this.itemIds = itemIds;
        this.itemQuantities = itemQuantities;
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

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public List<Integer> getItemQuantities() {
        return itemQuantities;
    }

    public void setItemQuantities(List<Integer> itemQuantities) {
        this.itemQuantities = itemQuantities;
    }
}
