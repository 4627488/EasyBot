package me.ed333.easyBot.apis;

/*
想要监听 Bot 的事件， 请设置 config.yml 中的
receive_type 为 event 或 all
 */

import net.sf.json.JSONObject;

public interface Events {
    // bot 登录成功事件
    JSONObject BotOnlineEvent();

    // bot 主动离线事件
    JSONObject BotOfflineEventActive();
}
