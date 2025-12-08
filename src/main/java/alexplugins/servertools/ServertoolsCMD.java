package alexplugins.servertools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ServertoolsCMD implements CommandExecutor, TabCompleter {

    private final String version = "v0.0.1";

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
        }

        return suggestions;
    }
}
