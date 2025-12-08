package alexplugins.servertools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StopServerCMD implements CommandExecutor {

    private static final String BYPASS_PERMISSION = "alexplugins.servertools.preventions.stopserverkick";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Bukkit.broadcastMessage(ChatColor.RED + "Server Shutdown");

        new BukkitRunnable() {
            int timer = 10;

            @Override
            public void run() {
                if (timer == 0) {

                    for (Player p : Bukkit.getOnlinePlayers()) {

                        // Skip kicking if player has bypass permission
                        if (p.hasPermission(BYPASS_PERMISSION)) {
                            p.sendMessage(ChatColor.GREEN
                                    + "You were not kicked due to bypass permission.");
                            continue;
                        }

                        p.kickPlayer("Server has been shutdown");
                    }

                    // Stop server after kicks
                    Bukkit.getScheduler().runTask(ServerTools.getInstance(), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    });

                    cancel();
                    return;
                }

                Bukkit.broadcastMessage(ChatColor.AQUA + "Shutting down in " + timer + "...");
                timer--;
            }
        }.runTaskTimer(ServerTools.getInstance(), 0L, 20L);

        return true;
    }
}
