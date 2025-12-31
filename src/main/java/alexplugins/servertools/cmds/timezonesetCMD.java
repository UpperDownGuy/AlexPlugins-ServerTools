package alexplugins.servertools.cmds;

import alexplugins.servertools.main.ServerTools;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.*;

public class timezonesetCMD implements CommandExecutor, TabCompleter {

    private final ServerTools plugin;

    public timezonesetCMD(ServerTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (!plugin.getSTConfig().getBoolean("timezones.enabled")) {
            player.sendMessage("§cTimezones are disabled on this server.");
            return true;
        }

        if (args.length == 0) {
            if (plugin.getTimezoneManager().hasTimezone(player.getUniqueId())) {
                player.sendMessage("§aYour timezone is set to §e"
                        + plugin.getTimezoneManager().getPlayerZone(player.getUniqueId()));
                player.sendMessage("§7Use §6/tz change §7to update it.");
            } else {
                player.sendMessage("§eYour timezone is not set.");
                player.sendMessage("§7Use §6/tz set §7to set it.");
            }
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "set":
            case "change": {

                if (args.length != 2) {
                    player.sendMessage("§eUsage: §6/tz " + args[0] + " <ZoneId>");
                    player.sendMessage("§7Find your ZoneId at §bhttps://tz.alexxweb.com");
                    return true;
                }

                try {
                    ZoneId zone = ZoneId.of(args[1]);
                    plugin.getTimezoneManager().setTimezone(player.getUniqueId(), zone);

                    player.sendMessage("§aTimezone set to §e" + zone);

                } catch (ZoneRulesException ex) {
                    player.sendMessage("§cInvalid timezone.");
                    player.sendMessage("§7Use §bhttps://tz.alexxweb.com");
                }
                return true;
            }

            case "remove": {
                plugin.getTimezoneManager().removeTimezone(player.getUniqueId());
                player.sendMessage("§aYour timezone has been removed.");
                player.sendMessage("§7Using server default.");
                return true;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            return List.of("set", "change", "remove");
        }

        return Collections.emptyList();
    }
}
