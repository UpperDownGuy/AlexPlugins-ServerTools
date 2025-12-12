package alexplugins.servertools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ServerTools extends JavaPlugin {

    public static final String VERSION = "v0.0.2";
    private static ServerTools instance;

    private FileConfiguration cfg;

    @Override
    public void onEnable() {
        instance = this;

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

        // Load classes
        ServertoolsCMD servertools = new ServertoolsCMD();
        DisconnectCMD disconnect = new DisconnectCMD();
        StopServerCMD stopServer = new StopServerCMD();

        // X-Ray
        XRay xray = new XRay(this);
        XrayTab xrayTab = new XrayTab();

        // Register listeners
        getServer().getPluginManager().registerEvents(xray, this);

        // Register commands
        register("xray", new XrayCMD(xray), xrayTab);
        register("servertools", servertools, servertools);
        register("disconnect", disconnect, null);
        register("stopserver", stopServer, null);

        getLogger().info("ServerTools " + VERSION + " enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerTools disabled!");
        getLogger().info("Bye Bye!");
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
                ((org.bukkit.command.CommandExecutor) executor)
                        .onCommand(sender, command, label, args));

        if (tab instanceof org.bukkit.command.TabCompleter completer) {
            getCommand(name).setTabCompleter(completer);
        }
    }

    // ----------------------------------------
    // UTIL: Create xray.yml if missing
    // ----------------------------------------
    private void createXrayConfig() {
        File folder = new File(getDataFolder(), "ServerTools");
        if (!folder.exists()) folder.mkdirs();

        File xrayFile = new File(folder, "xray.yml");
        if (!xrayFile.exists()) {
            saveResource("xray.yml", false);
            getLogger().info("Created default X-Ray configuration.");
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
