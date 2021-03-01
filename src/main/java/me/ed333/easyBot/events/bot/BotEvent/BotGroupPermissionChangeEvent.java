package me.ed333.easyBot.events.bot.BotEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * Bot 权限在群中改变事件
 */
public class BotGroupPermissionChangeEvent extends TriggeredByOperator {
    private final JSONObject json;

    public BotGroupPermissionChangeEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * 获取 Bot 的原权限
     * <p>OWNER</p>
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     * @return Bot origin permission
     */
    public String get_OriginPerm() {
        return json.getString("origin");
    }

    /**
     * 获取 Bot 的新权限
     * <p>OWNER</p>
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     * @return Bot new permission
     */
    public String get_Current() {
        return json.getString("current");
    }
}
