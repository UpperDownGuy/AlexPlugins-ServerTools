package alexplugins.servertools.main;

import alexplugins.servertools.main.ServerTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimezoneJoinListener implements Listener {

    private final ServerTools plugin;
    private final Map<UUID, BukkitTask> reminders = new HashMap<>();

    public TimezoneJoinListener(ServerTools plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!plugin.getSTConfig().getBoolean("timezones.enabled")) return;
        if (!plugin.getSTConfig().getBoolean("timezones.requestonjoin")) return;
        if (plugin.getTimezoneManager().hasTimezone(p.getUniqueId())) return;

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {
                    if (!p.isOnline()) return;
                    if (plugin.getTimezoneManager().hasTimezone(p.getUniqueId())) return;

                    p.sendMessage("§ePlease set your timezone using §6/tz set");
                    p.sendMessage("§7Find your timezone at §bhttps://tz.alexxweb.com");
                },
                20L,      // 1 second delay
                160L      // every 5 seconds
        );

        reminders.put(p.getUniqueId(), task);
    }

    public void stopReminder(UUID uuid) {
        BukkitTask task = reminders.remove(uuid);
        if (task != null) task.cancel();
    }
}
