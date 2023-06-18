package main.java.custommob.commands;

import main.java.custommob.items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveEgg implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player){
            if (player.isOp()){
                if (args.length == 1){
                    switch (args[0]){
                        case "alphawolf":
                            player.getInventory().addItem(ItemManager.alphaWolfEgg);
                            break;
                        case "broodmother":
                            player.getInventory().addItem(ItemManager.broodMotherEgg);
                            break;
                        case "turret":
                            player.getInventory().addItem(ItemManager.turretEgg);
                            break;
                        case "wildfire":
                            player.getInventory().addItem(ItemManager.wildfireEgg);
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
