package testplugin.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Config {

    private static Plugin plugin;

    public Config(Plugin plugin) {
        Config.plugin = plugin;
    }
    public void loadConfig() {

        Dictionary<String, String> configs = new Hashtable<>();

        configs.put("webhookURL","https://discord.com/api/webhooks/xxx/aaaa-bbbb-ccc");

        configs.put("discordBotToken","TOKEN");

        configs.put("discordRelayChannel","SnowflakeID");

        FileConfiguration config = plugin.getConfig();



        Enumeration<String> configIterator = configs.keys();
        while (configIterator.hasMoreElements()) {
            String configPath = configIterator.nextElement();
            String configDefault = configs.get(configPath);
            config.addDefault(configPath,configDefault);
        }
        config.options().copyDefaults(true);

        plugin.saveConfig();
    }

    public void setValue(String path, String value) {
        plugin.getConfig().set(path,value);
    }
    public Object getValue(String path) {
        return plugin.getConfig().get(path);
    }
}
