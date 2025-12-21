package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private int cookoutId;
    private List<MenuItem> items = new ArrayList<>();

    public Menu() {}

    public Menu(int cookoutId, List<MenuItem> items) {
        this.cookoutId = cookoutId;
        this.items = items;
    }

    public int getCookoutId() {
        return cookoutId;
    }

    public void setCookoutId(int cookoutId) {
        this.cookoutId = cookoutId;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public void addItem(MenuItem item) {
        this.items.add(item);
    }
}
