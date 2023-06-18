package main.java.custommob.mobs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.java.custommob.CustomMob;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.InflaterInputStream;

public class Vampire implements Listener {
    CustomMob plugin;
    static double health;
    static double armor;
    static double armorToug;

    public Vampire (CustomMob plugin){
        this.plugin = plugin;
    }

    public static void create(Location location){
    }
    public static String[] getSkin(Player player, String name){
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid
                    + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[] {texture,signature};
        } catch (Exception e) {
            Bukkit.broadcastMessage("Error");
        }
        return null;
    }
}
