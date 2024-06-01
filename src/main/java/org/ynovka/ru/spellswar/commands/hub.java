package org.ynovka.ru.spellswar.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class hub implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Обработка команды /hub


        // Проверка прав доступа
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Эта команда не доступна для консоли!");
            return true;
        }


        //Получаем имя лобби и игрока
        World lobbyWorld = Bukkit.getWorld("world");
        Player player = (Player) commandSender;


        // телепортируем
        Location spawnLocation = lobbyWorld.getSpawnLocation();
        player.teleport(spawnLocation);
        return true;
    }
}
