package org.stackit.shop;

import io.noctin.events.EntityHandler;
import org.bukkit.ChatColor;
import org.stackit.Bundler;
import org.stackit.StackIt;
import org.stackit.StackItBundle;
import org.stackit.StackItLogger;

public class StackItShop extends StackItBundle {

    public static final String PREFIX = ChatColor.AQUA + "[StackIt-Shop] ";
    public static final String ENABLING_MESSAGE = "Successfully enabled StackIt-Shop bundle !";

    private Bundler bundler;
    private EntityHandler handler;
    private StackItLogger logger = new StackItLogger(getServer().getConsoleSender(), PREFIX);

    public StackItShop() {
        super("StackIt Shop");
    }

    @Override
    public void onLoad() {
        this.author = getDescription().getAuthors().get(0);
        this.description = getDescription().getDescription();
        this.version = getDescription().getVersion();

        this.bundler = StackIt.bundler();

        this.bundler.registerBundle(this);

        this.handler = bundler.getEventHandler(this);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
