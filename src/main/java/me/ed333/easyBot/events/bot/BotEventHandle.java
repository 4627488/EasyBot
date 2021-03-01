package me.ed333.easyBot.events.bot;

import me.ed333.easyBot.events.bot.BotEvent.*;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * <p>检查是否接收到了事件</p>
 * <p>如果接收到了事件就触发对应事件</p>
 */
public class BotEventHandle {
    public BotEventHandle(JSONObject event_json) {
        String EventType = event_json.getString("type");
        PluginManager manager = Bukkit.getServer().getPluginManager();
        switch (EventType) {
            case"BotOnlineEvent":
                manager.callEvent(new BotOnlineEvent(event_json));
            case "BotOfflineEventActive":
                manager.callEvent(new BotOfflineEventActive(event_json));
            case "BotOfflineEventForce":
                manager.callEvent(new BotOfflineEventForce(event_json));
            case "BotOfflineEventDropped":
                manager.callEvent(new BotOfflineEventDropped(event_json));
            case "BotReloginEvent":
                manager.callEvent(new BotReloginEvent(event_json));
            case "BotGroupPermissionChangeEvent":
                manager.callEvent(new BotGroupPermissionChangeEvent(event_json));
            case "BotMuteEvent":
                manager.callEvent(new BotMuteEvent(event_json));
            case "BotUnmuteEvent":
                manager.callEvent(new BotUnmuteEvent(event_json));
            case "BotJoinGroupEvent":
                manager.callEvent(new BotJoinGroupEvent(event_json));
            case "BotLeaveEventActive":
                manager.callEvent(new BotLeaveEventActive(event_json));
            case "BotLeaveEventKick":
                manager.callEvent(new BotLeaveEventKick(event_json));
        }
    }
}
