package com.techelevator.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Cookout {

    private int id;
    private int hostId;
    private Integer chefId;
    private Integer menuId;
    private LocalDateTime startTime;

    private String name;
    private String location;
    private String description;

    public Cookout() {}

    public Cookout(int id, int hostId, Integer chefId, Integer menuId, LocalDateTime startTime,
                   String name, String location, String description) {
        this.id = id;
        this.hostId = hostId;
        this.chefId = chefId;
        this.menuId = menuId;
        this.startTime = startTime;
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHostId() { return hostId; }
    public void setHostId(int hostId) { this.hostId = hostId; }

    public Integer getChefId() { return chefId; }
    public void setChefId(Integer chefId) { this.chefId = chefId; }

    public Integer getMenuId() { return menuId; }
    public void setMenuId(Integer menuId) { this.menuId = menuId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
