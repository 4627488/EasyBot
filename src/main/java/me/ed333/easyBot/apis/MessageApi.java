package me.ed333.easyBot.apis;

import me.ed333.easyBot.CodeErrException;
import me.ed333.easyBot.ValuePool;
import me.ed333.easyBot.utils.JSON;
import net.sf.json.JSONObject;

/**
 * 提供发送消息、撤回消息的方法
 */
public class MessageApi implements ValuePool {

    /**
     * 发送纯文本群聊消息
     * @param str 文本内容
     * @param quote 是否启用引用
     * @param code 如果启用引用，引用的消息的 MessageId
     * @throws Exception 服务器连接异常
     * @throws CodeErrException 消息发送异常
     */
    public void sendRaw_Group(String str, boolean quote, int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.sendGroupMessage(vars.sessionKey, groupID, quote, code, JSON.jsonBuild.toRaw_MsgChain(str)));
        if (result.getInt("code") != 0) throw new CodeErrException("§3BOT: §c发送纯文本群聊消息失败！ 返回结果: " + result);
    }

    /**
     * 发送纯文本好友消息
     * @param str 文本内容
     * @param quote 是否启用引用
     * @param code 如果启用引用，引用的消息的 MessageId
     * @throws Exception 服务器连接异常
     * @throws CodeErrException 消息发送异常
     */
    public void sendRaw_Friend(String str, boolean quote, int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.sendFriendMessage(vars.sessionKey, groupID, quote, code, JSON.jsonBuild.toRaw_MsgChain(str)));
        if (result.getInt("code") != 0) throw new CodeErrException("§3BOT: §c发送纯文本好友消息失败！ 返回结果: " + result);

    }

    /**
     * 发送纯文本临时消息
     * @param str 文本内容
     * @param qq 临时消息对象的QQ号
     * @param quote 是否启用引用
     * @param code 如果启用引用，引用的消息的 MessageId
     * @throws Exception 服务器连接异常
     * @throws CodeErrException 消息发送异常
     */
    public void sendRaw_Temp(String str, long qq, boolean quote, int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.sendTempMessage(vars.sessionKey, qq, groupID, quote, code, JSON.jsonBuild.toRaw_MsgChain(str)));
        if (result.getInt("code") != 0) throw new CodeErrException("§3BOT: §c发送纯文本临时消息失败！ 返回结果: " + result);
    }

    /**
     * 撤回消息
     * @param code 消息id
     * @throws Exception 服务器连接异常
     * @throws CodeErrException 撤回消息异常
     */
    public void recall(int code) throws Exception {
        JSONObject result = JSONObject.fromObject(utils.recall(vars.sessionKey, code));
        if (result.getInt("code") != 0) throw new CodeErrException("§3BOT: §c撤回消息失败！ 返回结果: " + result);
    }
}
