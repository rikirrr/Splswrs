package org.ynovka.ru.spellswar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.ynovka.ru.spellswar.commands.*;
import org.ynovka.ru.spellswar.characters.*;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.ynovka.ru.spellswar.events.events;

import java.util.concurrent.CountDownLatch;


public final class SpellsWar extends JavaPlugin implements CommandExecutor {
    private CountDownLatch worldCreationLatch;
    public static Map<String, Integer> games = new HashMap<>();
    public static SpellsWar inst;

    public static Plugin getInstance() {
        return inst;
    }

    @Override
    public void onEnable() {
        inst = this;
        getServer().getPluginManager().registerEvents(new invisibleMan(), this);

        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new events(), this);
        Bukkit.getPluginManager().registerEvents(new invisibleMan(), this);
        Bukkit.getPluginManager().registerEvents(new lightningMan(), this);
        Bukkit.getPluginManager().registerEvents(new minerMan(), this);
        Bukkit.getPluginManager().registerEvents(new waterMan(), this);
        Bukkit.getPluginManager().registerEvents(new elfMan(), this);
        Bukkit.getPluginManager().registerEvents(new ironMan(), this);

        getCommand("join").setExecutor(new join());

        getCommand("createlobby").setExecutor(new createlobby());

        getCommand("startgame").setTabCompleter(new startgameCompleter());
        getCommand("startgame").setExecutor(new startgame());

        getCommand("stopgame").setExecutor(new stopgame());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}