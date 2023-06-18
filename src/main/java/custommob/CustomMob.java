package main.java.custommob;


import main.java.custommob.commands.GiveEgg;
import main.java.custommob.commands.MobSpawn;
import main.java.custommob.commands.RemoveCustomMob;
import main.java.custommob.commands.pigspawn;
import main.java.custommob.events.EntityHealth;
import main.java.custommob.events.UseSpawnEgg;
import main.java.custommob.items.ItemManager;
import main.java.custommob.mobs.*;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomMob extends JavaPlugin {

    @Override
    public void onEnable() {
        ItemManager.init();

        this.getCommand("mobspawn").setExecutor(new MobSpawn(this));
        this.getCommand("giveegg").setExecutor(new GiveEgg());
        this.getCommand("pigspawn").setExecutor(new pigspawn(this));
        this.getCommand("killmob").setExecutor(new RemoveCustomMob());

        getServer().getPluginManager().registerEvents(new AlphaWolf(this),this);
        getServer().getPluginManager().registerEvents(new Broodmother(this),this);
        getServer().getPluginManager().registerEvents(new Turret(this),this);
        getServer().getPluginManager().registerEvents(new Wildfire(this),this);
        getServer().getPluginManager().registerEvents(new Vampire(this),this);
        getServer().getPluginManager().registerEvents(new UseSpawnEgg(),this);
        getServer().getPluginManager().registerEvents(new EntityHealth(),this);


    }

    @Override
    public void onDisable() {

    }
}
