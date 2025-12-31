package alexplugins.servertools.cmds;

import alexplugins.servertools.main.ServerTools;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class DisconnectCMD implements CommandExecutor {

    private final ServerTools plugin;
    private FileConfiguration cfg;

    public DisconnectCMD(ServerTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("disconnect")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        File configFile = new File(plugin.getDataFolder(), "servertools.yml");
        if (!configFile.exists()) {
            plugin.saveResource("servertools.yml", false);
        }

        cfg = YamlConfiguration.loadConfiguration(configFile);

        if (!cfg.getBoolean("disconnect.enabled")) {
            sender.sendMessage("§cThe Disconnect Function Is Not Enabled!");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(Component.text("Disconnecting!"));
        player.kick(Component.text("Disconnected!"));

        return true;
    }
}
