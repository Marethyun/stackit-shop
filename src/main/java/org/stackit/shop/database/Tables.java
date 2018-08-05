package org.stackit.shop.database;

public enum Tables {
    PACKAGES("stackit_packages");

    private String tableName;

    Tables(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
