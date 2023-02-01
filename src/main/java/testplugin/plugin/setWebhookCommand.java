package testplugin.plugin;


import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class setWebhookCommand implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player | sender instanceof Server && args.length == 1) {
//            Server server = sender.getServer();
//            FileConfiguration config = ;
            String webhookURL = args[0];
            WebhookChangedEvent event = new WebhookChangedEvent(webhookURL);
            Bukkit.getServer().getPluginManager().callEvent(event);
            sender.sendMessage("Set webhook URL to " + webhookURL);
            return true;
        }
        return false;
    }
}
