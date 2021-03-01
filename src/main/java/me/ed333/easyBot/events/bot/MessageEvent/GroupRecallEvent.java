package me.ed333.easyBot.events.bot.MessageEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>群消息撤回事件</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class GroupRecallEvent extends TriggeredByOperator {
    private final JSONObject json;

    public GroupRecallEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * 原消息发送者 Id
     * @return qq number
     */
    public Long getAuthor_Id() {
        return json.getLong("authorId");
    }

    /**
     * 原消息 messageId
     * @return message Id
     */
    public Integer getMsgId() {
        return json.getInt("messageId");
    }

    /**
     * 原消息发送的时间
     * @return Message time
     */
    public Integer getMsgTime() {
        return json.getInt("time");
    }
}
