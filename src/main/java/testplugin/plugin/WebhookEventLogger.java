package testplugin.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public final class WebhookEventLogger extends JavaPlugin {
    private String webhookURL;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config config = new Config(this);

        config.loadConfig();

        getLogger().info("Starting");

        this.webhookURL = config.getValue("webhookURL").toString();

        getServer().getPluginManager().registerEvents(new Events(getLogger(), this, webhookURL), this);

        getCommand("webhook").setExecutor(new setWebhookCommand());


        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Server started.")
                .setColor(new Color(155, 225, 120))
        );
        try {
            Webhook.execute();
        } catch (IOException e) {
            getLogger().severe(e.getStackTrace().toString());
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Server stopped.")
                .setColor(new Color(220, 75, 40))
        );
        try {
            Webhook.execute();
        } catch (IOException e) {
            getLogger().severe(e.getStackTrace().toString());
        }
    }
}
