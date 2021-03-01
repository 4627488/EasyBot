package me.ed333.easyBot.events.bot.GroupEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>群名称改变事件</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class GroupNameChangeEvent extends TriggeredByOperator {
    private final JSONObject json;
    public GroupNameChangeEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * 原群名
     * @return Origin name
     */
    public String getOrigin_Name() {
        return json.getString("origin");
    }

    /**
     * 新群名
     * @return Current name
     */
    public String getCurrent_Name() {
        return json.getString("current");
    }
}
