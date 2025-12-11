package alexplugins.servertools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XrayTab implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "help", "view", "set", "reload"
    );

    private static final List<String> ORES = Arrays.asList(
            "coal", "iron", "diamond"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

        if (!sender.hasPermission("alexplugins.servertools.commands.xray"))
            return new ArrayList<>();

        List<String> list = new ArrayList<>();

        // /xray <subcommand>
        if (args.length == 1) {
            for (String sub : SUBCOMMANDS)
                if (sub.startsWith(args[0].toLowerCase()))
                    list.add(sub);
            return list;
        }

        // /xray set <ore>
        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            for (String ore : ORES)
                if (ore.startsWith(args[1].toLowerCase()))
                    list.add(ore);
            return list;
        }

        // /xray set <ore> <amount>
        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            for (int i = 1; i <= 100; i++) {
                String s = String.valueOf(i);
                if (s.startsWith(args[2]))
                    list.add(s);
            }
            return list;
        }

        return new ArrayList<>();
    }
}
