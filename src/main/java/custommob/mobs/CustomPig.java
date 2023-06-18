package main.java.custommob.mobs;

import main.java.custommob.CustomMob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.metadata.FixedMetadataValue;

public class CustomPig extends Husk {
    CustomMob plugin;
    public CustomPig(Location location, CustomMob plugin) {
        super(EntityType.HUSK, ((CraftWorld) location.getWorld()).getHandle());
        this.setPos(location.getX(), location.getY(), location.getZ());

        this.plugin = plugin;

        this.setBaby(true);
        this.setCustomName(Component.nullToEmpty(ChatColor.YELLOW+"Husky"));
        this.setCustomNameVisible(true);

        this.getBukkitEntity().setMetadata("name",new FixedMetadataValue(plugin, this.getCustomName().getString()));
    }

    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0,new FloatGoal(this));
        this.goalSelector.addGoal(1,new MeleeAttackGoal(this,1.0D,true));
        this.goalSelector.addGoal(2,new MoveTowardsRestrictionGoal(this,0.2D));
        this.goalSelector.addGoal(3,new MoveThroughVillageGoal(this,0.2D,false,1,null));
        this.goalSelector.addGoal(4,new RandomStrollGoal(this,0.2D));
        this.goalSelector.addGoal(5,new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6,new LookAtPlayerGoal(this, Player.class,8.0F));

        this.targetSelector.addGoal(1,new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Animal>(this,Animal.class,true));
        this.targetSelector.addGoal(3, new AvoidEntityGoal<Spider>(this, Spider.class,20,1.0D,1.0D));
    }
}
