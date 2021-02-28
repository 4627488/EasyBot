package me.ed333.easyBot.events.bot.BotEvent;

import net.sf.json.JSONObject;

/**
 * BOT 主动离线事件
 */
public class BotOfflineEventActive extends BotEvent {
    public BotOfflineEventActive(JSONObject json) {
        super(json);
    }
}
