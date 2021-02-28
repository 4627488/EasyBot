package me.ed333.easyBot.events.bot.BotEvent;


import me.ed333.easyBot.events.bot.GroupEvent.GroupEvent;
import net.sf.json.JSONObject;

/**
 * Bot 加入了一个新的群事件
 */
public class BotJoinGroupEvent extends GroupEvent {
    public BotJoinGroupEvent(JSONObject json) {
        super(json);
    }
}
