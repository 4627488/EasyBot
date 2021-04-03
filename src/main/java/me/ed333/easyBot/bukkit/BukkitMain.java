package me.ed333.easyBot.bukkit;

import me.ed333.easyBot.events.ListeningEvent;
import me.ed333.easyBot.utils.HttpRequest;
import me.ed333.easyBot.utils.MessageChain;
import me.ed333.easyBot.utils.Messages;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Set;

import static me.ed333.easyBot.utils.Messages.getMsg;
import static me.ed333.easyBot.utils.Messages.reloadMsg;

public class BukkitMain extends JavaPlugin implements ValuePool {

    public static BukkitMain INSTANCE;
    public BukkitMain() {INSTANCE = this;}

    public String dataPath = getDataFolder().getPath();

    @Override
    public void onEnable() {

        try {
            checkFile();
            CheckCfg();
            if (vars.Config.getBoolean("updateCheck")) CheckUpdate();

            Messages.initializeMsg();
            vars.prefix = msgMap.get("prefix").toString();
            vars.enable_bot = vars.Config.getBoolean("enable-bot");

            Bukkit.getPluginManager().registerEvents(new ListeningEvent(), this);
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceHolders().register();
            if (vars.Config.getBoolean("enable-bot")) initializeBot();
            else sender.sendMessage(vars.prefix + getMsg("disable_Bot"));

            Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();

            for (Player p : onlinePlayers) {
                if (vars.PlayerData.getBoolean(p.getUniqueId() + ".enable_Bot")) {
                    enabled_Bot_Player.add(p);
                }
            }
        } catch (Exception e) {
            sender.sendMessage("[EasyBot] §c初始化插件失败: " + e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        sender.sendMessage("§3BOT: §a关闭并保存数据中...");
        try {
            bot.closeSocket();
            vars.PlayerData.save(dataFile);
            vars.Bound_data.save(Bound_Data_File);
            sender.sendMessage("§3BOT: §a数据保存完毕！再见，感谢使用 EasyBot.");
        } catch (Exception e) {
            sender.sendMessage("§3BOT: §c出错了！原因: " + e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("bot")) {
                if (args.length == 0) {sender.sendMessage(command.getDescription());}
                else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (!sender.hasPermission("bot.reload")) {
                            sender.sendMessage(getMsg("permissionDeny"));
                        } else {
                            sender.sendMessage("§3BOT: §a保存配置并重新连接...");
                            vars.PlayerData.save(dataFile);
                            vars.Bound_data.save(Bound_Data_File);
                            checkFile();
                            reloadMsg();

                            // enable_bot 配置项检查
                            vars.enable_bot = vars.Config.getBoolean("enable-bot");
                            if (vars.enable_bot && !bot.isConnected) {
                                initializeBot();
                                sender.sendMessage(getMsg("enable_Bot"));
                            }
                            else if (!vars.enable_bot && bot.isConnected) {
                                bot.closeSocket();
                                sender.sendMessage(getMsg("disable_Bot"));
                            } else if (bot.isConnected){
                                bot.closeSocket();
                                initializeBot();
                                sender.sendMessage(getMsg("enable_Bot"));
                            }
                        }
                    } else if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (args[0].equalsIgnoreCase("bind") && args.length == 2) {
                            if (isQQ(args[1])) {
                                utils.sendTempMessage(
                                        Long.parseLong(args[1]),
                                        groupID,
                                        false, 0,
                                        new MessageChain().addPlain(getMsg("verify_text")));
                                vars.verify.put(p.getName(), Long.parseLong(args[1]));
                                p.sendMessage(vars.prefix + getMsg("verify_msgSend"));

                            } else {p.sendMessage(vars.prefix + getMsg("InvalidQQ"));}

                        // /bot enable
                        } else if (args[0].equalsIgnoreCase("enable")) {
                            vars.Bound_data.set(p.getUniqueId() + ".enable_Bot", true);
                            enabled_Bot_Player.add(p);
                            p.sendMessage(vars.prefix + getMsg("player_enable_Bot"));

                        // /bot disable
                        } else if (args[0].equalsIgnoreCase("disable")) {
                            vars.Bound_data.set(p.getUniqueId() + ".enable_Bot", false);
                            enabled_Bot_Player.remove(p);
                            p.sendMessage(vars.prefix + getMsg("player_disable_Bot"));
                        }
                    } else sender.sendMessage(vars.prefix + getMsg("notPlayer"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    private void initializeBot() throws Exception {
        JSONObject auth_result = JSONObject.fromObject(bot.auth());
        if (auth_result.getInt("code") == 0) {
            sender.sendMessage("§3BOT: §a注册成功! result: " + auth_result);
            vars.sessionKey = auth_result.getString("session");
            JSONObject verify_result = JSONObject.fromObject(bot.verify(vars.sessionKey));
            if (verify_result.getInt("code") == 0) {
                sender.sendMessage("§3BOT: §a验证成功! 服务器返回结果: " + verify_result);
                bot.connect(vars.sessionKey);
            } else sender.sendMessage("§3BOT: §c验证失败！ 服务器返回结果: §7" + verify_result);
        } else sender.sendMessage("§3BOT: §c注册失败！服务器返回结果: §7" + auth_result);
    }

    private void checkFile() throws IOException {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        if (!configFile.exists()) saveDefaultConfig();
        if (!langFile.exists()) saveResource("lang.yml", false);
        if (!dataFile.exists()) dataFile.createNewFile();
        if (!Bound_Data_File.exists()) Bound_Data_File.createNewFile();


        vars.lang = YamlConfiguration.loadConfiguration(langFile);
        vars.PlayerData = YamlConfiguration.loadConfiguration(dataFile);
        vars.Bound_data = YamlConfiguration.loadConfiguration(Bound_Data_File);
        vars.Config = YamlConfiguration.loadConfiguration(configFile);

        if (vars.Bound_data.getKeys(false).isEmpty()) {
            vars.Bound_data.createSection("QQ_Bound");
            vars.Bound_data.createSection("Name_Bound");
        }
    }

    private boolean isQQ(@NotNull String qq) {
        return qq.matches("[1-9][0-9]{8,10}");
    }

    public void printDEBUG(String txt) {
        if (vars.Config.getBoolean("DEBUG"))
        getLogger().info("DEBUG: " + txt);
    }

    /*
    检查有无新增配置
     */
    private void CheckCfg() throws IOException {
        InputStream in = getResource("config.yml");
        YamlConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
        Set<String> resourceConfigKeys = resourceConfig.getKeys(true);
        for (String key : resourceConfigKeys) {
            Object configVal = vars.Config.get(key);
            if (configVal == null) {
                vars.Config.set(key, resourceConfig.get(key));
                vars.Config.save(configFile);
            }
        }

        if (vars.Config.getDouble("version") != resourceConfig.getDouble("version")) {
            vars.Config.set("version", resourceConfig.getDouble("version"));
        }
        vars.Config = YamlConfiguration.loadConfiguration(configFile);
    }

    /*
    检查更新
     */
    private void CheckUpdate() {
        JSONObject updateJson = JSONObject.fromObject(HttpRequest.doGet("https://raw.githubusercontent.com/ed-3/EasyBot/master/CheckVersion/version.json", ""));
        String version = updateJson.getString("Version");
        double versionNum = Double.parseDouble(version.split("-")[0]);

        String downloadUrl = updateJson.getString("DownLoad_Url");
        JSONArray UpdateDescription = updateJson.getJSONArray("UpdateDescription");

        if (versionNum > vars.Config.getDouble("version")) {
            sender.sendMessage("§3BOT: §7有新的更新可用: §a" + version);
            sender.sendMessage("§3BOT: §7请下载最新版本以获得更多功能或避免BUG。");
            sender.sendMessage("§3BOT: §7请注意: §aBETA版§7会带来新的功能, 但也可能会造成新的BUG。如果有任何BUG欢迎反馈。");
            sender.sendMessage("§3BOT: §7下载地址: §a" + downloadUrl);
            sender.sendMessage("§3BOT: §7如果觉得插件好的话记得MCBBS评个分并把本项目给个Star~");
            sender.sendMessage("§3BOT: §7本次更新内容如下: ");
            for (Object str : UpdateDescription) {
                sender.sendMessage("§7      - " + str);
            }
            sender.sendMessage("");
        }
    }
}