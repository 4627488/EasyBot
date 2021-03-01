package me.ed333.easyBot.events.bot.MessageEvent;

import me.ed333.easyBot.ValuePool;
import net.md_5.bungee.api.chat.TextComponent;
import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <p>基础消息事件，被继承的类, 不会被触发</p>
 * <p>包含了部分基础方法</p>
 */
class MessageEvent extends Event implements ValuePool {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public MessageEvent(JSONObject json) {
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {return handlers;}


    /**
     * 获取纯文本消息
     * @return Message
     */
    public String getMessage() {
        return jsonParse.getText(json);
    }

    /**
     * 获取复合消息
     * @return Multi text
     */
    public TextComponent getMulti() {
        return jsonParse.getMulti(json);
    }

    /**
     * 获取发送者Id
     * @return Sender id
     */
    public Long getSenderId() {
        return jsonParse.getSenderId(json);
    }

    /**
     * 获取发送者的群名片
     * @return Sender group name
     */
    public String getSender_groupName() {
        return jsonParse.getSender_groupName(json);
    }

    /**
     * 获取群号
     * @return Group id
     */
    public Long getGroupId() {
        return jsonParse.getGroupID(json);
    }

    /**
     * 获取发送者绑定的游戏 Id
     * <p>未绑定的返回 null</p>
     * @return Sender game name
     */
    public String getSender_GameName() {
        return utils.get_gameName(getSenderId());
    }
}
