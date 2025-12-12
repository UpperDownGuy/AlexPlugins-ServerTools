package alexplugins.servertools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.util.*;

public class XRay implements Listener {

    private final ServerTools plugin;
    private FileConfiguration cfg;

    // player → ore → timestamps
    private final Map<UUID, Map<String, List<Long>>> oreMap = new HashMap<>();

    // Config values
    public static int COAL_LIMIT, IRON_LIMIT, GOLD_LIMIT, COPPER_LIMIT, REDSTONE_LIMIT, LAPIS_LIMIT, EMERALD_LIMIT, DIAMOND_LIMIT, NETHERITE_LIMIT;
    public static int ABSURD_COAL, ABSURD_IRON, ABSURD_GOLD, ABSURD_COPPER, ABSURD_REDSTONE, ABSURD_LAPIS, ABSURD_EMERALD, ABSURD_DIAMOND, ABSURD_NETHERITE;
    public static long TIME_WINDOW;

    public XRay(ServerTools plugin) {
        this.plugin = plugin;
        reloadValues();
    }

    public void reloadValues() {
        File xrayFile = new File(plugin.getDataFolder() + "/xray.yml");
        cfg = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(xrayFile);

        COAL_LIMIT = cfg.getInt("limits.coal", 0);
        IRON_LIMIT = cfg.getInt("limits.iron", 0);
        GOLD_LIMIT = cfg.getInt("limits.gold", 0);
        COPPER_LIMIT = cfg.getInt("limits.copper", 0);
        REDSTONE_LIMIT = cfg.getInt("limits.redstone", 0);
        LAPIS_LIMIT = cfg.getInt("limits.lapis", 0);
        EMERALD_LIMIT = cfg.getInt("limits.emerald", 0);
        DIAMOND_LIMIT = cfg.getInt("limits.diamond", 0);
        NETHERITE_LIMIT = cfg.getInt("limits.netherite", 0);

        ABSURD_COAL = cfg.getInt("absurd.coal", 999999);
        ABSURD_IRON = cfg.getInt("absurd.iron", 999999);
        ABSURD_GOLD = cfg.getInt("absurd.gold", 999999);
        ABSURD_COPPER = cfg.getInt("absurd.copper", 999999);
        ABSURD_REDSTONE = cfg.getInt("absurd.redstone", 999999);
        ABSURD_LAPIS = cfg.getInt("absurd.lapis", 999999);
        ABSURD_EMERALD = cfg.getInt("absurd.emerald", 999999);
        ABSURD_DIAMOND = cfg.getInt("absurd.diamond", 999999);
        ABSURD_NETHERITE = cfg.getInt("absurd.netherite", 999999);

        TIME_WINDOW = cfg.getInt("time-window", 60) * 1000L;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        long now = System.currentTimeMillis();

        if (p.hasPermission("alexplugins.servertools.xray.bypass")) return;

        String ore = getOreType(e.getBlock().getType());
        if (ore == null) return;

        addOre(id, ore, now);

        int count = getCount(id, ore);
        int limit = getLimit(ore);
        int absurdLimit = getAbsurdLimit(ore);

        if (count >= absurdLimit) {
            notifyStaff(ChatColor.RED + "[X-Ray] " + p.getName() + " mined absurd amounts of " + ore + "! (" + count + ")");
            p.kickPlayer(ChatColor.RED + "X-Ray Detected");
            return;
        }

        if (count >= limit) {
            notifyStaff(ChatColor.YELLOW + "[X-Ray] Suspicious mining by " + p.getName() +
                    " (" + ore + ": " + count + ")");
        }
    }

    // ------------------------------------------------------------
    // SAFE LOOKUP HELPERS (ALL NPE PROTECTED)
    // ------------------------------------------------------------

    private void addOre(UUID id, String ore, long time) {
        oreMap.computeIfAbsent(id, k -> new HashMap<>())
                .computeIfAbsent(ore, k -> new ArrayList<>())
                .add(time);
    }

    private int getCount(UUID id, String ore) {
        Map<String, List<Long>> playerMap =
                oreMap.computeIfAbsent(id, k -> new HashMap<>());

        List<Long> list =
                playerMap.computeIfAbsent(ore, k -> new ArrayList<>());

        long cutoff = System.currentTimeMillis() - TIME_WINDOW;

        // Safely remove old entries
        list.removeIf(t -> t < cutoff);

        return list.size();
    }

    private int getLimit(String ore) {
        return switch (ore) {
            case "coal" -> COAL_LIMIT;
            case "iron" -> IRON_LIMIT;
            case "gold" -> GOLD_LIMIT;
            case "copper" -> COPPER_LIMIT;
            case "redstone" -> REDSTONE_LIMIT;
            case "lapis" -> LAPIS_LIMIT;
            case "emerald" -> EMERALD_LIMIT;
            case "diamond" -> DIAMOND_LIMIT;
            case "netherite" -> NETHERITE_LIMIT;
            default -> 999999;
        };
    }

    private int getAbsurdLimit(String ore) {
        return switch (ore) {
            case "coal" -> ABSURD_COAL;
            case "iron" -> ABSURD_IRON;
            case "gold" -> ABSURD_GOLD;
            case "copper" -> ABSURD_COPPER;
            case "redstone" -> ABSURD_REDSTONE;
            case "lapis" -> ABSURD_LAPIS;
            case "emerald" -> ABSURD_EMERALD;
            case "diamond" -> ABSURD_DIAMOND;
            case "netherite" -> ABSURD_NETHERITE;
            default -> 999999;
        };
    }

    private String getOreType(Material type) {
        return switch (type) {
            case COAL_ORE, DEEPSLATE_COAL_ORE -> "coal";
            case IRON_ORE, DEEPSLATE_IRON_ORE -> "iron";
            case GOLD_ORE, DEEPSLATE_GOLD_ORE, NETHER_GOLD_ORE -> "gold";
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> "copper";
            case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> "redstone";
            case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> "lapis";
            case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> "emerald";
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> "diamond";
            case ANCIENT_DEBRIS -> "netherite";
            default -> null;
        };
    }

    private void notifyStaff(String msg) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.isOp() || p.hasPermission("alexplugins.servertools.notify.xray")) {
                p.sendMessage(msg);
            }
        });
        Bukkit.getConsoleSender().sendMessage(msg);
    }
}
