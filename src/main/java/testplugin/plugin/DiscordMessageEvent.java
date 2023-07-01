package testplugin.plugin;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class DiscordMessageEvent extends ListenerAdapter {

    private Plugin plugin;
    private Config config;

    public DiscordMessageEvent(Plugin p,Config cfg) {
        this.plugin = p;
        this.config = cfg;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        String message = event.getMessage().getContentStripped();
        if (event.isFromGuild() && !author.isBot() && event.getGuildChannel().getId().equals(config.getValue("discordRelayChannel").toString())) {
            plugin.getServer().broadcastMessage(ChatColor.BLUE + "[DISCORD] " + ChatColor.GRAY + author.getEffectiveName() + ChatColor.WHITE + ": " + message);
        }
    }
}
