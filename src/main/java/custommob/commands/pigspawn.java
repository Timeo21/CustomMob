package main.java.custommob.commands;

import main.java.custommob.CustomMob;
import main.java.custommob.mobs.CustomPig;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;

public class pigspawn implements CommandExecutor {

    CustomMob plugin;

    public pigspawn(CustomMob plugin){
    this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            CustomPig customPig = new CustomPig(p.getLocation(), plugin);
            ServerLevel world = ((CraftWorld) p.getWorld()).getHandle();
            world.addFreshEntity(customPig);
        }
        return false;
    }
}
