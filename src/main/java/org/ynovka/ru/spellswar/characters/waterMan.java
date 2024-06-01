package org.ynovka.ru.spellswar.characters;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ynovka.ru.spellswar.SpellsWar;
import org.ynovka.ru.spellswar.coolDown;


public class waterMan extends coolDown implements Listener {

    public void giveRole(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setInvisible(false);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20.0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, PotionEffect.INFINITE_DURATION, 128, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, PotionEffect.INFINITE_DURATION, 1, false, false));
    }

    public static void onPressF(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BLAZE_ROD) {
            if (coolDown.tryCooldown(player, "BLAZE_ROD", 30000)) {
                player.setInvisible(true);
                Location playerLocation = player.getLocation();
                for (int i = 0; i < 15; i++) {
                    double randomX = playerLocation.getX() + Math.random() * 10 - 5; // Случайная позиция по X
                    double randomY = playerLocation.getY() + Math.random() * 4 - 2; // Случайная позиция по Y
                    double randomZ = playerLocation.getZ() + Math.random() * 10 - 5; // Случайная позиция по Z
                    Location fishLocation = new Location(player.getWorld(), randomX, randomY, randomZ);
                    Fish fish = (Fish) player.getWorld().spawnEntity(fishLocation, EntityType.COD);
                }
                Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                    player.setInvisible(false);
                }, 10 * 20);
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 1! Подождите: " + (coolDown.getCooldown(player, "BLAZE_ROD") / 1000) + "секунд!");
            }
        }
        else if (item.getType() == Material.BLAZE_POWDER) {
            if (coolDown.tryCooldown(player, "BLAZE_POWDER", 60000)) {
                // активируем способность 2 (ульта)
                for (Player target : player.getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 7 && !target.equals(player)) {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 2, false, false));
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
        player.setInvisible(false); // Сделать игрока видимым
    }
}
