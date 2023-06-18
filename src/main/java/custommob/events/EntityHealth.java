package main.java.custommob.events;


import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityHealth implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof LivingEntity entity){
            if (entity.hasMetadata("name")){
                String name = entity.getMetadata("name").get(0).asString();
                int health = (int) Math.round(entity.getHealth()-event.getFinalDamage());
                if (health < 0) health = 0;
                name = name  +" "+ ChatColor.RED + health + "♥";
                entity.setCustomName(name);
            }
        }
    }
}
