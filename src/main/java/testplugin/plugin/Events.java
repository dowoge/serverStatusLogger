package testplugin.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Logger;

public class Events implements Listener {

    private Logger logger;
    private WebhookEventLogger plugin;
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
            logger.severe(e.getStackTrace().toString());
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
            logger.severe(e.getStackTrace().toString());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        DiscordWebhook Webhook = new DiscordWebhook(webhookURL);

        Webhook.setContent('<' + player.getName() + "> " + message.replaceAll("(@everyone|@here)", "`$&`"));

        try {
            Webhook.execute();
        } catch (IOException e) {
            logger.severe(e.getStackTrace().toString());
        }
    }

    public static int getLvlForXP(int xp) {
        if (xp <= 255) {
            return xp  / 17;
        } else if (xp > 272 && xp < 887) {
            return (int) ((Math.sqrt(24 * xp - 5159) + 59) / 6);
        } else if (xp > 825) {
            return (int) ((Math.sqrt(56 * xp - 32511) + 303) / 14);
        }
        return 0;
    }

    public static int toLevel(int xp) {
        if (xp <= 352) {
            return (int) Math.round(Math.sqrt(xp+9)-3);
        } else if (xp >= 394 && xp <= 1507) {
            return (int) Math.round((Math.sqrt(40*xp-7839)+81)*0.1);
        } else if (xp >= 1628) {
            return (int) Math.round((Math.sqrt(72*xp-54215)+325)/18);
        }
        return 0;
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
            logger.severe(e.getStackTrace().toString());
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
}