package org.ynovka.ru.spellswar.characters;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.ynovka.ru.spellswar.coolDown;

public class minerMan extends coolDown implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getEntryTeam(player.getName());
        if (team != null && team.getName().equalsIgnoreCase("minerMan")) {
            Block block = event.getBlock();
            if (block.getType() == Material.STONE || block.getType() == Material.DEEPSLATE) {
                if (Math.random() <= 0.07) {
                    Location loc = block.getLocation();
                    loc.add(0.5, 0.5, 0.5); // Перемещаем в центр блока
                    TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
                    tnt.setYield(7); // Предотвращаем нанесение урона игроку
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 255 , false, false));
                    tnt.setFuseTicks(0); // Устанавливаем таймер взрыва на 0 тиков (мгновенный взрыв)
                    block.setType(Material.AIR); // Удаляем блок камня
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

    public static boolean isOre(Material material) {
        // Проверяем, является ли материал рудой
        switch (material) {
            case COAL_ORE:
            case IRON_ORE:
            case GOLD_ORE:
            case REDSTONE_ORE:
            case LAPIS_ORE:
            case EMERALD_ORE:
            case DIAMOND_ORE:

            case DEEPSLATE_COAL_ORE:
            case DEEPSLATE_IRON_ORE:
            case DEEPSLATE_GOLD_ORE:
            case DEEPSLATE_REDSTONE_ORE:
            case DEEPSLATE_LAPIS_ORE:
            case DEEPSLATE_EMERALD_ORE:
            case DEEPSLATE_DIAMOND_ORE:

            case COAL_BLOCK:
            case IRON_BLOCK:
            case GOLD_BLOCK:
            case REDSTONE_BLOCK:
            case LAPIS_BLOCK:
            case EMERALD_BLOCK:
            case DIAMOND_BLOCK:
                return true;
            default:
                return false;
        }
    }

    public static void onPressF(Player player) {
        Material material;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BLAZE_ROD) {
            if (coolDown.tryCooldown(player, "BLAZE_ROD", 30000)) {
                // активируем способность 1
                World world = player.getWorld();
                Location loc = player.getLocation();
                Block underplayer = player.getLocation().subtract(0, 1, 0).getBlock();
                if (underplayer.getType().equals(Material.AIR)){material = Material.DIRT;}else {
                    material = underplayer.getType();
                    if (isOre(material)) {
                        material = Material.DIRT;
                    }
                }

                // Проверяем, что все 10 блоков над игроком - воздух или вода
                boolean clearAbove = true;
                for (int yOffset = 1; yOffset <= 10; yOffset++) {
                    Block blockAbove = loc.clone().add(0, yOffset, 0).getBlock();
                    if (blockAbove.getType() != Material.AIR && !blockAbove.isLiquid()) {
                        clearAbove = false;
                        coolDown.setCooldown(player, "BLAZE_ROD", 1);
                        break;
                    }
                }

                if (clearAbove) {
                    // Создаем столб 3x3x10
                    for (int y = 0; y < 10; y++) {
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                Location currentLoc = loc.clone().add(x, y, z); // Создаем новую локацию
                                Block block = world.getBlockAt(currentLoc);
                                block.setType(material); // Устанавливаем блок земли
                            }
                        }
                    }

                    // Телепортируем игрока на верхний блок столба
                    Location pillarTop = loc.clone().add(0, 10, 0);
                    player.teleport(pillarTop);
                }
                else {
                    player.sendMessage("Вы не можете создать столб земли, когда над вами есть блоки!");
                }
            }
            else {
                player.sendMessage("У Вас задержка на использование способности 1! Подождите: " + (coolDown.getCooldown(player, "BLAZE_ROD") / 1000) + "секунд!");
            }
        }
        else if (item.getType() == Material.BLAZE_POWDER) {
            if (coolDown.tryCooldown(player, "BLAZE_POWDER", 60000)) {
                // активируем способность 2 (ульта)
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 7 * 20, 6, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7 * 20, 6, false, false));

                for (Player target : player.getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 6 && !target.equals(player)) {
                        double health = target.getHealth();
                        double halfHealth = health / 2.0;
                        double damage = health - halfHealth;
                        target.damage(damage);
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
