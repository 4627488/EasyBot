package me.ed333.easyBot.events.bot.GroupEvent;

import net.sf.json.JSONObject;
import org.bukkit.event.HandlerList;


/**
 * 群员被禁言事件
 * <p></p>
 * <p>获取操作者信息的方法</p>
 * 返回 null 时为 Bot 操作
 * @see MuteEvent#getOperatorObj()
 */
public class MemberMuteEvent extends MuteEvent {
    private final JSONObject json;
    private static final HandlerList handlers = new HandlerList();

    public MemberMuteEvent(JSONObject json) {
        super(json);
        this.json = json;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }

    /**
     * 获取禁言时间 （秒）
     */
    public Long getDurationSeconds() {
        return json.getLong("durationSeconds");
    }

    private JSONObject getMemberObj() {
        return json.getJSONObject("member");
    }

    /**
     * 获取被禁言群员的QQ
     */
    public Long getMuteMember_Id() {
        return getMemberObj().getLong("id");
    }

    /**
     * 获取被禁言群员的群名片
     */
    public String getMuteMember_Name() {
        return getMemberObj().getString("name");
    }

    /**
     * 获取被禁言群员群中的权限
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     */
    public String getMuteMember_Perm() {
        return getMemberObj().getString("permission");
    }

    private JSONObject getMember_groupObj() {
        return getMemberObj().getJSONObject("group");
    }

    /**
     * 获取事件发生的群号
     */
    public Long getGroupId() {
        return getMember_groupObj().getLong("id");
    }

    /**
     * 获取事件发生的群名称
     */
    public String getGroup_Name() {
        return getMember_groupObj().getString("name");
    }

    /**
     * 获取 Bot 在群中的权限
     * <p>ADMINISTRATOR</p>
     * <p>MEMBER</p>
     */
    public String getBot_Perm() {
        return getMember_groupObj().getString("permission");
    }
}