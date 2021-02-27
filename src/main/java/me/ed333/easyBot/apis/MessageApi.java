package me.ed333.easyBot.apis;

import me.ed333.easyBot.CodeErrExpection;
import me.ed333.easyBot.ValuePool;
import me.ed333.easyBot.utils.JSON;
import net.sf.json.JSONObject;

public class MessageApi implements ValuePool {

    /**
     * 发送纯文本消息
     */
    public void sendRaw_Group(String str, boolean quote, int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.sendGroupMessage(vars.sessionKey, groupID, quote, code, JSON.jsonBuild.toRaw_MsgChain(str)));
        if (result.getInt("code") != 0) throw new CodeErrExpection("§3BOT: §c发送纯文本群聊消息失败！ 返回结果: " + result);
    }

    public void sendRaw_Friend(String str, boolean quote, int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.sendFriendMessage(vars.sessionKey, groupID, quote, code, JSON.jsonBuild.toRaw_MsgChain(str)));
        if (result.getInt("code") != 0) throw new CodeErrExpection("§3BOT: §c发送纯文本好友消息失败！ 返回结果: " + result);

    }

    public void sendRaw_Temp(String str, long qq, boolean quote, int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.sendTempMessage(vars.sessionKey, qq, groupID, quote, code, JSON.jsonBuild.toRaw_MsgChain(str)));
        if (result.getInt("code") != 0) throw new CodeErrExpection("§3BOT: §c发送纯文本临时消息失败！ 返回结果: " + result);
    }

    /**
     * 撤回消息
     * @param code 消息id
     */
    public void recall(int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.recall(vars.sessionKey, code));
        if (result.getInt("code") != 0) throw new CodeErrExpection("§3BOT: §c撤回消息失败！ 返回结果: " + result);
    }
}
