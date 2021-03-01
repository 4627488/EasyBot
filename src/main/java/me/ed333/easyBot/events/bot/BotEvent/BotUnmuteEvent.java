package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * Bot 被解除禁言
 */
public class BotUnmuteEvent extends TriggeredByOperator {
    public BotUnmuteEvent(JSONObject json) {
        super(json);
    }
}
