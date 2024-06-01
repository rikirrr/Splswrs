package org.ynovka.ru.spellswar.characters;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ynovka.ru.spellswar.coolDown;

public class elfMan extends coolDown implements Listener {


    public void giveRole(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setInvisible(false);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
        player.setHealth(40.0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2 * 20, 128, false, false));
    }

    public static void onPressF(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BLAZE_ROD) {
            if (coolDown.tryCooldown(player, "BLAZE_ROD", 30000)) {
                // активируем способность 1
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 1, false, false));
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 1! Подождите: " + (coolDown.getCooldown(player, "BLAZE_ROD") / 1000) + "секунд!");
            }
        }
        else if (item.getType() == Material.BLAZE_POWDER) {
            if (coolDown.tryCooldown(player, "BLAZE_POWDER", 60000)) {
                // активируем способность 2 (ульта)
                for (int i = 0; i < 8; i++) {
                    Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
                    wolf.setAngry(true); // Устанавливаем собаку в агрессивное состояние
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!onlinePlayer.equals(player)) {
                            wolf.setTarget(onlinePlayer); // Устанавливаем целью собаки игрока, кроме указанного
                        }
                    }
                }
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
    }
}
