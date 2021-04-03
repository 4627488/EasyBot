package me.ed333.easyBot.events.bot;

import me.ed333.easyBot.events.bot.GroupEvent.*;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * <p>检查是否接收到了事件</p>
 * <p>如果接收到了事件就触发对应事件</p>
 */
public class GroupEventHandle {
    public GroupEventHandle(JSONObject event_json) {
        String EventType = event_json.getString("type");
        PluginManager manager = Bukkit.getServer().getPluginManager();
        switch (EventType) {
            case "MemberMuteEvent" :
                manager.callEvent(new MemberMuteEvent(event_json));
            case "MemberUnmuteEvent":
                manager.callEvent(new MemberUnmuteEvent(event_json));
            case "GroupNameChangeEvent" :
                manager.callEvent(new GroupNameChangeEvent(event_json));
            case "GroupEntranceAnnouncementChangeEvent":
                manager.callEvent(new GroupEntranceAnnouncementChangeEvent(event_json));
            case "GroupMuteAllEvent" :
                manager.callEvent(new GroupMuteAllEvent(event_json));
            case "MemberLeaveEventKick":
                manager.callEvent(new MemberLeaveEventKick(event_json));
            case "MemberLeaveEventQuit":
                manager.callEvent(new MemberLeaveEventQuit(event_json));
        }
    }
}
