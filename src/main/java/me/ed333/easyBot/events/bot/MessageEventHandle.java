package me.ed333.easyBot.events.bot;

import me.ed333.easyBot.events.bot.MessageEvent.FriendRecallEvent;
import me.ed333.easyBot.events.bot.MessageEvent.GroupMessageReceiveEvent;
import me.ed333.easyBot.events.bot.MessageEvent.GroupRecallEvent;
import me.ed333.easyBot.events.bot.MessageEvent.TempMessageReceiveEvent;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * <p>检查是否接收到了事件</p>
 * <p>如果接收到了事件就触发对应事件</p>
 */
public class MessageEventHandle {
    public MessageEventHandle(JSONObject event_json) {
        String EventType = event_json.getString("type");
        PluginManager manager = Bukkit.getServer().getPluginManager();
        switch (EventType) {
            case "FriendRecallEvent":
                manager.callEvent(new FriendRecallEvent(event_json));
            case "GroupRecallEvent":
                manager.callEvent(new GroupRecallEvent(event_json));
            case "GroupMessage":
                manager.callEvent(new GroupMessageReceiveEvent(event_json));
            case "TempMessage":
                manager.callEvent(new TempMessageReceiveEvent(event_json));
        }
    }
}
