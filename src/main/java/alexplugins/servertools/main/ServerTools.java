package alexplugins.servertools.main;

import alexplugins.servertools.config.ServerToolsConfig;
import alexplugins.servertools.cmds.*;
import alexplugins.servertools.placeholders.ServerToolsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ServerTools extends JavaPlugin {

    public static final String VERSION = "v0.0.4";
    private static ServerTools instance;

    private FileConfiguration cfg;
    private TimezoneManager timezoneManager;
    private ServerToolsConfig serverToolsConfig;

    @Override
    public void onEnable() {
        serverToolsConfig = new ServerToolsConfig(this);
        instance = this;
        reloadConfig();

        // Create config files if missing
        createConfig();
        createXrayConfig();

        // Load main config
        File configFile = new File(getDataFolder(), "servertools.yml");
        cfg = YamlConfiguration.loadConfiguration(configFile);

        // 🔴 HARD ENABLE CHECK
        if (!cfg.getBoolean("plugin.enabled", true)) {
            getLogger().warning("ServerTools is disabled via config (plugin.enabled=false)");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ServerToolsExpansion(this).register();
            getLogger().info("ServerTools PlaceholderAPI expansion registered");
        }

        Bukkit.getPluginManager().registerEvents(
                new TimezoneJoinListener(this),
                this
        );

        timezoneManager = new TimezoneManager(this);
        getCommand("tz").setExecutor(new timezonesetCMD(this));
        getCommand("tz").setTabCompleter(new timezonesetCMD(this));

        // Load classes
        ServertoolsCMD servertools = new ServertoolsCMD();
        getCommand("disconnect").setExecutor(new DisconnectCMD(this));
        StopServerCMD stopServer = new StopServerCMD();

        // X-Ray
        XRay xray = new XRay(this);
        XrayTab xrayTab = new XrayTab();

        // Register listeners
        getServer().getPluginManager().registerEvents(xray, this);

        // Register commands
        register("xray", new XrayCMD(xray), xrayTab);
        register("servertools", servertools, servertools);
        register("stopserver", stopServer, null);

        getLogger().warning("⚠️ This plugin is a WIP!");
        getLogger().info("ServerTools " + VERSION + " enabled!");
    }


    @Override
    public void onDisable() {
        getLogger().info("ServerTools disabled!");
        getLogger().info("Bye Bye!");
    }

    public ServerToolsConfig getSTConfig() {
        return serverToolsConfig;
    }


    public TimezoneManager getTimezoneManager() {
        return timezoneManager;
    }

    public static ServerTools getInstance() {
        return instance;
    }

    // ----------------------------------------
    // UTIL: Register a command safely
    // ----------------------------------------
    private void register(String name, Object executor, Object tab) {
        if (getCommand(name) == null) {
            getLogger().warning("Command '" + name + "' is missing in plugin.yml!");
            return;
        }

        getCommand(name).setExecutor((sender, command, label, args) ->
                ((CommandExecutor) executor)
                        .onCommand(sender, command, label, args));

        if (tab instanceof TabCompleter completer) {
            getCommand(name).setTabCompleter(completer);
        }
    }

    // ----------------------------------------
    // UTIL: Create xray.yml if missing
    // ----------------------------------------
    private void createXrayConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File xrayFile = new File(getDataFolder(), "xray.yml");
        if (xrayFile.exists()) {
            return; // ✅ absolutely silent if it exists
        }

        try (var in = getResource("xray.yml")) {
            if (in == null) {
                getLogger().severe("xray.yml missing from plugin JAR!");
                return;
            }

            Files.copy(
                    in,
                    xrayFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            getLogger().info("Created default X-Ray configuration.");
        } catch (Exception e) {
            getLogger().severe("Failed to create xray.yml: " + e.getMessage());
        }
    }

    // ----------------------------------------
    // UTIL: Create servertools.yml if missing
    // ----------------------------------------
    private void createConfig() {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        File configFile = new File(getDataFolder(), "servertools.yml");
        if (!configFile.exists()) {
            saveResource("servertools.yml", false);
            getLogger().info("Created configuration file.");
        }
    }
}
