package me.ed333.easyBot.events.bot.BotEvent;

import net.sf.json.JSONObject;

/**
 * Bot被挤下线
 */
public class BotOfflineEventForce extends BotEvent {
    public BotOfflineEventForce(JSONObject json) {
        super(json);
    }
}
