package testplugin.plugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;
import testplugin.plugin.Commands.setDiscordRelayChannelIdCommand;
import testplugin.plugin.Commands.setDiscordTokenCommand;
import testplugin.plugin.Commands.setWebhookCommand;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public final class WebhookEventLogger extends JavaPlugin {
    private String webhookURL;
    private String discordBotToken;

    private JDA jda;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config config = new Config(this);

        config.loadConfig();

        this.webhookURL = config.getValue("webhookURL").toString();

        this.discordBotToken = config.getValue("discordBotToken").toString();
        if (discordBotToken.equals("TOKEN")) {
            getLogger().warning("Token not set, please set it using \"/discordtoken\" and \"/discordrelaychannelid\" ingame do set the necessary information");
        } else {
            getLogger().info("there is a token present");
            this.jda = JDABuilder.createDefault(discordBotToken).enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT).addEventListeners(new DiscordMessageEvent(this, config)).build();
        }

        getServer().getPluginManager().registerEvents(new LoggedEvents(getLogger(), this, webhookURL), this);

        Objects.requireNonNull(getCommand("webhook")).setExecutor(new setWebhookCommand());
        Objects.requireNonNull(getCommand("discordtoken")).setExecutor(new setDiscordTokenCommand());
        Objects.requireNonNull(getCommand("discordrelaychannelid")).setExecutor(new setDiscordRelayChannelIdCommand());


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
        jda.shutdownNow();
        try {
            Webhook.execute();
        } catch (IOException e) {
            getLogger().severe(Arrays.toString(e.getStackTrace()));
        }
    }
}
