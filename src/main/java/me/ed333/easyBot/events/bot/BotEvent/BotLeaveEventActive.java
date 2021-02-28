package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.GroupEvent.GroupEvent;
import net.sf.json.JSONObject;

/**
 * Bot 主动离开一个群
 */
public class BotLeaveEventActive extends GroupEvent {
    public BotLeaveEventActive(JSONObject json) {
        super(json);
    }
}
