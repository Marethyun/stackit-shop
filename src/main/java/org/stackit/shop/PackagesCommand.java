package org.stackit.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Handle;
import org.stackit.StackItCommand;
import org.stackit.StackItLogger;
import org.stackit.StackItPermissions;
import org.stackit.shop.database.PackagesMapper;
import org.stackit.shop.database.PackagesQueries;

import java.util.List;
import java.util.UUID;

public class PackagesCommand extends StackItCommand.Option {

    public static final String PERMISSION_USE = StackItPermissions.STACKIT_COMMAND.getPermission() + ".shop.packages.use";
    public static final String PERMISSION_OTHERS = StackItPermissions.STACKIT_COMMAND.getPermission() + ".shop.packages.others";

    public static final String MESSAGE_CONSOLE_MUST_PROVIDE_PLAYER = "As a console you must provide a player name";

    public static final String MESSAGE_PERMISSION_OTHERS = "You don't have the required permission to retrieve packages of an other player";

    public static final String USAGE = "/stackit packages [player]";
    public static final String DESCRIPTION = "Retrieve all packages according to the sender, by pages of 10";

    private final StackItShop pluginInstance;
    private StackItLogger logger;

    public PackagesCommand(StackItShop plugin) {
        super("packages");

        this.pluginInstance = plugin;

        this.permission = PERMISSION_USE;
        this.usage = USAGE;
        this.description = DESCRIPTION;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.logger = new StackItLogger(sender, StackItShop.PREFIX);

        UUID playerUUID = null;
        String playerName = null;

        if (args.length > 0){
            playerName = args[0];
            playerUUID = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        }

        if (sender instanceof Player){
            Player player = (Player) sender;

            // If at this point the uuid is null, retrieve the one of the current player
            if (playerUUID == null){
                playerName = player.getName();
                playerUUID = player.getUniqueId();
            }

            if (!playerUUID.equals(player.getUniqueId())) {
                if (!player.hasPermission(PERMISSION_OTHERS)){
                    logger.error(MESSAGE_PERMISSION_OTHERS);
                    return true;
                }
            }

            List<Package> packages = retrievePackages(playerUUID);

            displayPackages(packages, playerName, playerUUID.toString());
        }

        if (sender instanceof ConsoleCommandSender){
            // The console sender must provide the parameter "player"

            if (playerName == null){

                logger.info(MESSAGE_CONSOLE_MUST_PROVIDE_PLAYER);
                logger.info(USAGE);

                return true;
            }

            List<Package> packages = retrievePackages(playerUUID);

            displayPackages(packages, playerName, playerUUID.toString());

        }

        return true;
    }

    private List<Package> retrievePackages(UUID uuid){
        try (Handle handle = pluginInstance.getJdbi().open()){
            List<Package> packages = handle.createQuery(PackagesQueries.GET_WITH_PLAYER_UUID.getQuery())
                    .bind("player_uuid", uuid.toString())
                    .map(new PackagesMapper())
                    .list();
            return PackagesMapper.mapEntityList(packages);

        }
    }

    private void displayPackages(List<Package> packages, String playerName, String playerUUID){
        if (packages.size() == 0){
            logger.info(String.format("Player %s (%s) don't have any packages at his name yet.", playerName, playerUUID));
        } else {
            logger.info(String.format("Package(s) of player %s (%s): ", playerName, playerUUID));

            // E.g 1 - AZER1234: A beautiful package
            final String entry = "%s - %s: %s (%s)";

            for (int i = 0; i < packages.size(); i++) {
                Package pkg = packages.get(i);

                String claimed = pkg.isClaimed() ? "claimed" : "unclaimed";

                logger.info(ChatColor.DARK_AQUA + String.format(entry, i + 1, pkg.getUid(), pkg.getName(), claimed));
            }
        }
    }
}
