package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.GroupEvent.MuteEvent;
import net.sf.json.JSONObject;

/**
 * BOT 被禁言事件
 * <p></p>
 * <p>获取操作者信息的方法</p>
 * @see MuteEvent#getOperatorObj()
 */
public class BotMuteEvent extends MuteEvent {
    private final JSONObject json;

    public BotMuteEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * Bot 被禁言的时间
     * @return
     */
    public Integer get_DurationSeconds(){
        return json.getInt("durationSeconds");
    }

}
