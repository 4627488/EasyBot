package me.ed333.easyBot.events.bot.GroupEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>全员禁言事件</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class GroupMuteAllEvent extends TriggeredByOperator {
    private final JSONObject json;

    public GroupMuteAllEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * 原本是否全员禁言
     * @return State about mute all
     */
    public Boolean getOrigin_State() {
        return json.getBoolean("origin");
    }

    /**
     * 现在是否全员禁言
     * @return State about mute all
     */
    public Boolean getCurrent_State() {
        return json.getBoolean("current");
    }
}
