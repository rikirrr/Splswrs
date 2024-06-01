package org.ynovka.ru.spellswar.characters;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.ynovka.ru.spellswar.SpellsWar;
import org.ynovka.ru.spellswar.coolDown;

public class ironMan extends coolDown implements Listener {

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Scoreboard scoreboard = player.getScoreboard();
            Team team = scoreboard.getEntryTeam(player.getName());
            if (team != null && team.getName().equalsIgnoreCase("ironMan")) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL && player.equals(player)) {
                    event.setCancelled(true); // Отменяем урон от падения для игрока player
                }
            }
        }
    }

    public void giveRole(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setInvisible(false);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20.0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2 * 20, 128, false, false));

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        // Накладываем проклятье утраты и несъемность на нагрудник
        chestplate.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        chestplate.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        // Надеваем нагрудник на игрока
        player.getInventory().setChestplate(chestplate);
    }

    public static void onPressF(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.BLAZE_ROD) {
            if (coolDown.tryCooldown(player, "BLAZE_ROD", 30000)) {
                // активируем способность 1
                ItemStack[] armorContents = player.getInventory().getArmorContents();
                for (ItemStack item2 : armorContents) {
                    if (item2 != null && !item2.getType().isAir()) {
                        if (item2.getType() == Material.IRON_HELMET) {
                            ItemStack newHelmet = new ItemStack(Material.NETHERITE_HELMET);
                            player.getInventory().setHelmet(newHelmet);

                            Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                                ItemStack oldHelmet = new ItemStack(Material.IRON_HELMET);
                                player.getInventory().setHelmet(oldHelmet);
                            }, 10 * 20);
                        }
                        else if (item2.getType() == Material.IRON_CHESTPLATE) {
                            ItemStack newHelmet = new ItemStack(Material.NETHERITE_CHESTPLATE);
                            player.getInventory().setHelmet(newHelmet);

                            Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                                ItemStack oldHelmet = new ItemStack(Material.IRON_CHESTPLATE);
                                player.getInventory().setHelmet(oldHelmet);
                            }, 10 * 20);
                        }
                        else if (item2.getType() == Material.IRON_LEGGINGS) {
                            ItemStack newHelmet = new ItemStack(Material.NETHERITE_LEGGINGS);
                            player.getInventory().setHelmet(newHelmet);

                            Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                                ItemStack oldHelmet = new ItemStack(Material.IRON_LEGGINGS);
                                player.getInventory().setHelmet(oldHelmet);
                            }, 10 * 20);
                        }
                        else if (item2.getType() == Material.IRON_BOOTS) {
                            ItemStack newHelmet = new ItemStack(Material.NETHERITE_BOOTS);
                            player.getInventory().setHelmet(newHelmet);

                            Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                                ItemStack oldHelmet = new ItemStack(Material.IRON_BOOTS);
                                player.getInventory().setHelmet(oldHelmet);
                            }, 10 * 20);
                        }
                        else {
                            // nothing
                        }
                    }
                }
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 1! Подождите: " + (coolDown.getCooldown(player, "BLAZE_ROD") / 1000) + "секунд!");
            }
        }
        else if (item.getType() == Material.BLAZE_POWDER) {
            if (coolDown.tryCooldown(player, "BLAZE_POWDER", 60000)) {
                // активируем способность 2 (ульта)
                for (Player target : player.getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 10 && !target.equals(player)) {
                        Location teleportLocation = new Location(target.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 15, player.getLocation().getZ());

                        target.teleport(teleportLocation);
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
