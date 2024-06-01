package org.ynovka.ru.spellswar.characters;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class resetRoles {
    // в /stopgame вызываем этот класс для удаления ролей у игроков
    // Тут мы перебираем всех игроков, и смотрим их /team в зависимости от названия команды мы вызываем функцию removeRole соответствующего класса персонажа.
    // мы удаляем игроков из команд в /team и усё
    public static void resetroles(String currentLowerPlayerName, World world) {
        for (Player currentPlayer : world.getPlayers()) {
            if (currentPlayer.getWorld().getName().equals(currentLowerPlayerName)) {
                Scoreboard scoreboard = currentPlayer.getScoreboard();
                for (Team team : scoreboard.getTeams()) {
                    if (team.hasEntry(currentPlayer.getName())) {
                        String currentPlayerTeam = team.getName();

                        if (currentPlayerTeam.contains("invisibleMan")) {
                            Bukkit.broadcastMessage("invisibleMan - removed");
                            new invisibleMan().removeRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("lightningMan")) {
                            Bukkit.broadcastMessage("lightningMan - removed");
                            new lightningMan().removeRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("minerMan")) {
                            Bukkit.broadcastMessage("minerMan - removed");
                            new minerMan().removeRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("waterMan")) {
                            Bukkit.broadcastMessage("waterMan - removed");
                            new waterMan().removeRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("elfMan")) {
                            Bukkit.broadcastMessage("elfMan - removed");
                            new elfMan().removeRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("ironMan")) {
                            Bukkit.broadcastMessage("ironMan - removed");
                            new elfMan().removeRole(currentPlayer);
                        }
                        else {
                            // что то
                        }
                    }
                }
            }
        }
    }
}
