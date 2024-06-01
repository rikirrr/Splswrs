package org.ynovka.ru.spellswar.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class createlobby implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Обработка команды /createlobby


        // Проверка прав доступа
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Эта команда не доступна для консоли!");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("spellsWar.startgame")) {
            commandSender.sendMessage("У вас недостаточно прав, для выполнения этой команды!");
            return true;
        }


        String playerName = player.getName();
        String lowerPlayerName = playerName.toLowerCase();
        // Delete existing world if present
        Bukkit.dispatchCommand(commandSender, "mv delete " + lowerPlayerName);
        Bukkit.dispatchCommand(commandSender, "mv confirm");
        // Create new lobby world with default settings
        WorldCreator creator = new WorldCreator(lowerPlayerName);
        World newLobbyWorld = getServer().createWorld(creator);
        // Get world
        World newWorld = Bukkit.getWorld(lowerPlayerName);
        assert newWorld != null;
        newWorld.setPVP(false);
        // TP player
        Location spawnLocation = newWorld.getSpawnLocation();
        player.teleport(spawnLocation);
        // Set world border center
        WorldBorder worldBorder = newWorld.getWorldBorder();
        worldBorder.setCenter(spawnLocation);
        // Set world border size to 30
        worldBorder.setSize(30);
        Bukkit.dispatchCommand(commandSender, "mv import " + lowerPlayerName + " normal");
        return true;
    }
}
