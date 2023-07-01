package testplugin.plugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import testplugin.plugin.Events.DiscordRelayChannelIdChangedEvent;

public class setDiscordRelayChannelIdCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player | sender instanceof Server && args.length == 1) {
            String discordChannelId = args[0];
            DiscordRelayChannelIdChangedEvent event = new DiscordRelayChannelIdChangedEvent(discordChannelId);
            Bukkit.getServer().getPluginManager().callEvent(event);
            sender.sendMessage("Set discord relay channel to " + discordChannelId);
            return true;
        }
        return false;
    }
}