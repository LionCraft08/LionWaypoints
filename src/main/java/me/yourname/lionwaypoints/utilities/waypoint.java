package me.yourname.lionwaypoints.utilities;

import me.yourname.lionwaypoints.LionWaypoints;
import me.yourname.lionwaypoints.data.namespacedKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class waypoint {
    public static HashMap<String, waypoint> nameWaypoint;
    Location location;
    Material logo;
    String name;
    ItemStack finalItem;

    public waypoint(double x, double y, double z, World world, String name) {
        this.location = new Location(world, x, y, z);
        this.name = name;
        nameWaypoint.put(name, this);
        finalItem = createFinalItem();
    }

    public waypoint(Location location, String name) {
        this.location = location;
        this.name = name;
        nameWaypoint.put(name, this);
        finalItem = createFinalItem();
    }

    public waypoint(double x, double y, double z, World world, Material logo, String name) {
        this.location = new Location(world, x, y, z);
        this.logo = logo;
        this.name = name;
        nameWaypoint.put(name, this);
        finalItem = createFinalItem();
    }

    public waypoint(Location location, Material logo, String name) {
        this.location = location;
        this.logo = logo;
        this.name = name;
        nameWaypoint.put(name, this);
        finalItem = createFinalItem();
    }

    public String getPersistentData(){
        String s =  name + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName() + ":";
        s = s + Objects.requireNonNullElse(logo, Material.COMPASS);
        return s;
    }
    public Material getLogo(){
        if(logo == null){
            return Material.COMPASS;
        }
        return logo;
    }
    public void setLogo(Material logo){
        this.logo = logo;
        finalItem = createFinalItem();
    }
    public static void saveWaypoints(){
        List<String> persistentData = new ArrayList<>();
        for(waypoint wp : nameWaypoint.values()){//plugin getter!!!
            persistentData.add(wp.getPersistentData());
        }
        LionWaypoints.getPlugin().getConfig().set("waypoints.data", persistentData);
        LionWaypoints.getPlugin().saveConfig();
    }
    public static void recreateWaypoints(){
        List<String> persistentData = LionWaypoints.getPlugin().getConfig().getStringList("waypoints.data");
        for(String s : persistentData){
            try {
                int eins, zwei, drei, vier, fuenf;
                eins = s.indexOf(":");
                zwei = s.indexOf(":", eins + 1);
                drei = s.indexOf(":", zwei + 1);
                vier = s.indexOf(":", drei + 1);
                fuenf = s.indexOf(":", vier + 1);
                double x = Double.parseDouble(s.substring(eins + 1, zwei));
                double y = Double.parseDouble(s.substring(zwei + 1, drei));
                double z = Double.parseDouble(s.substring(drei + 1, vier));
                String name = s.substring(0, eins);
                World w = Bukkit.getWorld(s.substring(vier + 1, fuenf));
                Material m = Material.getMaterial(s.substring(fuenf + 1));
                if (m == Material.COMPASS || m == null) {
                    waypoint wp = new waypoint(x, y, z, w, name);
                } else {
                    waypoint wp = new waypoint(x, y, z, w, m, name);
                }
            }catch (NumberFormatException ignored){}
        }

    }
    public ItemStack createFinalItem(){
        ItemStack is = new ItemStack(getLogo());
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(name, TextColor.color(0, 255, 255)));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("X: " + ((Double) location.getX()).intValue(), TextColor.color(255, 255, 0)));
        lore.add(Component.text("Y: " + ((Double) location.getY()).intValue(), TextColor.color(255, 255, 0)));
        lore.add(Component.text("Z: " + ((Double) location.getZ()).intValue(), TextColor.color(255, 255, 0)));
        lore.add(Component.text("in " + location.getWorld().getName(), TextColor.color(255, 255, 0)));
        lore.add(Component.text("Click to open a Menu", TextColor.color(255, 255, 0)));
        im.getPersistentDataContainer().set(namespacedKeys.ItemType, PersistentDataType.STRING, "waypoint");
        im.getPersistentDataContainer().set(namespacedKeys.WaypointName, PersistentDataType.STRING, getName());
        im.lore(lore);
        is.setItemMeta(im);
        if(getLogo().equals(Material.COMPASS)){
            CompassMeta cm = (CompassMeta) is.getItemMeta();
            cm.setLodestoneTracked(false);
            cm.setLodestone(getLocation());
            is.setItemMeta(cm);
        }
        finalItem = is;
        return is;
    }
    public void sendMessage(CommandSender player){
        player.sendMessage(Component.text("|--" + getName() + "--", TextColor.color(255, 0, 255)));
        player.sendMessage(Component.text("|- X:" + (int) location.getX(), TextColor.color(255, 0, 255)));
        player.sendMessage(Component.text("|- Y:" + (int) location.getY(), TextColor.color(255, 0, 255)));
        player.sendMessage(Component.text("|- Z:" + (int) location.getZ(), TextColor.color(255, 0, 255)));
        player.sendMessage(Component.text("|- in " + location.getWorld().getName(), TextColor.color(255, 0, 255)));
        if(player instanceof Player){
            if(((Player) player).getWorld().equals(location.getWorld())){
                player.sendMessage(Component.text("|- " + (int) location.distance(((Player) player).getLocation()) + " Meters away", TextColor.color(255, 0, 255)));
            }
        }

    }
    public String getXaeroMessage(){
        int i = new Random().nextInt(15);
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        String world;
        if(location.getWorld().getEnvironment().equals(World.Environment.NETHER)){
            world = "Internal-the-nether-waypoints";
        }else if(location.getWorld().getEnvironment().equals(World.Environment.THE_END)){
            world = "Internal-the-end-waypoints";
        } else if (location.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            world = "Internal-overworld-waypoints";
        }else{
            world = "Internal-" + location.getWorld().getName().replace("_", "-") + "-waypoints";
        }
        return "<LionSystems> xaero-waypoint:"+name+":"+name.charAt(0)+":"+x+":"+y+":"+z+":"+i+":false:0:"+world;
    }

    public ItemStack getFinalItem() {
        return finalItem;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
    public static @Nullable waypoint getWaypoint(String name){
        if (name == null) return null;
        if(nameWaypoint.containsKey(name)){
            return nameWaypoint.get(name);
        }
        if(nameWaypoint.containsKey(name.replace("_", " "))){
            return nameWaypoint.get(name.replace("_", " "));
        }
        if(nameWaypoint.containsKey(name.replace(" ", "_"))){
            return nameWaypoint.get(name.replace(" ", "_"));
        }
        return null;
    }
    public void changeDimension(World newWorld){
        if(!getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)&&newWorld.getEnvironment().equals(World.Environment.NETHER)){
            getLocation().setX(getLocation().getX()/8);
            getLocation().setZ(getLocation().getZ()/8);
        }else if(getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)&&!newWorld.getEnvironment().equals(World.Environment.NETHER)){
            getLocation().setX(getLocation().getX()*8);
            getLocation().setZ(getLocation().getZ()*8);
        }
        getLocation().setWorld(newWorld);
    }
}
