package me.ed333.easyBot.events.bot.BotEvent;

import net.sf.json.JSONObject;

/**
 * Bot主动重新登录
 */
public class BotReloginEvent extends BotEvent {
    public BotReloginEvent(JSONObject json) {
        super(json);
    }
}
