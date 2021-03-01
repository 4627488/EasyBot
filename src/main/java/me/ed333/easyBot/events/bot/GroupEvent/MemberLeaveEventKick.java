package me.ed333.easyBot.events.bot.GroupEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>群员被踢出事件</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class MemberLeaveEventKick extends TriggeredByOperator {
    private final JSONObject json;
    public MemberLeaveEventKick(JSONObject json) {
        super(json);
        this.json = json;
    }

    private JSONObject getMemberObj() {
        return json.getJSONObject("member");
    }

    /**
     * 被踢出的群员ID
     * @return QQ number
     */
    public Long getMember_Id() {
        return getMemberObj().getLong("id");
    }

    /**
     * 被踢出的群员的名字
     * @return Kicked member name
     */
    public String getMember_Name() {
        return getMemberObj().getString("memberName");
    }
}
