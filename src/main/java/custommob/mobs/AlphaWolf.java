package main.java.custommob.mobs;

import main.java.custommob.CustomMob;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftWolf;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class AlphaWolf implements Listener{
    static double health = 20;
    static double armor = 5;
    static CustomMob plugin;

    public AlphaWolf(CustomMob plugin) {
        this.plugin = plugin;
    }

    public static void create(Location location) {
        // Create mob
        Wolf alphaWolf = location.getWorld().spawn(location, Wolf.class);
        // Set name
        alphaWolf.setCustomName(ChatColor.DARK_RED + "Alpha Wolf");
        alphaWolf.setCustomNameVisible(true);
        alphaWolf.setMetadata("name", new FixedMetadataValue(plugin, alphaWolf.getCustomName()));
        // Set properties
        alphaWolf.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);
        alphaWolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        alphaWolf.setHealth(health);
        alphaWolf.setAngry(true);

         CraftWolf craftWolf =(CraftWolf) alphaWolf;
         net.minecraft.world.entity.animal.Wolf wolfEntity = craftWolf.getHandle();

        // Set runnable
        ArrayList<Wolf> wolves = new ArrayList<>();
        new BukkitRunnable() {
            public void run() {
                if (!alphaWolf.isDead()) {
                    wolves.removeIf(Entity::isDead);
                    if (alphaWolf.getTarget() == null) {
                        for (Entity entity : alphaWolf.getNearbyEntities(20, 10, 20)) {
                            if (entity instanceof Player player) {
                                alphaWolf.setTarget(player);
                            }
                        }
                    } else {
                        LivingEntity target = alphaWolf.getTarget();
                        if (target.getLocation().distanceSquared(alphaWolf.getLocation()) > 15*15) {
                            alphaWolf.setTarget(null);
                        }
                    }

                    while (wolves.size() < 5) {
                        Wolf wolf = alphaWolf.getWorld().spawn(alphaWolf.getLocation(), Wolf.class);
                        wolf.setCustomName(ChatColor.RED + "Pup");
                        wolf.setCustomNameVisible(true);
                        wolf.setBaby();
                        wolf.setMetadata("name", new FixedMetadataValue(plugin, wolf.getCustomName()));
                        if (alphaWolf.getTarget() != null) wolf.setTarget(alphaWolf.getTarget());
                        wolves.add(wolf);
                    }

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 10 * 20L);


        new BukkitRunnable() {
            public void run() {
                if (alphaWolf.isDead()) {
                    for (Wolf wolf : wolves) {
                        if (alphaWolf.getTarget() != null) wolf.setTarget(alphaWolf.getTarget());
                        wolf.setBaby();
                        wolf.setAngry(true);
                        if (!wolf.isDead()) {
                            wolf.setAdult();
                            wolf.setCustomName(ChatColor.RED + "Angry Wolf");
                            wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
                            wolf.setHealth(wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                            wolf.setAngry(true);
                            wolf.setMetadata("name", new FixedMetadataValue(plugin, wolf.getCustomName()));
                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}