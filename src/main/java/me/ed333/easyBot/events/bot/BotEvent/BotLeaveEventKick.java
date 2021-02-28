package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.GroupEvent.GroupEvent;
import net.sf.json.JSONObject;

/**
 * Bot 被踢出群事件
 */
public class BotLeaveEventKick extends GroupEvent {
    public BotLeaveEventKick(JSONObject json) {
        super(json);
    }
}
