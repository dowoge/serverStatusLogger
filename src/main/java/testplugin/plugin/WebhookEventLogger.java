package testplugin.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public final class WebhookEventLogger extends JavaPlugin {
    private String webhookURL;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config config = new Config(this);

        config.loadConfig();

        this.webhookURL = config.getValue("webhookURL").toString();

        getServer().getPluginManager().registerEvents(new Events(getLogger(), this, webhookURL), this);

        Objects.requireNonNull(getCommand("webhook")).setExecutor(new setWebhookCommand());


        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Server started.")
                .setColor(new Color(155, 225, 120))
        );
        try {
            Webhook.execute();
        } catch (IOException e) {
            getLogger().severe(Arrays.toString(e.getStackTrace()));
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
            getLogger().severe(Arrays.toString(e.getStackTrace()));
        }
    }
}
