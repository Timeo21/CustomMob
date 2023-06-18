package main.java.custommob.mobs;

import main.java.custommob.CustomMob;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftCaveSpider;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Broodmother implements Listener {
    static CustomMob plugin;
    static double health = 200;
    static double armor = 10;
    static double armorTough = 10;
    static double speed = 0.1;

    public Broodmother(CustomMob plugin) {
        this.plugin = plugin;
    }

    public static void create(Location location){
        //Spawn mob
        Spider broodMother = location.getWorld().spawn(location,Spider.class);
        BossBar bossBar = Bukkit.createBossBar(ChatColor.DARK_GREEN+"Broodmother",BarColor.GREEN,BarStyle.SEGMENTED_12);
        bossBar.setVisible(true);
        bossBar.setProgress(1);
        //Set name
        broodMother.setCustomName(ChatColor.DARK_GREEN +"Broodmother");
        broodMother.setCustomNameVisible(true);
        broodMother.setMetadata("name", new FixedMetadataValue(plugin,broodMother.getCustomName()));
        //set properties
        broodMother.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        broodMother.setHealth(health);
        broodMother.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);
        broodMother.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(armorTough);
        broodMother.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        broodMother.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(10);
        broodMother.setAI(false);
        broodMother.setPersistent(true);

        //set Runnable
        ArrayList<CaveSpider> babySpiders = new ArrayList<>();
        new BukkitRunnable(){
            public void run() {
                int maxSpider = 3;
                if (broodMother.getHealth() < health/4*3) maxSpider = 5;
                if (broodMother.getHealth() < health/2) maxSpider = 8;
                if (!broodMother.isDead()){
                    babySpiders.removeIf(Entity::isDead);
                    while (babySpiders.size() < maxSpider){
                        CaveSpider spider = broodMother.getWorld().spawn(broodMother.getLocation(),CaveSpider.class);
                        babySpiders.add(spider);
                        spider.setCustomName(ChatColor.GREEN+"Baby Spider");
                        spider.setCustomNameVisible(true);
                        spider.setMetadata("name", new FixedMetadataValue(plugin,spider.getCustomName()));
                        spider.setHealth(10);
                    }
                    for (CaveSpider spider : babySpiders){
                        if (spider.getTarget() == null){
                            if (broodMother.getTarget() != null) {
                            spider.setTarget(broodMother.getTarget());
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5 * 20L);

        new BukkitRunnable(){
            public void run() {
                if (broodMother.getHealth() < health/3){
                    broodMother.setAI(true);
                }

                for (CaveSpider spider : babySpiders){
                    if (spider.getLocation().distanceSquared(broodMother.getLocation()) > 10*10){
                        CraftCaveSpider craftCaveSpider = (CraftCaveSpider) spider;
                        net.minecraft.world.entity.monster.CaveSpider entityCaveSpider = craftCaveSpider.getHandle();

                        Location targetLocation = broodMother.getLocation();
                        Path path = entityCaveSpider.getNavigation().createPath(targetLocation.getX()+1,targetLocation.getY(),targetLocation.getZ()+1,1);
                        entityCaveSpider.getNavigation().moveTo(path,1.0D);
                        entityCaveSpider.getNavigation().setSpeedModifier(1.0D);
                    } else {
                        for (Entity entity : broodMother.getNearbyEntities(10,10,10)){
                            if (entity instanceof Player player) spider.setTarget(player);
                        }
                    }
                    if (broodMother.isDead()) spider.setHealth(0);
                }
                if (broodMother.isDead() || !broodMother.isValid()) cancel();

            }
        }.runTaskTimer(plugin, 0L, 20L);

        new BukkitRunnable(){
            public void run(){
                bossBar.setProgress(broodMother.getHealth()/health);
                bossBar.removeAll();
                if (broodMother.isDead()) cancel();
                for (Entity entity : broodMother.getNearbyEntities(30,15,30)){
                    if (entity instanceof Player player){
                        bossBar.addPlayer(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof CaveSpider caveSpider){
            if (caveSpider.hasMetadata("name") && caveSpider.getMetadata("name").get(0).asString().equals(ChatColor.GREEN+"Baby Spider")){
                player.sendMessage("Poisoned");
                PotionEffect potionEffect = new PotionEffect(PotionEffectType.POISON,3*20,0);
                player.addPotionEffect(potionEffect);
            }
        }
    }
}