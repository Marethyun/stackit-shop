package org.stackit.shop.database;

public enum PackagesQueries {
    GET_ALL("SELECT * FROM %t"),
    INSERT_ONE("INSERT INTO %t (uid, player_uuid, commands, slotsnumber, name, claimed_time) VALUES (:uid, :player_uuid, :commands, :slotsnumber, :name, :claimed_time)"),
    GET_WITH_PLAYER_UUID("SELECT * FROM %t WHERE player_uuid = :player_uuid");

    private String query;

    PackagesQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query.replace("%t", Tables.PACKAGES.getTableName());
    }

    @Override
    public String toString() {
        return getQuery();
    }
}
