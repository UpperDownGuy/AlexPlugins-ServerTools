package alexplugins.servertools.cmds;

import alexplugins.servertools.main.ServerTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class ServertoolsCMD implements CommandExecutor, TabCompleter {

    private final String version = "v0.0.4";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("servertools")) {
            return true;
        }

        // No args
        if (args.length == 0) {
            sender.sendMessage("ServerTools is working correctly!");
            return true;
        }

        // Only one argument allowed, so args[0] is safe
        switch (args[0].toLowerCase()) {

            case "version":
                sender.sendMessage("§aVersion: " + version);
                return true;

            case "disable":
                if (!sender.hasPermission("alexplugins.servertools.command.disable")
                        && !sender.hasPermission("alexplugins.servertools.function.disable")) {

                    sender.sendMessage("§cYou don't have permission to run this command!");
                    return true;
                }

                sender.sendMessage("§bDisabling ServerTools");
                ServerTools.getInstance().getLogger().warning("ServerTools disabled by " + sender.getName());
                Bukkit.getPluginManager().disablePlugin(ServerTools.getInstance());
                return true;

            case "verifyclient":
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cPlayers only.");
                    return true;
                }

                player.sendMessage("§6Verification Status");

                // Login verification
                if (getServer().getOnlineMode()) {
                    player.sendMessage("§a✔ Login verified (online-mode)");
                    player.sendMessage("§7UUID: §f" + player.getUniqueId());
                } else {
                    player.sendMessage("§c✖ Login NOT verified (offline-mode)");
                }

                // Client identification
                String brand = player.getClientBrandName();
                if (brand != null) {
                    player.sendMessage("§eClient brand: §f" + brand);
                } else {
                    player.sendMessage("§cClient brand unavailable.");
                }

                return true;

            case "playeridentification":
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cPlayers only.");
                    return true;
                }

                if (getServer().getOnlineMode()) {
                    UUID uuid = player.getUniqueId();
                    sender.sendMessage("§bPlayer Identification:");
                    sender.sendMessage("§aUsername: " + sender.getName());
                    sender.sendMessage("§aUUID: " + uuid);
                    sender.sendMessage("§aClient Type: " + player.getClientBrandName());
                    return true;
                }
                else {
                    String uuid = "§cNot Available!";
                    sender.sendMessage("§bPlayer Identification:");
                    sender.sendMessage("§aUsername: " + sender.getName());
                    sender.sendMessage("§aUUID: " + uuid);
                    sender.sendMessage("§aClient Type: " + player.getClientBrandName());
                    return true;
                }



            default:
                sender.sendMessage("§cUnknown command!");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("version");
            suggestions.add("disable");
            suggestions.add("verifyclient");
            suggestions.add("playeridentification");
        }

        return suggestions;
    }
}
