package alexplugins.servertools.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ServerToolsConfig {

    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    public ServerToolsConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        file = new File(plugin.getDataFolder(), "servertools.yml");

        if (!file.exists()) {
            plugin.saveResource("servertools.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
