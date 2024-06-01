package org.ynovka.ru.spellswar.events;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.ynovka.ru.spellswar.characters.*;

import org.bukkit.*;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.ynovka.ru.spellswar.coolDown;

public class events implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getName().equals("world")) {
            player.setGameMode(GameMode.ADVENTURE);
        }
        else {
            player.setGameMode(GameMode.SPECTATOR);
        }

        // Проверяем наличие победителя
        String worldName = player.getWorld().getName();

        int survivalPlayers = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(worldName) && p.getGameMode() == GameMode.SURVIVAL) {
                survivalPlayers++;
            }
        }

        // Если остался только один игрок с режимом выживания, объявляем его победителем
        if (survivalPlayers == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().getName().equals(worldName) && p.getGameMode() == GameMode.SURVIVAL) {
                    // Завершаем игру и объявляем победителя
                    for (Player playerToTeleport : Bukkit.getOnlinePlayers()) {
                        if (playerToTeleport.getWorld().getName().equals(worldName)) {
                            World World = Bukkit.getWorld("world");
                            assert World != null;
                            playerToTeleport.teleport(World.getSpawnLocation());
                        }
                    }
                    Bukkit.broadcastMessage("Игрок " + p.getName() + " победил в лобби " + worldName + "!");
                    // Здесь вы можете выполнить другие действия, связанные с завершением игры
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv delete " + worldName);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv confirm");
                }
            }
        }
    }








    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        Scoreboard scoreboard = player.getScoreboard();
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                String currentPlayerTeam = team.getName();
                if (currentPlayerTeam.contains("invisibleMan")) {
                    event.setCancelled(true); // Отменяем попытку взять предмет в другую руку
                    if (coolDown.tryCooldown(player, "PressF", 100)) {
                        invisibleMan.onPressF(player);
                    } else {}
                }

                if (currentPlayerTeam.contains("lightningMan")) {
                    event.setCancelled(true); // Отменяем попытку взять предмет в другую руку
                    if (coolDown.tryCooldown(player, "PressF", 100)) {
                        lightningMan.onPressF(player);
                    } else {}
                }

                if (currentPlayerTeam.contains("minerMan")) {
                    event.setCancelled(true); // Отменяем попытку взять предмет в другую руку
                    if (coolDown.tryCooldown(player, "PressF", 100)) {
                        minerMan.onPressF(player);
                    } else {}
                }

                if (currentPlayerTeam.contains("waterMan")) {
                    event.setCancelled(true); // Отменяем попытку взять предмет в другую руку
                    if (coolDown.tryCooldown(player, "PressF", 100)) {
                        waterMan.onPressF(player);
                    } else {}
                }
            }
        }
    }


    @EventHandler
    public void onInventoryClick(PlayerMoveEvent event) {
        Player player = (Player) event.getPlayer();
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getEntryTeam(player.getName());
        if (team != null && team.getName().equalsIgnoreCase("invisibleMan")) {
            ItemStack[] armorContents = player.getInventory().getArmorContents();
            boolean slow = false;
            for (ItemStack item : armorContents) {
                if (item != null && !item.getType().isAir()) {
                    player.setWalkSpeed(0.04F);
                    slow = true;
                }
                else {
                    if (slow) {
                        break;
                    }
                    else {
                        player.setWalkSpeed(0.2F);
                    }
                }
            }
        }


        if (team != null && team.getName().equalsIgnoreCase("ironMan")) {
            ItemStack[] armorContents = player.getInventory().getArmorContents();
            for (ItemStack item : armorContents) {
                if (item != null && !item.getType().isAir()) {
                    if (item.getType() == Material.IRON_HELMET) {
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                    }
                    else if (item.getType() == Material.IRON_CHESTPLATE) {
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                    }
                    else if (item.getType() == Material.IRON_LEGGINGS) {
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                    }
                    else if (item.getType() == Material.IRON_BOOTS) {
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                    }
                    else {
                        Inventory inventory = player.getInventory();
                        inventory.remove(item);
                    }
                }
            }
        }
    }
}