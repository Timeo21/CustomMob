package main.java.custommob.events;

import main.java.custommob.mobs.AlphaWolf;
import main.java.custommob.mobs.Broodmother;
import main.java.custommob.mobs.Turret;
import main.java.custommob.mobs.Wildfire;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class UseSpawnEgg implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND)){
                if(event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getLore() != null){
                    Location spawnLocation;
                    if(event.getClickedBlock().isPassable()){
                        spawnLocation = event.getClickedBlock().getLocation().add(0.5,0,0.5);
                    } else {
                        spawnLocation = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().add(0.5,0,0.5);
                    }
                    event.setCancelled(true);
                    switch (event.getItem().getItemMeta().getLore().get(0)) {
                        case "Spawns Alpha Wolf" -> AlphaWolf.create(spawnLocation);
                        case "Spawns Broodmother" -> Broodmother.create(spawnLocation);
                        case "Spawns Turret" -> Turret.create(spawnLocation);
                        case "Spawns Wildfire" -> Wildfire.create(spawnLocation);
                        default -> {
                        }
                    }
                }
            }
        }
    }
}
