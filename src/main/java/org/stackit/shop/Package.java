package org.stackit.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public final class Package {
    private int id;
    private String uid;
    private String player_uuid;
    private String commands;
    private int slotsnumber;
    private String name;
    private long claimed_time;

    private PackageUID packageUID;
    private UUID playerUUID;
    private ArrayList<String> commandsList;
    private Date claimedDate;

    public Package() {}

    public Package(int id, String uid, String player_uuid, String commands, int slotsnumber, String name, long claimed_time) {
        this.id = id;
        this.uid = uid;
        this.player_uuid = player_uuid;
        this.commands = commands;
        this.slotsnumber = slotsnumber;
        this.name = name;
        this.claimed_time = claimed_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlayer_uuid() {
        return player_uuid;
    }

    public void setPlayer_uuid(String player_uuid) {
        this.player_uuid = player_uuid;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public int getSlotsnumber() {
        return slotsnumber;
    }

    public void setSlotsnumber(int slotsnumber) {
        this.slotsnumber = slotsnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getClaimed_time() {
        return claimed_time;
    }

    public void setClaimed_time(long claimed_time) {
        this.claimed_time = claimed_time;
    }

    /************************************************/

    public PackageUID getPackageUID() {
        return packageUID;
    }

    public void setPackageUID(PackageUID packageUID) {
        this.packageUID = packageUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public ArrayList<String> getCommandsList() {
        return commandsList;
    }

    public void setCommandsList(ArrayList<String> commandsList) {
        this.commandsList = commandsList;
    }

    public Date getClaimedDate() {
        return claimedDate;
    }

    public void setClaimedDate(Date claimedDate) {
        this.claimedDate = claimedDate;
    }

    public boolean isClaimed(){
        return this.claimed_time != 0;
    }
}
