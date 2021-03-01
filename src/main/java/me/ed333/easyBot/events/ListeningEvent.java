package me.ed333.easyBot.events;

import me.ed333.easyBot.ValuePool;
import me.ed333.easyBot.events.bot.BotEvent.BotMuteEvent;
import me.ed333.easyBot.events.bot.MessageEvent.GroupMessageReceiveEvent;
import me.ed333.easyBot.events.bot.MessageEvent.TempMessageReceiveEvent;
import me.ed333.easyBot.utils.Bot;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

/**
 * 插件默认监听的一些事件
 */
public class ListeningEvent implements ValuePool, Listener {

    /*
    游戏中的事件
     */
    @EventHandler
    private void onJoin(@NotNull PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (!enabled_Bot_Player.contains(p)) {
            if (vars.PlayerData.getBoolean(p.getUniqueId() + ".enable_Bot")) {
                enabled_Bot_Player.add(p);
            }else if (!utils.name_isBound(p.getName())) {
                vars.PlayerData.set(p.getUniqueId() + ".enable_Bot", true);
                enabled_Bot_Player.add(p);
            }
        }

        vars.PlayerData.set(p.getUniqueId() + ".lastName", p.getName());
        sender.sendMessage(enabled_Bot_Player.toString());
    }

    @EventHandler
    private void onLeave(@NotNull PlayerQuitEvent event) {
        enabled_Bot_Player.remove(event.getPlayer());
    }

    @EventHandler
    private void onChat(@NotNull AsyncPlayerChatEvent event) throws Exception {
        String message = event.getMessage();
        if (bot.isConnected)
            utils.sendGroupMessage(
                    ValuePool.vars.sessionKey,
                    groupID,
                    false,
                    0,
                    new JSONArray().element(
                            new JSONObject().element("type", "Plain")
                                    .element("text", event.getPlayer().getName() + ": " + message)
                    ));
    }

    @EventHandler
    private void onGroupMessage(GroupMessageReceiveEvent event) {
        if (event.getGroupId().equals(groupID)) {
            String catchType = vars.Config.getString("catch.type");
            for (Player p : enabled_Bot_Player) {
                if (catchType.equals("text") && vars.Config.getBoolean("catch.text")) {
                    p.sendMessage(event.getMessage());
                } else if (catchType.equals("multi") && (catch_at || catch_img || catch_text)) {
                    p.spigot().sendMessage(event.getMulti());
                }
            }
        }
    }

    @EventHandler
    private void onTempMessage(TempMessageReceiveEvent event) {
        Long senderId = event.getSenderId();
        if (vars.verify.containsValue(senderId)) {
            vars.Bound_data.set("QQ_Bound." + senderId, getKey(vars.verify, senderId));
            vars.Bound_data.set("Name_Bound." + getKey(vars.verify, senderId), senderId);
            vars.verify.remove(getKey(vars.verify, senderId));
        }
    }


    private String getKey(HashMap<String, Long> map, Object value) {
        String key = "NOT FIND";
        Set<String> keySet = map.keySet();
        for (String o : keySet) {
            if (map.get(o).equals(value)) key = o;
        }
        return key;
    }

}
