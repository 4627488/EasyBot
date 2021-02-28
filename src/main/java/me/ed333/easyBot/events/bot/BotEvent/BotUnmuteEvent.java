package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.GroupEvent.MuteEvent;
import net.sf.json.JSONObject;

/**
 * Bot 被解除禁言
 */
public class BotUnmuteEvent extends MuteEvent {
    public BotUnmuteEvent(JSONObject json) {
        super(json);
    }
}
