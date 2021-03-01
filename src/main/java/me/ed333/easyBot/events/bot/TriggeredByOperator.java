package me.ed333.easyBot.events.bot;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <p>这是一个被继承的的事件</p>
 * <p>有操作者触发的事件都会继承这个类</p>
 * <p>这里返回操作者信息</p>
 * <p>为 null 时为Bot操作</p>
 */
public class TriggeredByOperator extends Event {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public TriggeredByOperator(JSONObject json) {
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    /**
     * 获取操作者的信息
     * <p>返回 null 为 bot 操作</p>
     * @return 操作者信息
     */
    public JSONObject getOperatorObj() {
        return json.getJSONObject("operator");
    }

    /**
     * 获取操作者的 QQ
     * @return Operator QQ Number
     */
    public Long getOperator_QQ() {
        return getOperatorObj().getLong("id");
    }

    /**
     * 获取操作者的群名片
     * @return Operator group name
     */
    public String getOperator_Name() {
        return getOperatorObj().getString("memberName");
    }

    /**
     * 获取操作者群中的权限：
     * <p>OWNER</p>
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     * @return Operator permission
     */
    public String getOperator_Perm() {
        return getOperatorObj().getString("permission");
    }

    private JSONObject getGroupObj() {
        return getOperatorObj().getJSONObject("group");
    }

    /**
     * 事件发生的群号
     * @return Group number
     */
    public Long getGroup_Id() {
        return getGroupObj().getLong("id");
    }

    /**
     * 事件发生的群名
     * @return Group name
     */
    public String getGroup_Name() {
        return getGroupObj().getString("name");
    }
}
