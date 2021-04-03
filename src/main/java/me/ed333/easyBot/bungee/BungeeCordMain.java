package me.ed333.easyBot.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 测试功能
 * 暂无实际用途
 */
public class BungeeCordMain extends Plugin implements ValuePool{
    static Configuration config;
    public static BungeeCordMain INSTANCE;

    @Override
    public void onLoad() {
        getLogger().info("服务器: ");
    }

    @Override
    public ProxyServer getProxy() {
        return super.getProxy();
    }

    public void saveDefaultConfig() {
        File dir = getDataFolder();
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")){
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void reloadConfig() {
        File file = new File(getDataFolder(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfig() {
        return config;
    }
}