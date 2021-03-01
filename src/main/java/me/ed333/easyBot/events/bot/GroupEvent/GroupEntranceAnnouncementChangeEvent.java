package me.ed333.easyBot.events.bot.GroupEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>某群入群公告改变</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class GroupEntranceAnnouncementChangeEvent extends TriggeredByOperator {
    private final JSONObject json;

    public GroupEntranceAnnouncementChangeEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * 原有的公告
     * @return Origin announcement
     */
    public String getOrigin_Announcement() {
        return json.getString("origin");
    }

    /**
     * 新的公告
     * @return Current announcement
     */
    public String getCurrent_Announcement() {
        return json.getString("current");
    }
}
