package testplugin.plugin.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class DiscordTokenChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;

    public DiscordTokenChangedEvent(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
