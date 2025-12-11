package alexplugins.servertools;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ServerTools extends JavaPlugin {

    public static final String VERSION = "v0.0.2";

    private static ServerTools instance;

    @Override
    public void onEnable() {
        instance = this;

        // Create config folder + xray.yml if missing
        createXrayConfig();

        // Load classes
        ServertoolsCMD servertools = new ServertoolsCMD();
        DisconnectCMD disconnect = new DisconnectCMD();
        StopServerCMD stopServer = new StopServerCMD();

        // X-Ray detector, TabCompleter, and Commands
        XRay xray = new XRay(this);
        XrayTab xrayTab = new XrayTab();

        // Register listeners
        getServer().getPluginManager().registerEvents(xray, this);

        // Register commands & tab completers
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

        getCommand(name).setExecutor((commandSender, command, label, args) ->
                ((org.bukkit.command.CommandExecutor) executor)
                        .onCommand(commandSender, command, label, args));

        if (tab instanceof org.bukkit.command.TabCompleter completer) {
            getCommand(name).setTabCompleter(completer);
        }
    }

    // ----------------------------------------
    // UTIL: Create xray.yml if missing
    // ----------------------------------------
    private void createXrayConfig() {
        File folder = new File(getDataFolder(), "ServerTools");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File xrayFile = new File(folder, "xray.yml");

        if (!xrayFile.exists()) {
            saveResource("ServerTools/xray.yml", false);
            getLogger().info("Created default X-Ray configuration.");
        }
    }
}
