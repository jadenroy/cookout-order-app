package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

public class MenuDto {

    private int cookoutId;
    private List<String> itemNames = new ArrayList<>();

    public MenuDto() {}

    public MenuDto(int cookoutId, List<String> itemNames) {
        this.cookoutId = cookoutId;
        this.itemNames = itemNames;
    }

    public int getCookoutId() {
        return cookoutId;
    }

    public void setCookoutId(int cookoutId) {
        this.cookoutId = cookoutId;
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }
}
