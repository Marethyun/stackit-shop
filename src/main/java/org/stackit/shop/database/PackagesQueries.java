package org.stackit.shop.database;

public enum PackagesQueries {
    GET_ALL("SELECT * FROM %t");

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
