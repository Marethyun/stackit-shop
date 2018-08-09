package org.stackit.shop;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jdbi.v3.core.Handle;
import org.stackit.StackItCommand;
import org.stackit.StackItLogger;
import org.stackit.StackItPermissions;
import org.stackit.shop.database.PackagesMapper;
import org.stackit.shop.database.PackagesQueries;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClaimCommand extends StackItCommand.Option {

    public static final String UID_NOT_PROVIDED = "You must provide a package UID";
    public static final String UID_MALFORMED = "You malformed the package UID, it must contains 8 chars, numbers or letters";
    public static final String UID_EXAMPLE = "E.G. MDJ5OH90";
    public static final String CONSOLE_ERROR = "As a console you cannot claim a package for a player";

    public static final String PERMISSION_USE = StackItPermissions.STACKIT_COMMAND + ".shop.claim.use";
    public static final String DESCRIPTION = "Claims a package by its unique id";
    public static final String USAGE = "/stackit claim <packageuid>";

    private StackItLogger logger;
    private final StackItShop pluginInstance;

    public ClaimCommand(StackItShop plugin){
        super("claim");

        this.permission = PERMISSION_USE;
        this.description = DESCRIPTION;
        this.usage = USAGE;

        this.pluginInstance = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.logger = new StackItLogger(sender, StackItShop.PREFIX);

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                try {
                    PackageUID packageUID = new PackageUID(args[0]);

                    UUID uuid = player.getUniqueId();

                    List<Package> packages = retrievePackages(uuid);

                    List<Package> collect = packages.stream().filter(p -> p.getPackageUID().equals(packageUID) && !p.isClaimed()).collect(Collectors.toList());

                    if (collect.size() == 0){
                        logger.error(String.format("You don't have any unclaimed package with UID '%s'", packageUID));
                    } else {
                        Package pkg = collect.get(0);

                        if (freeInventorySlots(player.getInventory()) >= pkg.getSlotsnumber()) {

                            // Claim the package
                            try (Handle handle = pluginInstance.getJdbi().open()) {
                                handle.createUpdate(PackagesQueries.CLAIM_PACKAGE_WITH_ID.getQuery())
                                        .bind("claimed_time", System.currentTimeMillis())
                                        .bind("id", pkg.getId())
                                        .execute();
                            } catch (Exception e) {
                                logger.error("An error occurred while claiming the package");
                                pluginInstance.logger().error(String.format("An error occurred while executing commands of %s's (%s) package with UID %s", player.getName(), uuid.toString(), packageUID.toString()));
                                e.printStackTrace();
                                return true;
                            }

                            try {
                                for (String cmd : pkg.getCommandsList()) {
                                    executeCommand(cmd);

                                }
                            } catch (CommandException e) {
                                logger.error("An error occurred while claiming the package");
                                pluginInstance.logger().error(String.format("An error occurred while executing commands of %s's (%s) package with UID %s", player.getName(), uuid.toString(), packageUID.toString()));
                                e.printStackTrace();
                                return true;
                            }

                            logger.info(String.format("You successfully claimed the package with id %s !", packageUID.toString()));
                            return true;
                        } else {
                            logger.error(String.format("You don't have enough slots in your inventory to claim this package ! (required: %s)", pkg.getSlotsnumber()));
                        }
                    }

                } catch (PackageUID.MalformedUIDException e) {
                    logger.error(UID_MALFORMED);
                    logger.error(UID_EXAMPLE);
                }

            } else {
                logger.error(UID_NOT_PROVIDED);
                logger.error(USAGE);
            }
            return true;
        }

        if (sender instanceof ConsoleCommandSender){
            logger.error(CONSOLE_ERROR);
            return true;
        }

        return false;
    }

    private void executeCommand(String command) throws CommandException {
        Server server = pluginInstance.getServer();

        server.dispatchCommand(server.getConsoleSender(), command);
    }

    private int freeInventorySlots(Inventory inventory){
        ItemStack[] stacks = inventory.getContents();
        int slotNumber = 0;
        for (ItemStack stack : stacks){
            if (stack == null){
                slotNumber++;
            }
        }
        return slotNumber;
    }

    // TODO: Centralize method
    private List<Package> retrievePackages(UUID uuid){
        try (Handle handle = pluginInstance.getJdbi().open()){
            List<Package> packages = handle.createQuery(PackagesQueries.GET_WITH_PLAYER_UUID.getQuery())
                    .bind("player_uuid", uuid.toString())
                    .map(new PackagesMapper())
                    .list();
            return PackagesMapper.mapEntityList(packages);

        }
    }
}
