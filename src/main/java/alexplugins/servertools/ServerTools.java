package alexplugins.servertools;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerTools extends JavaPlugin {
    String version = "v0.0.1";

    private static ServerTools instance;

    @Override
    public void onEnable() {
        instance = this;  // <<==== THIS WAS MISSING (critical)

        ServertoolsCMD Server = new ServertoolsCMD();
        DisconnectCMD Disconnect = new DisconnectCMD();
        StopServerCMD StopServer = new StopServerCMD();

        getCommand("servertools").setExecutor(Server);
        getCommand("servertools").setTabCompleter(Server);
        getCommand("disconnect").setExecutor(Disconnect);
        getCommand("stopserver").setExecutor(StopServer);

        getLogger().info("ServerTools enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerTools disabled!");
        getLogger().info("Bye Bye!");
    }

    public static ServerTools getInstance() {
        return instance;
    }
}
