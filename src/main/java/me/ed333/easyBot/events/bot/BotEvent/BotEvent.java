package me.ed333.easyBot.events.bot.BotEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <p>被继承的类</p>
 * <p>该事件不会被触发  仅用于继承</p>
 */
class BotEvent extends Event {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public BotEvent(JSONObject json) {
        super(true);
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }

    /**
     * 获取botQQ
     * @return BotQQ
     */
    public Long getQQ() {
        return json.getLong("id");
    }
}
