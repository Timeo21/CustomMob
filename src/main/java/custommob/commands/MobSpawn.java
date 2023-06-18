package main.java.custommob.commands;

import main.java.custommob.CustomMob;
import main.java.custommob.mobs.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MobSpawn implements CommandExecutor {
    CustomMob plugin;

    public MobSpawn(CustomMob plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p){
            if (args.length != 1){
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "alphawolf" -> {
                    Bukkit.broadcastMessage("Spawning Alpha Wolf");
                    AlphaWolf.create(p.getLocation());
                    return true;
                }
                case "broodmother" -> {
                    Bukkit.broadcastMessage("Spawning Broodmother");
                    Broodmother.create(p.getLocation());
                    return true;
                }
                case "turret" -> {
                    Bukkit.broadcastMessage("Spawning Turret");
                    Turret.create(p.getLocation());
                    return true;
                }
                case "wildfire" -> {
                    Bukkit.broadcastMessage("Spawning Wildfire");
                    Wildfire.create(p.getLocation());
                    return true;
                }
                case "vampire" -> {
                    Bukkit.broadcastMessage("Spawning Vampire");
                    Vampire.create(p.getLocation());
                    return true;
                }
                default -> p.sendMessage("Invalid");
            }

        }
        return false;
    }
}
