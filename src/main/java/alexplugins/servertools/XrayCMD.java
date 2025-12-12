package alexplugins.servertools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class XrayCMD implements CommandExecutor {

    private final XRay xray;

    public XrayCMD(XRay xrayInstance) {
        this.xray = xrayInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Base permission
        if (!sender.hasPermission("alexplugins.servertools.commands.xray")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use /xray.");
            return true;
        }

        // No args = help menu
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {

            // ---------------- HELP ----------------
            case "help":
                sendHelp(sender);
                return true;

            // ---------------- VIEW ----------------
            case "view":
                sender.sendMessage(ChatColor.GOLD + "------ X-Ray Detector Settings ------");
                sender.sendMessage(ChatColor.YELLOW + "Coal: " + XRay.COAL_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Iron: " + XRay.IRON_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Gold: " + XRay.GOLD_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Copper: " + XRay.COPPER_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Redstone: " + XRay.REDSTONE_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Lapis: " + XRay.LAPIS_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Emerald: " + XRay.EMERALD_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Diamond: " + XRay.DIAMOND_LIMIT);
                sender.sendMessage(ChatColor.YELLOW + "Netherite (Ancient Debris): " + XRay.NETHERITE_LIMIT);
                return true;

            // ---------------- RELOAD ----------------
            case "reload":
                if (!sender.hasPermission("alexplugins.servertools.xray.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to reload X-Ray settings.");
                    return true;
                }

                xray.reloadValues();
                sender.sendMessage(ChatColor.GREEN + "X-Ray configuration reloaded successfully.");
                return true;

            // ---------------- SET VALUES ----------------
            case "set":
                if (!sender.hasPermission("alexplugins.servertools.xray.changevalues")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to modify X-Ray values.");
                    return true;
                }

                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /xray set <ore> <amount>");
                    return true;
                }

                String ore = args[1].toLowerCase();
                int amount;

                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Amount must be a number!");
                    return true;
                }

                boolean success = switch (ore) {
                    case "coal" -> { XRay.COAL_LIMIT = amount; yield true; }
                    case "iron" -> { XRay.IRON_LIMIT = amount; yield true; }
                    case "gold" -> { XRay.GOLD_LIMIT = amount; yield true; }
                    case "copper" -> { XRay.COPPER_LIMIT = amount; yield true; }
                    case "redstone" -> { XRay.REDSTONE_LIMIT = amount; yield true; }
                    case "lapis" -> { XRay.LAPIS_LIMIT = amount; yield true; }
                    case "emerald" -> { XRay.EMERALD_LIMIT = amount; yield true; }
                    case "diamond" -> { XRay.DIAMOND_LIMIT = amount; yield true; }
                    case "netherite", "ancientdebris", "debris" -> { XRay.NETHERITE_LIMIT = amount; yield true; }

                    default -> false;
                };

                if (!success) {
                    sender.sendMessage(ChatColor.RED + "Unknown ore type: " + ore);
                    return true;
                }

                sender.sendMessage(ChatColor.GREEN + "Updated X-Ray limit for " + ore + " to " + amount + ".");
                return true;

            // ---------------- UNKNOWN ----------------
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /xray help.");
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "---- /xray Commands ----");
        sender.sendMessage(ChatColor.YELLOW + "/xray view" + ChatColor.WHITE + " - View current detector limits");
        sender.sendMessage(ChatColor.YELLOW + "/xray set <ore> <amount>" + ChatColor.WHITE + " - Change ore limit");
        sender.sendMessage(ChatColor.YELLOW + "/xray reload" + ChatColor.WHITE + " - Reload xray.yml");
        sender.sendMessage(ChatColor.YELLOW + "/xray help" + ChatColor.WHITE + " - Show this help menu");
    }
}
