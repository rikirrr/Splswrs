package org.ynovka.ru.spellswar.characters;
// в /startgame мы присваиваем игроков в команды и вызываем этот класс для установки ролей у игроков

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class setRoles {
    public static void setroles(String currentLowerPlayerName, World world) {
        for (Player currentPlayer : world.getPlayers()) {
            if (currentPlayer.getWorld().getName().equals(currentLowerPlayerName)) {
                Scoreboard scoreboard = currentPlayer.getScoreboard();
                for (Team team : scoreboard.getTeams()) {
                    if (team.hasEntry(currentPlayer.getName())) {
                        String currentPlayerTeam = team.getName();

                        if (currentPlayerTeam.contains("invisibleMan")) {
                            Bukkit.broadcastMessage("invisibleMan");
                            new invisibleMan().giveRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("lightningMan")) {
                            Bukkit.broadcastMessage("lightningMan");
                            new lightningMan().giveRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("minerMan")) {
                            Bukkit.broadcastMessage("minerMan");
                            new minerMan().giveRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("waterMan")) {
                            Bukkit.broadcastMessage("waterMan");
                            new waterMan().giveRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("elfMan")) {
                            Bukkit.broadcastMessage("elfMan");
                            new elfMan().giveRole(currentPlayer);
                        }
                        else if (currentPlayerTeam.contains("ironMan")) {
                            Bukkit.broadcastMessage("ironMan");
                            new elfMan().giveRole(currentPlayer);
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