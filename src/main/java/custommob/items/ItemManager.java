package main.java.custommob.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack alphaWolfEgg;
    public static ItemStack broodMotherEgg;
    public static ItemStack turretEgg;
    public static ItemStack wildfireEgg;

    public static void init(){
        createAlphaWolfEgg();
        createBroodMotherEgg();
        createTurretEgg();
        createWildfireEgg();
    }

    private static void createAlphaWolfEgg(){
        ItemStack item = new ItemStack(Material.WOLF_SPAWN_EGG,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED+"Alpha Wolf Egg");
        List<String> lore = new ArrayList<>();
        lore.add("Spawns Alpha Wolf");
        meta.setLore(lore);
        item.setItemMeta(meta);
        alphaWolfEgg = item;
    }
    private static void createBroodMotherEgg() {
        ItemStack item = new ItemStack(Material.SPIDER_SPAWN_EGG,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN+"Broodmother Egg");
        List<String> lore = new ArrayList<>();
        lore.add("Spawns Broodmother");
        meta.setLore(lore);
        item.setItemMeta(meta);
        broodMotherEgg = item;
    }
    private static void createTurretEgg() {
        ItemStack item = new ItemStack(Material.SKELETON_SPAWN_EGG,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY+"Turret Egg");
        List<String> lore = new ArrayList<>();
        lore.add("Spawns Turret");
        meta.setLore(lore);
        item.setItemMeta(meta);
        turretEgg = item;
    }
    private static void createWildfireEgg() {
        ItemStack item = new ItemStack(Material.BLAZE_SPAWN_EGG,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD +"Wildfire Egg");
        List<String> lore = new ArrayList<>();
        lore.add("Spawns Wildfire");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wildfireEgg = item;
    }
}
