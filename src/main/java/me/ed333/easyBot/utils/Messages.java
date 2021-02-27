package me.ed333.easyBot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ed333.easyBot.ValuePool;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Set;
public class Messages implements ValuePool {

    public static void initializeMsg() {

        Set<String> keys = vars.lang.getKeys(true);

        for (String key : keys) {
            msgMap.put(key, replaceColor(vars.lang.get(key).toString()));
        }

    }

    public static  String getMsg(String key) {
        return PlaceholderAPI.setPlaceholders(null, msgMap.get(key).toString());
    }

    public static void reloadMsg() {
        vars.lang = YamlConfiguration.loadConfiguration(langFile);
        initializeMsg();
    }

    private static  String replaceColor( String txt) {
        return txt.replace("&", "§");
    }


    protected static TextComponent get_unBoundTXT() {
        TextComponent tc = new TextComponent(getMsg("unBound_QQ.text"));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                hoverEvent_txt_replace(getMsg("unBound_QQ.hoverEvent"))
        ).create()));
        return tc;
    }

    protected static String hoverEvent_txt_replace( String txt) {
        return txt.replace("[", "").replace("]", "").replace(", ", "\n");
    }
}
