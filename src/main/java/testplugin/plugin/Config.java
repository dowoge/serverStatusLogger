package testplugin.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {

    private static Plugin plugin;

    public Config(Plugin plugin) {
        this.plugin = plugin;
    }
    public static void loadConfig() {

        String webhookURLPath = "webhookURL";

        FileConfiguration config = plugin.getConfig();

        config.addDefault(webhookURLPath,"https://discord.com/api/webhooks/xxx/aaaa-bbbb-ccc");
        config.options().copyDefaults(true);

        plugin.saveConfig();
    }

    public static void setValue(String path, String value) {
        plugin.getConfig().set(path,value);
    }
    public static Object getValue(String path) {
        return plugin.getConfig().get(path);
    }
}
