package alexplugins.servertools.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.UUID;

public class TimezoneManager {

    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration data;

    public TimezoneManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        file = new File(plugin.getDataFolder(), "timezones.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasTimezone(UUID uuid) {
        return data.contains("players." + uuid);
    }

    public ZoneId getPlayerZone(UUID uuid) {
        String id = data.getString("players." + uuid);
        if (id == null) return null;
        return ZoneId.of(id);
    }

    public void setTimezone(UUID uuid, ZoneId zone) {
        data.set("players." + uuid, zone.getId());
        save();
    }

    public void removeTimezone(UUID uuid) {
        data.set("players." + uuid, null);
        save();
    }
}
