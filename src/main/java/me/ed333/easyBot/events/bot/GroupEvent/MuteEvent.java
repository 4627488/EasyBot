package me.ed333.easyBot.events.bot.GroupEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MuteEvent extends Event {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public MuteEvent(JSONObject json) {
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    /**
     * 获取操作者的信息
     * <p>返回 null 为 bot 操作</p>
     */
    public JSONObject getOperatorObj() {
        return json.getJSONObject("operator");
    }

    /**
     * 获取操作者的 QQ
     */
    public Long getOperator_QQ() {
        return getOperatorObj().getLong("id");
    }

    /**
     * 获取操作者的群名片
     */
    public String getOperator_Name() {
        return getOperatorObj().getString("memberName");
    }

    /**
     * 获取操作者群中的权限：
     * <p>OWNER</p>
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     */
    public String getOperator_Perm() {
        return getOperatorObj().getString("permission");
    }

    private JSONObject getGroupObj() {
        return getOperatorObj().getJSONObject("group");
    }

    /**
     * 事件发生的群号
     */
    public Long getGroup_Id() {
        return getGroupObj().getLong("id");
    }

    /**
     * 事件发生的群名
     */
    public String getGroup_Name() {
        return getGroupObj().getString("name");
    }

    /**
     * Bot 群中的权限
     */
    public String getBot_Perm() {
        return getGroupObj().getString("permission");
    }
}
