package me.ed333.easyBot.bukkit;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ed333.easyBot.utils.Bot;
import me.ed333.easyBot.utils.JSON;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ValuePool {

    File langFile = new File(BukkitMain.INSTANCE.dataPath, "lang.yml");
    File configFile = new File(BukkitMain.INSTANCE.dataPath, "config.yml");
    File dataFile = new File(BukkitMain.INSTANCE.dataPath, "playerData.yml");
    File Bound_Data_File = new File(BukkitMain.INSTANCE.dataPath, "Bound_Data.yml");

    ConsoleCommandSender sender = Bukkit.getConsoleSender();

    Bot bot = new Bot();
    Bot.Utils utils = new Bot.Utils();
    JSON.jsonParse jsonParse = new JSON.jsonParse();

    // bot 的 QQ 和启用的群号
    long botID = vars.Config.getLong("botID");
    long groupID = vars.Config.getLong("groupID");

    List<Player> enabled_Bot_Player = new ArrayList<>();

    /*
     k: msg Key
     v: msg val
     */
    HashMap<String, Object> msgMap = new HashMap<>();

    // 是否启用了抓取纯文本消息
    boolean catch_text = vars.Config.getBoolean("catch.text");

    // 是否启用了抓取图片消息
    boolean catch_img = vars.Config.getBoolean("catch.img");

    // 是否启用了抓取At消息
    boolean catch_at = vars.Config.getBoolean("catch.at");

    // 配置文件中的 host 和 authkey
    String url = vars.Config.getString("host");
    String authKey = vars.Config.getString("authKey");

    // 存放变量
    class vars {
        // 是否启用了Bot
        public static boolean enable_bot;
        public static String prefix;
        public static String sessionKey;
        public static JSONObject msg_Json;
        public static JSONObject msg_Img;
        public static JSONObject msg_At;
        /*
         k: game name
         v: qq number
         */
        public static HashMap<String, Long> verify = new HashMap<>();
        public static YamlConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
        public static YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(dataFile);
        public static YamlConfiguration Bound_data = YamlConfiguration.loadConfiguration(Bound_Data_File);
        public static YamlConfiguration Config = YamlConfiguration.loadConfiguration(configFile);
    }

    class PlaceHolders extends PlaceholderExpansion {

        @Override
        public @NotNull String getIdentifier() {
            return "txt";
        }

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public String onPlaceholderRequest(Player player, @NotNull String params) {
            switch (params) {
                case "sender_qq" :
                    return jsonParse.getSenderId(vars.msg_Json).toString();
                case "sender_name":
                    return jsonParse.getSender_groupName(vars.msg_Json);
                case "sender_gameName":
                    return utils.get_gameName_byQQ(jsonParse.getSenderId(vars.msg_Json));
                case "image_id":
                    return jsonParse.getImg_id(vars.msg_Img);
                case "image_url":
                    return jsonParse.getImg_url(vars.msg_Img);
                case "at_targetID" :
                    return jsonParse.getAt_targetID(vars.msg_At).toString();
                case "at_targetName":
                    return JSONObject.fromObject(utils.getMemberInfo(jsonParse.getAt_targetID(vars.msg_At))).getString("name");
                case "at_target_gameName":
                    return utils.get_gameName_byQQ(jsonParse.getAt_targetID(vars.msg_At));
                case "group":
                    return String.valueOf(groupID);
            }
            return null;
        }

        @Override
        public @NotNull String getAuthor() {
            return "ed";
        }

        @Override
        public @NotNull String getVersion() {
            return "000";
        }
    }

}