package org.ynovka.ru.spellswar.characters;

import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.ynovka.ru.spellswar.coolDown;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.ynovka.ru.spellswar.SpellsWar;

public class invisibleMan extends coolDown implements Listener {

    @EventHandler
    public void onSneaking(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getEntryTeam(player.getName());
        if (team != null && team.getName().equalsIgnoreCase("invisibleMan")) {
            if (event.isSneaking()) {
                player.setWalkSpeed(0.5F);
            }
            else {
                player.setWalkSpeed(0.2F);
            }
        }
    }


    public void giveRole(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26.0);
        player.setHealth(26.0);
        player.setInvisible(true); // Сделать игрока невидимым
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2 * 20, 128, false, false));
    }

    public static void onPressF(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        String playerName = player.getName();

        if (item.getType() == Material.BLAZE_ROD) {
            if (coolDown.tryCooldown(player, "BLAZE_ROD", 30000)) {
                // активируем способность 1
                player.sendMessage("Активирована способность 1");
                Bukkit.dispatchCommand(player, "npc create " + playerName);
                Bukkit.dispatchCommand(player, "npc skin " + playerName + " " + playerName);
                Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                    Bukkit.dispatchCommand(player, "npc remove " + playerName);
                }, 10 * 20);
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 1! Подождите: " + (coolDown.getCooldown(player, "BLAZE_ROD") / 1000) + "секунд!");
            }
        }
        else if (item.getType() == Material.BLAZE_POWDER) {
            if (coolDown.tryCooldown(player, "BLAZE_POWDER", 60000)) {
                // активируем способность 2 (ульта)
                player.sendMessage("Активирована способность 2");
                for (Player target : player.getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 10 && !target.equals(player)) {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 11 * 20, 1, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 11 * 20, 127, false, false));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 11 * 20, 4, false, false));
                    }
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 18 * 20, 10, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 11 * 20, 10, false, false));
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 2! Подождите: " + (coolDown.getCooldown(player, "BLAZE_POWDER") / 1000) + "секунд!");
            }
        }
    }

    public void removeRole(Player player) {
        player.getActivePotionEffects().clear();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20.0);
        player.setInvisible(false); // Сделать игрока видимым
    }
}
