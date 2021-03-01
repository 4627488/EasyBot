package me.ed333.easyBot.events.bot.GroupEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;

/**
 * <p>群成员被解除禁言事件（该成员不是Bot）</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class MemberUnmuteEvent extends TriggeredByOperator {
    private final JSONObject json;
    public MemberUnmuteEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    private JSONObject getMemberObj() {
        return json.getJSONObject("member");
    }

    /**
     * 被禁言成员的QQ
     * @return Member's QQ number
     */
    public Long getMember_Id() {
        return getMemberObj().getLong("id");
    }

    /**
     * 被禁言成员的名字
     * @return Member group name
     */
    public String getMember_Name() {
        return getMemberObj().getString("name");
    }
}
