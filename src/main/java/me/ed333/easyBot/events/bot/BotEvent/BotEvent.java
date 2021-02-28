package me.ed333.easyBot.events.bot.BotEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class BotEvent extends Event {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public BotEvent(JSONObject json) {
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }

    /**
     * 获取botQQ
     */
    public Long getQQ() {
        return json.getLong("id");
    }
}
