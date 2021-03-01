package me.ed333.easyBot.events.bot.MessageEvent;

import net.sf.json.JSONObject;

/**
 * 临时消息事件
 */
public class TempMessageReceiveEvent extends MessageEvent{
    public TempMessageReceiveEvent(JSONObject json) {
        super(json);
    }
}
