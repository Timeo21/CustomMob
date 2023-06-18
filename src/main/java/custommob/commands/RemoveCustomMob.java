package main.java.custommob.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RemoveCustomMob implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && sender instanceof Player player){
            World world = player.getWorld();
            int a;
            try {
                a = Integer.parseInt(args[0]);
            }catch (Exception e){
                a = 20;
            }
            for (Entity entity : player.getNearbyEntities(a,a,a)) {
                if (entity.hasMetadata("name")) {
                    entity.remove();
                }
            }
        }
        return false;
    }
}
