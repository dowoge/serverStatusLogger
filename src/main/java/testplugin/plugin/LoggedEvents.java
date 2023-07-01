package testplugin.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class Events implements Listener {

    private final Logger logger;
    private final WebhookEventLogger plugin;
    private String webhookURL;

    public Events(Logger logger, WebhookEventLogger plugin, String webhookURL) {
        this.logger = logger;
        this.plugin = plugin;
        this.webhookURL = webhookURL;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        int playerCount = Bukkit.getOnlinePlayers().size();

        Player player = event.getPlayer();

        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.setContent(player.getName() + " joined the game (" + playerCount + '/' + Bukkit.getMaxPlayers() + ')');

        try {
            Webhook.execute();
        } catch (IOException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        int playerCount = Bukkit.getOnlinePlayers().size() - 1;

        Player player = event.getPlayer();

        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.setContent(player.getName() + " left the game (" + playerCount + '/' + Bukkit.getMaxPlayers() + ')');

        try {
            Webhook.execute();
        } catch (IOException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.setContent('<' + player.getName() + "> " + message.replaceAll("(<[@#]\\S+>|@\\S+|#\\S+)","`$1`"));

        try {
            Webhook.execute();
        } catch (IOException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        String deathMessage = event.getDeathMessage();
        int level = event.getPlayer().getLevel();

        Webhook.setContent(deathMessage + " (had " + level + (level != 1 ? " levels" : " level") + ")");

        try {
            Webhook.execute();
        } catch (IOException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWebhookChanged(WebhookChangedEvent event) {
        this.webhookURL = event.getMessage();
        Config config = new Config(this.plugin);
        logger.warning("webhook changed!");
        config.setValue("webhookURL",this.webhookURL);
        this.plugin.saveConfig();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDiscordTokenChanged(DiscordTokenChangedEvent event) {
        String discordToken = event.getMessage();
        Config config = new Config(this.plugin);
        logger.warning("discord token changed!");
        config.setValue("discordBotToken",discordToken);
        this.plugin.saveConfig();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDiscordRelayChannelIdChanged(DiscordRelayChannelIdChangedEvent event) {
        String discordRelayChannelId = event.getMessage();
        Config config = new Config(this.plugin);
        logger.warning("discord relay channel id changed!");
        config.setValue("discordRelayChannel",discordRelayChannelId);
        this.plugin.saveConfig();
    }
}