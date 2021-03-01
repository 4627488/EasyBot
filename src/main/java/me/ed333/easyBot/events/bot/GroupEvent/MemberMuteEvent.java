package me.ed333.easyBot.events.bot.GroupEvent;

import me.ed333.easyBot.events.bot.TriggeredByOperator;
import net.sf.json.JSONObject;


/**
 * <p>群员被禁言事件（该成员不是Bot）</p>
 * <p>有关获取操作者信息的方法</p>
 * @see TriggeredByOperator#getOperatorObj()
 */
public class MemberMuteEvent extends TriggeredByOperator {
    private final JSONObject json;

    public MemberMuteEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    /**
     * 获取禁言时间 （秒）
     * @return 禁言时间
     */
    public Long getDurationSeconds() {
        return json.getLong("durationSeconds");
    }

    private JSONObject getMemberObj() {
        return json.getJSONObject("member");
    }

    /**
     * 获取被禁言群员的QQ
     * @return Muted QQ number
     */
    public Long getMuteMember_Id() {
        return getMemberObj().getLong("id");
    }

    /**
     * 获取被禁言群员的群名片
     * @return Muted member name
     */
    public String getMuteMember_Name() {
        return getMemberObj().getString("memberName");
    }

    /**
     * 获取被禁言群员群中的权限
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     * @return Member permission
     */
    public String getMuteMember_Perm() {
        return getMemberObj().getString("permission");
    }

    private JSONObject getMember_groupObj() {
        return getMemberObj().getJSONObject("group");
    }

    /**
     * 获取事件发生的群号
     * @return groupId
     */
    public Long getGroupId() {
        return getMember_groupObj().getLong("id");
    }

    /**
     * 获取事件发生的群名称
     * @return Group Name
     */
    public String getGroup_Name() {
        return getMember_groupObj().getString("name");
    }
}