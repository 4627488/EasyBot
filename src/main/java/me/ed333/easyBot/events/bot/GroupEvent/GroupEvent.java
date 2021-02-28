package me.ed333.easyBot.events.bot.GroupEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupEvent extends Event {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public GroupEvent(JSONObject json) {
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
     */
    public Long getGroup_Id() {
        return getGroupObj().getLong("id");
    }

    /**
     * 群名
     */
    public String getGroup_Name() {
        return getGroupObj().getString("name");
    }

    /**
     * Bot 在群中的权限
     */
    public String getGroup_Perm() {
        return getGroupObj().getString("permission");
    }
}
