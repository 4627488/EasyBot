package me.ed333.easyBot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ed333.easyBot.ValuePool;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
public class Messages implements ValuePool {

    public static void initializeMsg() {

        @NotNull Set<String> keys = vars.lang.getKeys(true);

        for (String key : keys) {
            msgMap.put(key, replaceColor(vars.lang.get(key).toString()));
        }

    }

    public static @NotNull String getMsg(String key) {
        return PlaceholderAPI.setPlaceholders(null, msgMap.get(key).toString());
    }

    public static void reloadMsg() {
        vars.lang = YamlConfiguration.loadConfiguration(langFile);
        initializeMsg();
    }

    private static String replaceColor(String txt) {
        return txt.replace("&", "§");
    }
}
