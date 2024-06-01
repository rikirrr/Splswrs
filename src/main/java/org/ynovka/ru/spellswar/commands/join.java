package org.ynovka.ru.spellswar.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class join implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Обработка команды /join


        // Проверка прав доступа
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Эта команда не доступна для консоли!");
            return true;
        }


        try {
            if (strings.length == 0) {
                String lobbyName = "ynovka";
                World lobbyWorld = Bukkit.getWorld(lobbyName);
                Player player = (Player) commandSender;

                Location spawnLocation = lobbyWorld.getSpawnLocation();
                player.teleport(spawnLocation);

            }
            else {
                String lobbyName = strings[0].toLowerCase();
                World lobbyWorld = Bukkit.getWorld(lobbyName);
                Player player = (Player) commandSender;

                Location spawnLocation = lobbyWorld.getSpawnLocation();
                player.teleport(spawnLocation);
            }

            return true;
        }
        catch (ClassCastException e) {
            return false;
        }
    }
}
