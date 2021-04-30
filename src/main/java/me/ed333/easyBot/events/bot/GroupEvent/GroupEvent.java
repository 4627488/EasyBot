package me.ed333.easyBot.events.bot.GroupEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <p>被继承的事件类</p>
 * <p>该事件不会被触发</p>
 */
public class GroupEvent extends Event {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public GroupEvent(JSONObject json) {
        super(true);
        this.json = json;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    public JSONObject getGroupObj() {
        return json.getJSONObject("group");
    }

    /**
     * 群号
     * @return Group name
     */
    public Long getGroup_Id() {
        return getGroupObj().getLong("id");
    }

    /**
     * 群名
     * @return Group name
     */
    public String getGroup_Name() {
        return getGroupObj().getString("name");
    }

    /**
     * Bot 在群中的权限
     * @return Bot permission in group
     */
    public String getGroup_Perm() {
        return getGroupObj().getString("permission");
    }
}
