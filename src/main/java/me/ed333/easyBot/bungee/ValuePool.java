package me.ed333.easyBot.bungee;

import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ValuePool {
    Map<String, ServerInfo> serverMap = BungeeCordMain.INSTANCE.getProxy().getServers();
}
