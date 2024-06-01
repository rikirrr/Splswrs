package org.ynovka.ru.spellswar.characters;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.ynovka.ru.spellswar.SpellsWar;
import org.ynovka.ru.spellswar.coolDown;

public class lightningMan extends coolDown implements Listener {
    static Boolean storm;

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        Player player = event.getLightning().getCausingPlayer();
        if (player != null) {
            Scoreboard scoreboard = player.getScoreboard();
            Team team = scoreboard.getEntryTeam(player.getName());
            if (team != null && team.getName().equalsIgnoreCase("lightningMan")) {
                event.setCancelled(true); // Отменяем удар молнии, если цель - защищенный игрок
            }
        }
    }


    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {

        Entity entity = event.getDamager();

        if (entity instanceof Player) {
            Player player1 = (Player) event.getDamager();
            Scoreboard scoreboard = player1.getScoreboard();
            Team team = scoreboard.getEntryTeam(player1.getName());
            if (team != null && team.getName().equalsIgnoreCase("lightningMan")) {
                if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                    Player attacker = (Player) event.getDamager();
                    Player victim = (Player) event.getEntity();
                    if (attacker.getInventory().getItemInMainHand().getType().toString().contains("SWORD")) {
                        if (storm) {
                            if (Math.random() <= 0.2) {
                                World world = victim.getWorld();
                                world.strikeLightning(victim.getLocation());
                            }
                        }
                    }
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
    }

    public static void onPressF(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BLAZE_ROD) {
            if (coolDown.tryCooldown(player, "BLAZE_ROD", 30000)) {
                // активируем способность 1
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 6, false, false));
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 1! Подождите: " + (coolDown.getCooldown(player, "BLAZE_ROD") / 1000) + "секунд!");
            }
        }
        else if (item.getType() == Material.BLAZE_POWDER) {
            if (coolDown.tryCooldown(player, "BLAZE_POWDER", 60000)) {
                // активируем способность 2 (ульта)
                World world = player.getWorld();
                world.setStorm(true);
                world.setThundering(true);
                storm = true;
                Bukkit.getScheduler().runTaskLater(SpellsWar.getInstance(), () -> {
                    world.setStorm(false);
                    world.setThundering(false);
                    storm = false;
                }, 20 * 20);
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
