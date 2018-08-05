package org.stackit.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public final class Package {
    private int id;
    private PackageUID uid;
    private UUID playerUUID;
    private ArrayList<String> commands;
    private int slotsNumber;
    private String name;
    private Date claimed;

    public Package() {
    }

    public Package(int id, PackageUID uid, UUID playerUUID, ArrayList<String> commands, int slotsNumber, String name, Date claimed) {
        this.id = id;
        this.uid = uid;
        this.playerUUID = playerUUID;
        this.commands = commands;
        this.slotsNumber = slotsNumber;
        this.name = name;
        this.claimed = claimed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PackageUID getUid() {
        return uid;
    }

    public void setUid(PackageUID uid) {
        this.uid = uid;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }

    public int getSlotsNumber() {
        return slotsNumber;
    }

    public void setSlotsNumber(int slotsNumber) {
        this.slotsNumber = slotsNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getClaimed() {
        return claimed;
    }

    public void setClaimed(Date claimed) {
        this.claimed = claimed;
    }

    public boolean isClaimed(){
        return this.claimed != null;
    }
}
