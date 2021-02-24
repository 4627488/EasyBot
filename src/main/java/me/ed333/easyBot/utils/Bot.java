package me.ed333.easyBot.utils;

import me.ed333.easyBot.ValuePool;
import net.md_5.bungee.api.chat.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

import static me.ed333.easyBot.utils.Messages.getMsg;

public class Bot extends HttpRequest implements ValuePool {
    private socketClient client;
    
    /**
     * 连接 SocketServer 监听 事件/消息
     * @param sessionKey session
     */
    public void connect(String sessionKey) throws URISyntaxException {
        sender.sendMessage("§3BOT: §eConnecting to http socket server...");

        URI uri = new URI("ws://" + this.url + "/message?sessionKey=" + sessionKey);
        client = new socketClient(uri);
        client.connect();
    }

    /**
     * 关闭连接
     */
    public void closeSocket() throws Exception {
        client.close();
        sender.sendMessage("§3BOT: §aReleasing session...");
        String result = bot.release_Session(vars.sessionKey);
        sender.sendMessage("§3BOT: §aRelease session finished. result: " + result);
    }

    /**
     * 验证身份
     * @return result
     */
    public String auth() throws Exception {
        sender.sendMessage("§aAuth bot...");
        return doPost("http://" + this.url + "/auth", new JSONObject().element("authKey", authKey));
    }

    /**
     * 校验 Session 并将 Session 绑定到BotQQ
     * @param SessionKey session
     * @return result String
     */
    public String verify(String SessionKey) throws Exception {
        sender.sendMessage("§aStarting bind...");
        return doPost("http://" + this.url + "/verify", new JSONObject().element("sessionKey", SessionKey).element("qq", botID));
    }

    /**
     * 在卸载插件时释放 bot 绑定的session
     */
    public String release_Session(String sessionKey) throws Exception {
        JSONObject request = new JSONObject().element("sessionKey", sessionKey)
                .element("qq", botID);
        return doPost("http://" + url + "/release", request);
    }

    static class socketClient extends WebSocketClient implements ValuePool {

        public socketClient(URI serverUri) { super(serverUri); }

        @Override
        public void onOpen(@NotNull ServerHandshake handshake) {
            sender.sendMessage("§3BOT: §aConnected! §7| §aPlayerData: " + handshake.getHttpStatus());
        }

        @Override
        public void onMessage(String message) {
           sender.sendMessage(message);

            JSONObject msg_json = JSONObject.fromObject(message);
            vars.msg_Json = msg_json;

            if (jsonParse.getMsgType(msg_json).equals("GroupMessage")) {
                if (jsonParse.getGroupID(msg_json).equals(groupID)) {

                    String catchType = defaultConfig.getString("catch.type");

                    for (Player p : enabled_Bot_Player) {
                        if (catchType.equals("text") && defaultConfig.getBoolean("catch.text") && !jsonParse.getText(msg_json).equals("")) {
                            p.sendMessage(jsonParse.getText(msg_json));
                        } else if (catchType.equals("multi") && (catch_at || catch_img || catch_text)) {
                            p.spigot().sendMessage(jsonParse.getMulti(msg_json));
                        }
                    }
                }
            } else if (jsonParse.getMsgType(msg_json).equals("TempMessage")) {
                if (vars.verify.containsValue(jsonParse.getSenderId(msg_json))) {
                    vars.Bound_data.set("QQ_Bound." + jsonParse.getSenderId(msg_json), utils.getKey(vars.verify, jsonParse.getSenderId(msg_json)));
                    vars.Bound_data.set("Name_Bound." + utils.getKey(vars.verify, jsonParse.getSenderId(msg_json)), jsonParse.getSenderId(msg_json));
                    vars.verify.remove(utils.getKey(vars.verify, jsonParse.getSenderId(msg_json)));
                }
            }

        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            sender.sendMessage("§3BOT: §aOkay,closed!");
        }

        @Override
        public void onError(@NotNull Exception ex) {
            sender.sendMessage("§3BOT: §cOh, it looks that something takes wrong, result: " + ex.getCause());
            ex.printStackTrace();
        }
    }
    
    public static class Utils {
        /**
         * 发送好友消息
         * @param sessionKey session
         * @param target 好友ID
         * @param quote 启用引用
         * @param code 如果引用启用， msg 的 SourceID
         * @param msgChain 要发送的消息
         * @return result
         */
        public String sendFriendMessage(String sessionKey, long target, boolean quote, int code, JSONArray msgChain) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
                    .element("target", target)
                    .element("messageChain", msgChain);
            if (quote) request.element("quote", code);
            return doPost("http://" + url + "/sendFriendMessage", request);
        }

        /**
         * 通过群给某人发送临时消息
         * @param qq 临时消息对象的QQ
         * @param groupID 临时消息的群号
         * @return result
         */
        public String sendTempMessage(String sessionKey, long qq, long groupID, boolean quote, int code, JSONArray msgChain) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
                    .element("qq", qq)
                    .element("group", groupID)
                    .element("messageChain",msgChain);
            if (quote) request.element("quote", code);
            return doPost("http://" + url + "/sendTempMessage", request);
        }

        /**
         * 发送群聊消息
         * @return result
         */
        public String sendGroupMessage(String sessionKey, long groupID, boolean quote, int code, JSONArray msgChain) throws Exception {
            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
                    .element("target", groupID)
                    .element("messageChain", msgChain);
            if (quote) request.element("quote", code);
            return doPost("http://" + url + "/sendGroupMessage", request);
        }

        public @NotNull String getMemberInfo(long memberID) {
            return doGet("http://" + url + "/memberInfo", "sessionKey=" + vars.sessionKey + "&target=" + groupID + "&memberId=" + memberID);
        }

//
//        /**
//         * 撤回消息
//         * @param SourceID 消息的ID
//         * @return result
//         */
//        public String recall(String sessionKey, int SourceID) throws Exception {
//            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
//                    .element("target", SourceID);
//            return doPost("http://" + b.url + "/recall", request);
//        }
//
//        /**
//         * 禁言群员
//         * @param memberID 群员ID
//         * @param time 时间 单位: 秒
//         * @return result
//         */
//        public String mute(String sessionKey, long groupID, long memberID, int time) throws Exception {
//            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
//                    .element("target", groupID)
//                    .element("memberID", memberID)
//                    .element("time", time);
//            return doPost("http://" + b.url + "/mute", request);
//        }
//
//        /**
//         * 解除禁言
//         */
//        public String unmute(String sessionKey, long groupID, long memberID) throws Exception {
//            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
//                    .element("target", groupID)
//                    .element("memberID", memberID);
//            return doPost("http://" + b.url + "/unmute", request);
//        }
//
//        /**
//         * 踢出群员
//         * @param reason 原因
//         * @return result
//         */
//        public String kick(String sessionKey, long groupID, long memberID, String reason) throws Exception {
//            JSONObject request = new JSONObject().element("sessionKey", sessionKey)
//                    .element("target", groupID)
//                    .element("memberID", memberID)
//                    .element("msg", reason);
//            return doPost("http://" + b.url + "/kick", request);
//        }

//                                                            //
//  depart =========================================== depart //
//                                                            //
        public TextComponent group_To_game_format(long qq) {
            return qq_isBound(qq) ? get_unBoundTXT() :
                    new TextComponent(
                            getMsg("group_to_game")
                    );
        }

        public String get_gameName_byQQ(long qq) {
            return qq_isBound(qq) ? getMsg("unBound_QQ.text") : vars.Bound_data.getString("QQ_Bound." + qq);
        }

        public boolean qq_isBound(long qq) {
            Set<String> bound_qq = vars.Bound_data.getConfigurationSection("QQ_Bound").getKeys(false);
            return !bound_qq.contains(String.valueOf(qq));
        }

        public boolean name_isBound(String name) {
            Set<String> bound_name = vars.Bound_data.getConfigurationSection("Name_Bound").getKeys(false);
            sender.sendMessage(bound_name.toString());
            return bound_name.contains(name);
        }

        protected TextComponent get_unBoundTXT() {
            TextComponent tc = new TextComponent(getMsg("unBound_QQ.text"));
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                   hoverEvent_txt_replace(getMsg("unBound_QQ.hoverEvent"))
            ).create()));
            return tc;
        }

        protected String hoverEvent_txt_replace(String txt) {
            return txt.replace("[", "").replace("]", "").replace(", ", "\n");
        }

        private String getKey(HashMap<String, Long> map, Object value) {
            String key = "NOT FIND";
            Set<String> keySet = map.keySet();
            for (String o : keySet) {
                if (map.get(o).equals(value)) key = o;
            }
            return key;
        }
    }
    
    public static class jsonParse {

        /**
         * 通过接收到的 json 获取 msgChain
         * @param msg_json 接收到的json
         * @return msgChain
         */
        public JSONArray get_msgChainArray(@NotNull JSONObject msg_json) {
            return msg_json.getJSONArray("messageChain");
        }

        /**
         * 通过接收到的 json 获取 msgType
         * @param msg_Json 接收到的json
         * @return msg Type
         */
        public String getMsgType(@NotNull JSONObject msg_Json) {
            return msg_Json.getString("type");
        }

        /**
         * 通过接收到的 json 获取 "sender" 块 json
         * @param msg_Json 接收到的 json
         * @return sender_json
         */
        public JSONObject getSender_Json(@NotNull JSONObject msg_Json) {
            return msg_Json.getJSONObject("sender");
        }

        /**
         * 通过 sender_json 获取 "group" 块的 json
         * @param sender_json sender 块的 json
         * @return group_json
         */
        public JSONObject getSender_group_json(@NotNull JSONObject sender_json) {
            return sender_json.getJSONObject("group");
        }

        /**
         * 通过接收到的 json 获取 "group" 块的 json
         * @param msg_Json 接收到的 json
         * @return group_json
         */
        public JSONObject getSender_group(JSONObject msg_Json) {
            return getSender_Json(msg_Json).getJSONObject("group");
        }

        /**
         * 通过接收到的 json 获取发送者的 qq 号
         * @param msg_json 接收到的json
         * @return sender_id
         */
        public @NotNull Long getSenderId(JSONObject msg_json) {
            return getSender_Json(msg_json).getLong("id");
        }

        /**
         * 通过接收到的 json 获取发送者的群名片
         * @param msg_json 接收到的 json
         * @return sender group name
         */
        public String getSender_groupName(JSONObject msg_json) {
            return getSender_Json(msg_json).getString("memberName");
        }

        /**
         * 通过接收到的 json 获取发送者的 QQ 昵称
         * @param msg_json 原始json
         * @return result
         * @see jsonParse#getSender_groupName(JSONObject msgJson)
         */
        @Deprecated
        public String getSender_nickName(JSONObject msg_json) {
            return getSender_Json(msg_json).getString("nickName");
        }

        /**
         * 获取发送者所在的群id 该方法同时还可以获取临时消息中发送者所在群id
         * @param msg_Json 原始JSON
         * @return groupID
         */
        public @NotNull Long getGroupID(JSONObject msg_Json) {
            return getSender_group_json(getSender_Json(msg_Json)).getLong("id");
        }

        /**
         * 获得消息中图片地址
         */
        public String getImg_url(@NotNull JSONObject msg_single) {
            return msg_single.getString("url");
        }

        /**
         * 获得消息中图片的 id
         */
        public String getImg_id(@NotNull JSONObject msg_Single) {
            return msg_Single.getString("imageId");
        }

        /**
         * 获取消息中被at的id
         */
        public Long getAt_targetID(JSONObject msg_at) {
            return msg_at.getLong("target");
        }

        /**
         * 获取 Text
         * 仅获取文字内容
         */
        public @NotNull String getText(JSONObject msg_json) {
            StringBuilder sb = new StringBuilder();
            JSONArray msgChain = get_msgChainArray(msg_json);
            for (Object o: msgChain) {
                JSONObject msg_Single = JSONObject.fromObject(o);
                String type = msg_Single.getString("type");

                if (type.equals("Plain")) sb.append(msg_Single.getString("text"));
            }
            return sb.toString();
        }

        /**
         * 获取 text 可以获取 纯文本 image at 等
         * @param msg_json 接收的json
         * @return result
         */
        public @NotNull TextComponent getMulti(JSONObject msg_json) {
            TextComponent txt = new TextComponent();

            txt.addExtra(Messages.getMsg("group_prefix"));
            txt.addExtra(utils.group_To_game_format(jsonParse.getSenderId(msg_json)));

            JSONArray msgChain = get_msgChainArray(msg_json);
            for (Object o : msgChain) {

                JSONObject msg_Single = JSONObject.fromObject(o);

                String type = msg_Single.getString("type");
                if (type.equals("Plain") && catch_text) txt.addExtra(msg_Single.getString("text"));

                if (type.equals("Image") && catch_img) {
                    vars.msg_Img = msg_Single;
                    TextComponent image_txt = new TextComponent(getMsg("Image.text"));
                    image_txt.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, getImg_url(msg_Single)));
                    image_txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(
                                    utils.hoverEvent_txt_replace(getMsg("Image.hoverEvent"))
                            ).create()));
                    txt.addExtra(image_txt);
                }

                if (type.equals("At") && catch_at) {
                    vars.msg_At = msg_Single;
                    TextComponent at_txt = new TextComponent(getMsg("At.text"));
                    at_txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(
                                    utils.hoverEvent_txt_replace(getMsg("At.hoverEvent"))
                            ).create()));
                    txt.addExtra(at_txt);
                }
            }
            return txt;
        }
    }
}