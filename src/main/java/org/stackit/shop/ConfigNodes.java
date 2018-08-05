package org.stackit.shop;

public enum ConfigNodes {
    DATABASE_HOST("StackItShop.database.host"),
    DATABASE_PORT("StackItShop.database.port"),
    DATABASE_USERNAME("StackItShop.database.username"),
    DATABASE_PASSWORD("StackItShop.database.password"),
    DATABASE_NAME("StackItShop.database.name");

    private String node;

    ConfigNodes(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
