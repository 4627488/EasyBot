package me.ed333.easyBot.events.bot.MessageEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 好友消息撤回事件
 */
public class FriendRecallEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final JSONObject json;

    public FriendRecallEvent(JSONObject json) {
        super(true);
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 原消息发送者QQ
     * @return QQ number
     */
    public Long getAuthor_Id() {
        return json.getLong("authorId");
    }

    /**
     * 原消息Id
     * @return Message Id
     */
    public Integer getMsgId() {
        return json.getInt("messageId");
    }

    /**
     * 原消息发送时间
     * @return Message time
     */
    public Integer getMsgTime() {
        return json.getInt("time");
    }
}
