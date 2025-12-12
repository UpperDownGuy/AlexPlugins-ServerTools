package alexplugins.servertools;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ServertoolsCMD implements CommandExecutor, TabCompleter {

    private final String version = "v0.0.2";

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

            case "--version":
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



            default:
                sender.sendMessage("§cUnknown command!");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("--version");
            suggestions.add("disable");
        }

        return suggestions;
    }
}
