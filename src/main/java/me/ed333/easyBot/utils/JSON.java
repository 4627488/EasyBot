package me.ed333.easyBot.utils;

import me.ed333.easyBot.ValuePool;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;

import static me.ed333.easyBot.utils.Messages.getMsg;

public class JSON implements ValuePool{
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
            txt.addExtra(utils.group_To_game_format(getSenderId(msg_json)));

            JSONArray msgChain = get_msgChainArray(msg_json);
            for (Object o : msgChain) {

                JSONObject msg_Single = JSONObject.fromObject(o);

                String type = msg_Single.getString("type");
                if (type.equals("Plain") && catch_text) txt.addExtra(msg_Single.getString("text"));

                if (type.equals("Image") && catch_img) {
                    ValuePool.vars.msg_Img = msg_Single;
                    TextComponent image_txt = new TextComponent(getMsg("Image.text"));
                    image_txt.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, getImg_url(msg_Single)));
                    image_txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(
                                    utils.hoverEvent_txt_replace(getMsg("Image.hoverEvent"))
                            ).create()));
                    txt.addExtra(image_txt);
                }

                if (type.equals("At") && catch_at) {
                    ValuePool.vars.msg_At = msg_Single;
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
