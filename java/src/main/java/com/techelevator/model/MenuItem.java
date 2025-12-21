package com.techelevator.model;

public class MenuItem {

    private int id;
    private int cookoutId;
    private String itemName;

    public MenuItem() {}

    public MenuItem(int id, int cookoutId, String itemName) {
        this.id = id;
        this.cookoutId = cookoutId;
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCookoutId() {
        return cookoutId;
    }

    public void setCookoutId(int cookoutId) {
        this.cookoutId = cookoutId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
