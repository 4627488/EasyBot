package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>BOT 被禁言事件</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class BotMuteEvent extends TriggeredByOperator {
    private final JSONObject json;

    public BotMuteEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * Bot 被禁言的时间 (秒)
     * @return Duration seconds
     */
    public Integer get_DurationSeconds(){
        return json.getInt("durationSeconds");
    }

}
