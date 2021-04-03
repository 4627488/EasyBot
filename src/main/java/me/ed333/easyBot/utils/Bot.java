package me.ed333.easyBot.utils;

import me.ed333.easyBot.CodeErrException;
import me.ed333.easyBot.bukkit.BukkitMain;
import me.ed333.easyBot.bukkit.ValuePool;
import me.ed333.easyBot.events.bot.BotEventHandle;
import me.ed333.easyBot.events.bot.GroupEventHandle;
import me.ed333.easyBot.events.bot.MessageEventHandle;
import net.md_5.bungee.api.chat.*;
import net.sf.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static me.ed333.easyBot.utils.Messages.getMsg;
import static me.ed333.easyBot.utils.Messages.get_unBoundTXT;
import static me.ed333.easyBot.utils.HttpRequest.*;

public class Bot implements ValuePool {
    private socketClient client;
    public boolean isConnected;
    
    /**
     * 连接 SocketServer 监听 事件/消息
     * @param sessionKey session
     * @throws URISyntaxException 链接不存在或链接错误
     */
    public void connect(String sessionKey) throws URISyntaxException {
        sender.sendMessage("§3BOT: §a链接服务器中...");

        URI uri = new URI("ws://" + this.url + "/" + vars.Config.getString("receive_type") + "?sessionKey=" + sessionKey);
        client = new socketClient(uri);
        client.connect();
    }

    /**
     * 关闭连接
     * @throws Exception 请求异常时抛出
     */
    public void closeSocket() throws Exception {
        client.close();
        sender.sendMessage("§3BOT: §a释放session...");
        JSONObject result = JSONObject.fromObject(bot.release_Session(vars.sessionKey));
        if (result.getInt("code") == 0) sender.sendMessage("§3BOT: §a释放完成. result: " + result);
        else throw new CodeErrException("§3BOT: §c释放失败！服务器返回结果: " + result);
    }

    /**
     * 验证身份
     * @return result
     * @throws Exception 请求异常时抛出
     */
    public String auth() throws Exception {
        sender.sendMessage("§3BOT: §a注册bot...");
        return doPost("http://" + this.url + "/auth", new JSONObject().element("authKey", authKey));
    }

    /**
     * 校验 Session 并将 Session 绑定到BotQQ
     * @param SessionKey session
     * @return result String
     * @throws Exception 请求异常时抛出
     */
    public String verify(String SessionKey) throws Exception {
        sender.sendMessage("§3BOT: §a绑定BOT...");
        return doPost("http://" + this.url + "/verify", new JSONObject().element("sessionKey", SessionKey).element("qq", botID));
    }

    /**
     * 在卸载插件时释放 bot 绑定的session
     * @param sessionKey sessionKey
     * @return result
     * @throws Exception 请求异常时抛出
     */
    protected String release_Session(String sessionKey) throws Exception {
        JSONObject request = new JSONObject().element("sessionKey", sessionKey)
                .element("qq", botID);
        return doPost("http://" + url + "/release", request);
    }

    protected static class socketClient extends WebSocketClient implements ValuePool {

        public socketClient(URI serverUri) { super(serverUri); }

        @Override
        public void onOpen(@NotNull ServerHandshake handshake) {
            bot.isConnected = this.isOpen();
            sender.sendMessage(bot.isConnected + "");
            sender.sendMessage("§3BOT: §a连接成功！ 状态: " + handshake.getHttpStatusMessage() + " | " + handshake.getHttpStatus());
        }

        @Override
        public void onMessage(String message) {
                BukkitMain.INSTANCE.printDEBUG(message);
                JSONObject msg_json = JSONObject.fromObject(message);
                vars.msg_Json = msg_json;

                new BotEventHandle(msg_json);
                new GroupEventHandle(msg_json);
                new MessageEventHandle(msg_json);
            // Bukkit.getPluginManager().callEvent(new GroupMessageReceiveEvent(msg_json));
/*  旧的方法

            if (jsonParse.getMsgType(msg_json).equals("GroupMessage")) {
                if (jsonParse.getGroupID(msg_json).equals(groupID)) {

                    String catchType = vars.Config.getString("catch.type");

                    for (Player p : enabled_Bot_Player) {
                        if (catchType.equals("text") && vars.Config.getBoolean("catch.text") && !jsonParse.getText(msg_json).equals("")) {
                            p.sendMessage(jsonParse.getText(msg_json));
                        } else if (catchType.equals("multi") && (catch_at || catch_img || catch_text)) {
                            p.spigot().sendMessage(jsonParse.getMulti(msg_json));
                        }
                    }
                }
            } else if (jsonParse.getMsgType(msg_json).equals("TempMessage")) {
                if (vars.verify.containsValue(jsonParse.getSenderId(msg_json))) {
                    vars.Bound_data.set("QQ_Bound." + jsonParse.getSenderId(msg_json), getKey(vars.verify, jsonParse.getSenderId(msg_json)));
                    vars.Bound_data.set("Name_Bound." + getKey(vars.verify, jsonParse.getSenderId(msg_json)), jsonParse.getSenderId(msg_json));
                    vars.verify.remove(getKey(vars.verify, jsonParse.getSenderId(msg_json)));
                }
            }
*/

        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            sender.sendMessage("§3BOT: §a从服务器断开连接!");
        }

        @Override
        public void onError(@NotNull Exception ex) {
            sender.sendMessage("§3BOT: §c出错了！原因: " + ex.getCause());
            ex.printStackTrace();
        }
    }

    public static class Utils {
        /**
         * 发送好友消息
         * @param target 好友ID
         * @param quote 启用引用
         * @param code 如果引用启用， msg 的 SourceID
         * @param msgChain 要发送的消息
         * @return result
         * @throws Exception 请求异常时抛出
         */
        public String sendFriendMessage(long target, boolean quote, int code, MessageChain msgChain) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("target", target)
                    .element("messageChain", msgChain.toString());
            if (quote) request.element("quote", code);
            return doPost("http://" + url + "/sendFriendMessage", request);
        }

        /**
         * 通过群给某人发送临时消息
         * @param qq 临时消息对象的QQ
         * @param groupID 临时消息的群号
         * @param quote 启用引用
         * @param code 如果启用引用, 引用的Message id
         * @param msgChain 消息链
         * @return result
         * @throws Exception 请求异常时抛出
         */
        public String sendTempMessage(long qq, long groupID, boolean quote, int code, MessageChain msgChain) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("qq", qq)
                    .element("group", groupID)
                    .element("messageChain",msgChain.toString());
            if (quote) request.element("quote", code);
            return doPost("http://" + url + "/sendTempMessage", request);
        }

        /**
         * 发送群聊消息
         * @param groupID 群号
         * @param quote 启用引用
         * @param code 如果启用引用, 引用的 Message id
         * @param msgChain 消息链
         * @return result
         * @throws Exception 请求异常时抛出
         */
        public String sendGroupMessage(long groupID, boolean quote, int code, MessageChain msgChain) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("target", groupID)
                    .element("messageChain", msgChain.toString());
            if (quote) request.element("quote", code);
            System.out.println(request);
            return doPost("http://" + url + "/sendGroupMessage", request);
        }

        /**
         * 获取群员的信息
         * @param memberID 群员的QQ
         * @return result
         */
        public @NotNull String getMemberInfo(long memberID) {
            return doGet("http://" + url + "/memberInfo", "sessionKey=" + vars.sessionKey + "&target=" + groupID + "&memberId=" + memberID);
        }


        /**
         * 撤回消息
         * @param SourceID 消息的ID
         * @return result
         * @throws Exception 请求异常时抛出
         */
        public String recall(int SourceID) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("target", SourceID);
            return doPost("http://" + url + "/recall", request);
        }

        /**
         * 禁言群员
         * @param memberID 群员ID
         * @param time 时间 单位: 秒
         * @return result
         */
        public String mute(long groupID, long memberID, int time) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("target", groupID)
                    .element("memberID", memberID)
                    .element("time", time);
            return doPost("http://" + url + "/mute", request);
        }

        /**
         * 解除禁言
         */
        public String unmute(long groupID, long memberID) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("target", groupID)
                    .element("memberID", memberID);
            return doPost("http://" + url + "/unmute", request);
        }

        /**
         * 踢出群员
         * @param reason 原因
         * @return result
         */
        public String kick(long groupID, long memberID, String reason) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", vars.sessionKey)
                    .element("target", groupID)
                    .element("memberID", memberID)
                    .element("msg", reason);
            return doPost("http://" + url + "/kick", request);
        }

//                                                            //
//  depart =========================================== depart //
//                                                            //
        public TextComponent group_To_game_format(long qq) {
            return qq_isBound(qq) ? get_unBoundTXT() :
                    new TextComponent(
                            getMsg("group_to_game")
                    );
        }

        /*
        这两个方法没有什么区别
        只不过是通过未绑定的QQ获取时
        一个返回了 lang.yml 中的 unBound_QQ.text 内容
        一个返回了 null
         */
        public String get_gameName_byQQ(long qq) {
            return qq_isBound(qq) ? getMsg("unBound_QQ.text") : vars.Bound_data.getString("QQ_Bound." + qq);
        }

        public String get_gameName(long qq) {
            return qq_isBound(qq) ? null : vars.Bound_data.getString("QQ_Bound." + qq);
        }

        /*
        QQ 是否被绑定了
         */
        public boolean qq_isBound(long qq) {
            Set<String> bound_qq = vars.Bound_data.getConfigurationSection("QQ_Bound").getKeys(false);
            return !bound_qq.contains(String.valueOf(qq));
        }

        /*
        名字是否被绑定了
         */
        public boolean name_isBound(String name) {
            Set<String> bound_name = vars.Bound_data.getConfigurationSection("Name_Bound").getKeys(false);
            sender.sendMessage(bound_name.toString());
            return bound_name.contains(name);
        }
    }
}
