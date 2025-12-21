package com.techelevator.model;

public class Invitation {

    private int invitationId;
    private int fromId;
    private int toId;
    private int cookoutId;
    private boolean active;
    private boolean accepted;

    // ✅ DISPLAY FIELDS (Option B)
    private String cookoutName;
    private String fromUsername;

    public Invitation() {}

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getCookoutId() {
        return cookoutId;
    }

    public void setCookoutId(int cookoutId) {
        this.cookoutId = cookoutId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    // ===== DISPLAY =====

    public String getCookoutName() {
        return cookoutName;
    }

    public void setCookoutName(String cookoutName) {
        this.cookoutName = cookoutName;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }
}
