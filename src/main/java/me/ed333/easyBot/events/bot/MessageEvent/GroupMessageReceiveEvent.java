package me.ed333.easyBot.events.bot.MessageEvent;

import net.sf.json.JSONObject;

/**
 * 收到了群消息事件
 * @see GroupMessageEvent
 */
public class GroupMessageReceiveEvent extends GroupMessageEvent {
    public GroupMessageReceiveEvent(JSONObject json) {
        super(json);
    }
}
