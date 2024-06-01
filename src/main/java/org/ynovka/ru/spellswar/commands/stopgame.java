package org.ynovka.ru.spellswar.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.ynovka.ru.spellswar.characters.resetRoles;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class stopgame implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Обработка команды /stopgame


        // Проверка прав доступа
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Эта команда не доступна для консоли!");
            return true;
        }
        // Получаем имя игрока, вызвавшего команду
        Player player = (Player) commandSender;
        String playerName = player.getName();
        String currentLowerPlayerName = playerName.toLowerCase();
        // Получение мира, в котором находится игрок
        World world = player.getWorld();
        if (!player.hasPermission("spellsWar.stopgame")) {
            commandSender.sendMessage("У вас недостаточно прав, для выполнения этой команды!");
            return true;
        }
        if (player.getWorld().getName().equals("world")) {
            commandSender.sendMessage("Нельзя остановить игру в хабе!");
            return true;
        }


        // Устанавливаем барьер на спавне
        WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setSize(50.0);


        // Очистка ролей игрокам
        resetRoles.resetroles(currentLowerPlayerName, world);


        // Телепортируем всех игроков находящзихся в мире на спавн этого мира и ставим режим приключений
        for (Player currentPlayer : world.getPlayers()) {
            if (currentPlayer.getWorld().getName().equals(currentLowerPlayerName)) {
                Location location = world.getSpawnLocation();
                currentPlayer.teleport(location);
                currentPlayer.setGameMode(GameMode.ADVENTURE);
                currentPlayer.setHealth(20.0);
                currentPlayer.setFoodLevel(20);
                currentPlayer.setSaturation(20.0f);
            }
        }
        return true;
    }
}
