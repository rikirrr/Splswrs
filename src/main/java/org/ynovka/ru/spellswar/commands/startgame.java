package org.ynovka.ru.spellswar.commands;

import org.bukkit.scoreboard.*;
import org.ynovka.ru.spellswar.SpellsWar;
import org.ynovka.ru.spellswar.characters.*;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class startgame implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Обработка команды /startgame


        // Проверка прав доступа
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Эта команда не доступна для консоли!");
            return true;
        }
        // Получаем имя игрока, вызвавшего команду
        Player player = (Player) commandSender;
        String playerName = player.getName();
        String lowerPlayerName = playerName.toLowerCase();
        // Получение мира, в котором находится игрок
        World world = player.getWorld();
        if (!player.hasPermission("spellsWar.startgame")) {
            commandSender.sendMessage("У вас недостаточно прав, для выполнения этой команды!");
            return true;
        }
        if (player.getWorld().getName().equals("world")) {
            commandSender.sendMessage("Нельзя начать игру в хабе!");
            return true;
        }


        // Получение параметров команды
        String duration = "medium"; // Значение по умолчанию для duration
        String choice = "random"; // Значение по умолчанию для choice
        try {
            if (strings.length >= 1) {
                duration = strings[0].toLowerCase();
            }
            if (strings.length >= 2) {
                choice = strings[1].toLowerCase();
            }
        }
        catch (ClassCastException e) {
            return false;
        }




        // Установка барьера, в зависимости от выбора продолжительности игры
        int barrierSize, barrierTime;
        switch (duration) {
            case "short": barrierSize = 1000; barrierTime = 900; break;
            case "medium": barrierSize = 2000; barrierTime = 1800; break;
            case "long": barrierSize = 3000; barrierTime = 3200; break;
            default: commandSender.sendMessage("Неверно указана длительность игры. Используйте short, medium или long."); return false;
        }


        // Установка ролей игрокам, в зависимости от выбора их раздачи (рандом, выбор)
        if (choice.equals("random")) {
            // Логика для случайного выбора ролей
            List<String> roles = new ArrayList<>();
            roles.add("invisibleMan");
            roles.add("lightningMan");
            roles.add("minerMan");
            roles.add("waterMan");
            roles.add("elfMan");
            roles.add("ironMan");

            List<String> abilities = new ArrayList<>();
            abilities.add("dash"); // рывок
            abilities.add("inspection"); // всевидящее око - надзор - инспекция
            abilities.add("healing"); // исцеление
            abilities.add("hack"); // взлом
            // перебираем всех игроков, кто в этом мире - назвначаем случайную роль
            for (Player currentPlayer : world.getPlayers()) {
                if (currentPlayer.getWorld().getName().equals(lowerPlayerName)) {
                    String currentPlayerName = currentPlayer.getName();
                    String currentLowerPlayerName = currentPlayerName.toLowerCase();


                    Random random_role = new Random();
                    int randomIndex_role = random_role.nextInt(roles.size());
                    String randomRole_role = roles.remove(randomIndex_role);

                    Scoreboard scoreboard_role = currentPlayer.getScoreboard();
                    Team team = scoreboard_role.getTeam(randomRole_role);
                    if (team == null) {
                        team = scoreboard_role.registerNewTeam(randomRole_role);
                    }
                    team.addEntry(currentPlayer.getName());
                    setRoles.setroles(currentLowerPlayerName, world);

                    // Выдача рандомных способностей
//                    Random random_abilities = new Random();
//                    int randomIndex_abilities = random_abilities.nextInt(roles.size());
//                    String random_ability = random_abilities.randomIndex_abilities;
//
//
//                    ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
//                    Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
//
//                    // Создаем или получаем скорборд "abilities"
//                    Objective objective = scoreboard.getObjective("abilities");
//                    if (objective == null) {
//                        objective = scoreboard.registerNewObjective("abilities", "dummy", "Abilities");
//                    }
//
//                    // Устанавливаем значение для игрока
//                    Score score = objective.getScore(player.getName());
//                    score.setScore(value);
//
//                    // Назначаем скорборд игроку
//                    player.setScoreboard(scoreboard);
                }
            }
        } else if (choice.equals("select")) {
            for (Player currentPlayer : world.getPlayers()) {
                if (currentPlayer.getWorld().getName().equals(lowerPlayerName)) {
                    currentPlayer.performCommand("menu");
                }
            }
        } else {
            commandSender.sendMessage("Неверно указан режим выбора ролей. Используйте random или select.");
            return false;
        }


        // Логика изменения настроек игрового мира


        // Изменение размера барьера
        WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setSize(barrierSize);
        world.setPVP(false);
        Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
            world.setPVP(true);
        }, 120 * 20);


        // Логика телепортации всех игроков в этом мире в рандомные точки центра локации
        for (Player currentPlayer : world.getPlayers()) {
            if (currentPlayer.getWorld().getName().equals(lowerPlayerName)) {
                Random random = new Random();
                double min = -250;
                double max = 250;
                double offsetX = min + (max - min) * random.nextDouble();
                double offsetZ = min + (max - min) * random.nextDouble();

                Location spawnLocation = world.getSpawnLocation();

                double newX = spawnLocation.getX() + offsetX;
                double newZ = spawnLocation.getZ() + offsetZ;

                double newY = currentPlayer.getWorld().getHighestBlockYAt((int) newX, (int) newZ) + 2;
                // Создаем новую локацию с использованием полученной высоты
                Location teleportLocation = new Location(currentPlayer.getWorld(), newX, newY, newZ);
                currentPlayer.teleport(teleportLocation);
//                player.getInventory().clear();
                currentPlayer.setGameMode(GameMode.SURVIVAL);
                currentPlayer.setHealth(20.0);
                currentPlayer.setFoodLevel(20);
                currentPlayer.setSaturation(20.0f);
            }
        }


        // Начинаем сужение барьера
        worldBorder.setSize(50.0, barrierTime);

        return true;
    }
}
