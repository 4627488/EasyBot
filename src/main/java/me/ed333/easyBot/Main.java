package me.ed333.easyBot;

import me.ed333.easyBot.utils.Messages;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static me.ed333.easyBot.utils.Messages.getMsg;

public class Main extends JavaPlugin implements ValuePool, Listener {

    public static Main I;
    public Main() {I= this;}

    public String dataPath = getDataFolder().getPath();

    @Override
    public void onEnable() {
        try {
            checkFile();
            Messages.initializeMsg();
            vars.prefix = msgMap.get("prefix").toString();

            Bukkit.getPluginManager().registerEvents(this, this);
            new PlaceHolders().register();

            if (defaultConfig.getBoolean("enable-bot")) initializeBot();
            else sender.sendMessage("§eBot not enabled");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to initialize plugin: " + e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        sender.sendMessage("§3BOT: §aSaving data and closing connect...");
        try {
            bot.closeSocket();
            vars.PlayerData.save(dataFile);
            vars.Bound_data.save(Bound_Data_File);
            sender.sendMessage("§3BOT: §aOkay! Bye bye.");
        } catch (Exception e) {
            sender.sendMessage("§3BOT: §cOh! Something takes wrong: " + e.getCause());
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
                        sender.sendMessage("§3BOT: §aSaving data and reconnect...");
                        vars.PlayerData.save(dataFile);
                        vars.Bound_data.save(Bound_Data_File);
                        checkFile();
                        reloadConfig();
                        Messages.reloadMsg();

                        if (defaultConfig.getBoolean("enable-bot")) {
                            bot.closeSocket();
                            initializeBot();
                        }
                        sender.sendMessage(msgMap.get("prefix").toString() + getMsg("reload"));
                        }
                    }

                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (args[0].equalsIgnoreCase("bind") && args.length == 2) {
                            if (isQQ(args[1])) {
                                utils.sendTempMessage(
                                        vars.sessionKey,
                                        Long.parseLong(args[1]),
                                        groupID,
                                        false,
                                        0,
                                        new JSONArray().element(
                                                new JSONObject().element("type", "Plain")
                                                        .element("text", getMsg("verify_text"))
                                        ));
                                vars.verify.put(p.getName(), Long.parseLong(args[1]));
                                p.sendMessage(vars.prefix + getMsg("verify_msgSend"));

                            } else {p.sendMessage(vars.prefix + getMsg("InvalidQQ"));}
                        } else if (args[0].equalsIgnoreCase("enable")) {
                            vars.Bound_data.set(p.getUniqueId() + ".enable_Bot", true);
                            enabled_Bot_Player.add(p);
                            p.sendMessage(vars.prefix + getMsg("enable_Bot"));
                        } else if (args[0].equalsIgnoreCase("disable")) {
                            vars.Bound_data.set(p.getUniqueId() + ".enable_Bot", false);
                            enabled_Bot_Player.remove(p);
                            p.sendMessage(vars.prefix + getMsg("disable_Bot"));
                        }


                    } else sender.sendMessage(vars.prefix + getMsg("notPlayer"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    public void initializeBot() throws Exception {
        JSONObject auth_result = JSONObject.fromObject(bot.auth());
        if (auth_result.getInt("code") == 0) {
            sender.sendMessage("§aAuth successful! result: " + auth_result);
            vars.sessionKey = auth_result.getString("session");
            JSONObject verify_result = JSONObject.fromObject(bot.verify(vars.sessionKey));
            if (verify_result.getInt("code") == 0) {
                sender.sendMessage("§aSuccessful! result: " + verify_result);
                bot.connect(vars.sessionKey);
            } else sender.sendMessage("§cBot bind failed! result: §7" + verify_result);
        } else sender.sendMessage("§cBot auth failed, result: §7" + auth_result);
    }

    //@SuppressWarnings("all")
    private void checkFile() throws IOException {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        if (!configFile.exists()) saveDefaultConfig();
        if (!langFile.exists()) saveResource("lang.yml", false);
        if (!dataFile.exists()) dataFile.createNewFile();
        if (!Bound_Data_File.exists()) Bound_Data_File.createNewFile();


        vars.lang = YamlConfiguration.loadConfiguration(langFile);
        vars.PlayerData = YamlConfiguration.loadConfiguration(dataFile);
        vars.Bound_data = YamlConfiguration.loadConfiguration(Bound_Data_File);

        if (vars.Bound_data.getKeys(false).isEmpty()) {
            vars.Bound_data.createSection("QQ_Bound");
            vars.Bound_data.createSection("Name_Bound");
        }
    }

    private boolean isQQ(@NotNull String qq) {
        return qq.matches("[1-9][0-9]{8,10}");
    }

    @EventHandler
    private void onJoin(@NotNull PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (vars.PlayerData.getBoolean(p.getUniqueId() + ".enable_Bot")) {
            enabled_Bot_Player.add(p);
        }else if (!utils.name_isBound(p.getName())) {
            vars.PlayerData.set(p.getUniqueId() + ".enable_Bot", true);
            enabled_Bot_Player.add(p);
        }

        sender.sendMessage(enabled_Bot_Player.toString());
    }

    @EventHandler
    private void onLeave(@NotNull PlayerQuitEvent event) {
        enabled_Bot_Player.remove(event.getPlayer());
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) throws Exception {
        String message = event.getMessage();
        if (defaultConfig.getBoolean("enable-bot"))
        utils.sendGroupMessage(
                vars.sessionKey,
                groupID,
                false,
                0,
                new JSONArray().element(
                        new JSONObject().element("type", "Plain")
                        .element("text", event.getPlayer().getName() + ": " + message)
        ));
    }
}
