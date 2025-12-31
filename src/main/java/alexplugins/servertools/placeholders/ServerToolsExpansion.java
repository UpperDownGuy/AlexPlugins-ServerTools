package alexplugins.servertools.placeholders;

import alexplugins.servertools.main.ServerTools;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ServerToolsExpansion extends PlaceholderExpansion {

    private final ServerTools plugin;

    public ServerToolsExpansion(ServerTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "servertools";
    }

    @Override
    public @NotNull String getAuthor() {
        return "AlexPlugins";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        if (!params.equalsIgnoreCase("time")) return null;

        if (!plugin.getSTConfig().getBoolean("timezones.enabled")) {
            return formatTime(
                    ZoneId.of(plugin.getSTConfig().getString("timezones.default")
                    )
            );
        }

        ZoneId zone = null;

        if (player != null) {
            zone = plugin.getTimezoneManager().getPlayerZone(player.getUniqueId());
        }

        if (zone == null) {
            zone = ZoneId.of(plugin.getSTConfig().getString("timezones.default"));
        }

        return formatTime(zone);
    }

    private String formatTime(ZoneId zone) {
        ZonedDateTime now = ZonedDateTime.now(zone);

        String time = now.format(DateTimeFormatter.ofPattern("h:mm a"));
        String abbr = now.format(DateTimeFormatter.ofPattern("z"));

        if (abbr.length() > 3) {
            abbr = abbr.substring(0, 3);
        }

        return time + " [" + abbr.toUpperCase() + "]";
    }
}
