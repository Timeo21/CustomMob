package main.java.custommob.mobs;

import main.java.custommob.CustomMob;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;

public class Wildfire implements Listener {
    static double health = 200;
    static double armor = 15;
    static double armorToug = 5;
    static CustomMob plugin;


    public Wildfire(CustomMob plugin){
        this.plugin = plugin;
    }

    public static void create(Location location){
        World world = location.getWorld();
        Blaze wildFire = world.spawn(location,Blaze.class);
        BossBar bossBar = Bukkit.createBossBar(ChatColor.DARK_RED+"Wildfire", BarColor.RED, BarStyle.SEGMENTED_10);
        bossBar.setVisible(true);
        bossBar.setProgress(1);

        wildFire.setCustomName(ChatColor.GOLD+"Wildfire");
        wildFire.setCustomNameVisible(true);
        wildFire.setMetadata("name", new FixedMetadataValue(plugin,wildFire.getCustomName()));

        wildFire.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);
        wildFire.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        wildFire.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(armorToug);
        wildFire.setHealth(health);

        ArmorStand shield1 = world.spawn(wildFire.getLocation(),ArmorStand.class);
        ArmorStand shield2 = world.spawn(wildFire.getLocation(),ArmorStand.class);
        ArmorStand shield3 = world.spawn(wildFire.getLocation(),ArmorStand.class);
        ArmorStand shield4 = world.spawn(wildFire.getLocation(),ArmorStand.class);


        ArrayList<ArmorStand> shields = new ArrayList<>(){};
        shields.add(shield1); shields.add(shield2); shields.add(shield3); shields.add(shield4);

        ItemStack shieldItem = new ItemStack(Material.SHIELD,1);
        BlockStateMeta blockStateMeta = (BlockStateMeta) shieldItem.getItemMeta();
        Banner banner = (Banner) blockStateMeta.getBlockState();
        banner.setBaseColor(DyeColor.RED);
        banner.addPattern(new Pattern(DyeColor.YELLOW, PatternType.CURLY_BORDER));
        banner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.RHOMBUS_MIDDLE));
        banner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.TRIANGLES_BOTTOM));
        banner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.GRADIENT_UP));
        banner.addPattern(new Pattern(DyeColor.YELLOW, PatternType.GRADIENT));
        banner.update();
        blockStateMeta.setBlockState(banner);
        shieldItem.setItemMeta(blockStateMeta);


        final int[] phase = {1};

        for (ArmorStand shield : shields){
            shield.getEquipment().setItemInMainHand(shieldItem);
            shield.setCustomName("shield");
            shield.setGravity(false);
            shield.setInvisible(true);
            shield.setMetadata("name", new FixedMetadataValue(plugin, shield.getCustomName()));
            EulerAngle eulerAngle = new EulerAngle(Math.toRadians(90),0,Math.toRadians(10));
            shield.setRightArmPose(eulerAngle);
        }
        final int[] offset = {0};
        new BukkitRunnable(){
            public void run(){
                ArrayList<Location> locations = (ArrayList<Location>) getRotatedLocations(wildFire.getLocation(),0.5,32);
                for (ArmorStand shield : shields){
                    shield.teleport(locations.get(offset[0]));
                    shield.setRotation((360/32*(offset[0] +16)+80)%360,0);
                    offset[0] = (offset[0] +8)%32;
                }
                offset[0]++;
                if (wildFire.isDead()){
                    for (ArmorStand shield : shields){
                        shield.remove();
                    }
                    cancel();
                }
                if (wildFire.getHealth() < health/10*8 && phase[0] == 1){
                    Bukkit.broadcastMessage("Phase 1");
                    changePhase(phase, wildFire);
                } else if (wildFire.getHealth() < health/10*6 && phase[0] == 2){
                    Bukkit.broadcastMessage("Phase 2");
                    changePhase(phase, wildFire);
                } else if (wildFire.getHealth() < health/10*4 && phase[0] == 3){
                    Bukkit.broadcastMessage("Phase 3");
                    changePhase(phase, wildFire);
                } else if (wildFire.getHealth() < health/10*2 && phase[0] == 4){
                    Bukkit.broadcastMessage("Phase 4");
                    changePhase(phase, wildFire);
                }




            }
        }.runTaskTimer(plugin, 0L, 1L);
        new BukkitRunnable(){
            public void run(){
                bossBar.setProgress(wildFire.getHealth()/health);
                bossBar.removeAll();
                if (wildFire.isDead() || !wildFire.isValid()) cancel();
                for (Entity entity : wildFire.getNearbyEntities(30,15,30)){
                    if (entity instanceof Player player){
                        bossBar.addPlayer(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    public static void changePhase(int[] phase, Blaze wildfire){
        ArrayList<Blaze> guardingBlaze = new ArrayList<>();
        switch (phase[0]){
            case 4:
                createBlaze(wildfire,guardingBlaze);
                createBlaze(wildfire,guardingBlaze);
                createBlaze(wildfire,guardingBlaze);
            case 3:
                createBlaze(wildfire,guardingBlaze);
                createBlaze(wildfire,guardingBlaze);
            case 2:
                createBlaze(wildfire,guardingBlaze);
            case 1:
                createBlaze(wildfire,guardingBlaze);
                phase[0]++;
                double maxHealth = guardingBlaze.get(0).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()* guardingBlaze.size();
                wildfire.setInvulnerable(true);
                BossBar guardBar = Bukkit.createBossBar(ChatColor.RED+"Guarding Blaze", BarColor.RED, BarStyle.SOLID);
                guardBar.setVisible(true);
                guardBar.setProgress(1);
                new BukkitRunnable(){
                    public void run(){
                        for (Entity entity : wildfire.getNearbyEntities(30,15,30)){
                            if (entity instanceof Player player) guardBar.addPlayer(player);
                        }
                        double totalHealth = 0;
                        if (guardingBlaze.size() != 0) {
                            for (Blaze blaze : guardingBlaze){
                                if (blaze.isDead()) guardingBlaze.remove(blaze);
                                totalHealth += blaze.getHealth();
                            }
                        }
                        guardBar.setProgress(totalHealth/maxHealth);
                        if (totalHealth == 0) {
                            wildfire.setInvulnerable(false);
                            Bukkit.broadcastMessage("Guard Defeated");
                            guardBar.removeAll();
                            cancel();
                        }

                    }
                }.runTaskTimer(plugin, 0L, 1L);
        }

    }

    public static void createBlaze(Blaze wildfire, ArrayList<Blaze> guardingBlaze){
        Blaze blaze = wildfire.getWorld().spawn(wildfire.getLocation(),Blaze.class);
        blaze.setCustomName(ChatColor.GOLD+"Guarding Blaze");
        blaze.setCustomNameVisible(true);
        blaze.setMetadata("name", new FixedMetadataValue(plugin,blaze.getCustomName()));
        guardingBlaze.add(blaze);
    }






    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof ArmorStand && event.getEntity().hasMetadata("name")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof ArmorStand && event.getEntity().hasMetadata("name")){
            event.setCancelled(true);
        }
    }
    public static List<Location> getRotatedLocations(Location center, double radius, int amount) {
        List<Location> locations = new ArrayList<>();
        double tau = Math.PI*2;
        double increment = tau / amount;
        for(int i = 0; i <= amount; i++) {
            double angle = i * increment;
            double z = center.getZ() + radius * Math.sin(angle);
            double x = center.getX() + radius * Math.cos(angle);
                locations.add(new Location(center.getWorld(), x, center.getY()-0.8, z));
        }
        return locations;
    }
    @EventHandler
    public void onFireBallShoot(ProjectileLaunchEvent event){
        if (event.getEntity().getShooter() instanceof Blaze && event.getEntity() instanceof SmallFireball smallFireball){
            smallFireball.setMetadata("name",new FixedMetadataValue(plugin, "wildfireball"));
        }
    }
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        if (event.getEntity().hasMetadata("name") && event.getEntity().getMetadata("name").get(0).asString().equals("wildfireball")){
            event.setCancelled(true);
            event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(),2,true,false);
        }
    }
}
