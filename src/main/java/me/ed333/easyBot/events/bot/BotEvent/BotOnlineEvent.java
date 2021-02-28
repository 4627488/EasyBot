package me.ed333.easyBot.events.bot.BotEvent;

import net.sf.json.JSONObject;

/**
 * BOT 登录成功事件
 */
public class BotOnlineEvent extends BotEvent {
    public BotOnlineEvent(JSONObject json) {
        super(json);
    }
}
