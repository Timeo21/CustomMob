package main.java.custommob.mobs;

import main.java.custommob.CustomMob;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Turret implements Listener {
    static CustomMob plugin;
    static double health = 40;
    static double armor = 20;
    static double armorToug = 10;

    public Turret(CustomMob plugin) {
        this.plugin = plugin;
    }

    public static void create(Location location) {
        Skeleton turret = location.getWorld().spawn(location, Skeleton.class);

        turret.setCustomName(ChatColor.GRAY + "Turret");
        turret.setCustomNameVisible(true);
        turret.setMetadata("name", new FixedMetadataValue(plugin, turret.getCustomName()));

        turret.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        turret.setHealth(health);
        turret.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);
        turret.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(armorToug);
        turret.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.00001D);
        turret.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1000);

        new BukkitRunnable(){
            public void run() {
                double speed = 3;
                if (turret.getTarget() != null){
                    Vector direction = turret.getTarget().getLocation().toVector().subtract(turret.getLocation().toVector());
                    Arrow arrow = turret.launchProjectile(Arrow.class);
                    arrow.setBounce(false);
                    arrow.setVelocity(new Vector(direction.getX(), direction.getY(), direction.getZ()).normalize().multiply(speed));
                } else {
                    for (Entity entity : turret.getNearbyEntities(25,15,25)){
                        if (entity instanceof Player player){
                            turret.setTarget(player);
                        }
                    }
                }
                if (turret.isDead()) cancel();
            }
        }.runTaskTimer(plugin, 0L, 3*10L);
    }
    @EventHandler
    public void onShootEvent(EntityShootBowEvent event){
        if(event.getEntity().hasMetadata("name") && event.getEntity().getMetadata("name").get(0).asString().equals(ChatColor.GRAY + "Turret")){
            event.setCancelled(true);
        }
    }
}