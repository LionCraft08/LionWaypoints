package me.yourname.lionwaypoints.data;

import me.yourname.lionwaypoints.LionWaypoints;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ButtonCreators {
    public static ItemStack CloseButton;
    public static ItemStack[] BlockButtons;
    public static ItemStack BackButton;
    public static ItemStack NextButton;
    public static ItemStack teleportButton;
    public static ItemStack deleteButton;
    public static ItemStack xaeroButton;
    public static ItemStack messageButton;
    public static void setButtons(){
        ButtonCreators b1 = new ButtonCreators();
        CloseButton = b1.CloseButton();
        BlockButtons = b1.block(54);
        BackButton = back("overview");
        NextButton = b1.next();
        teleportButton = b1.tp();
        deleteButton = get(Component.text("DELETE", TextColor.color(255, 0, 0)), Material.RED_WOOL, "This waypoint will be gone forever (a very long time)");
        xaeroButton = get(Component.text("Xaeros Maps"), Material.KNOWLEDGE_BOOK, "Click to add this Waypoint to", "your World Map (requires Xaeros Minimap)");
        messageButton = get(Component.text("Track this Waypoint"), Material.GOAT_HORN, "Opens a Menu to track the" , "Waypoint in your GUI");
    }
    public static ItemStack getWorldButton(World world){
        World nextWorld;
        try {
            nextWorld = LionWaypoints.worlds.get(LionWaypoints.worlds.indexOf(world) + 1);
        }catch (IndexOutOfBoundsException e){
            nextWorld = LionWaypoints.worlds.get(0);
        }
        ItemStack is = get(Component.text(world.getName()), Material.GRASS_BLOCK, "This Waypoint is in the Dimension " + world.getName() , "Click to set the dimension to " + nextWorld.getName());
        if(world.getEnvironment().equals(World.Environment.NETHER)){
            is.setType(Material.NETHERRACK);
        } else if (world.getEnvironment().equals(World.Environment.THE_END)) {
            is.setType(Material.END_STONE);
        }
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(namespacedKeys.ItemType, PersistentDataType.STRING, "worldbutton");
        im.getPersistentDataContainer().set(namespacedKeys.WorldName, PersistentDataType.STRING, nextWorld.getName());
        is.setItemMeta(im);
        return is;
    }

    public ItemStack CloseButton(){
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closemeta = close.getItemMeta();
        closemeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLOSE");
        List<String> closelore = new ArrayList<>();
        closelore.add("Click to close");
        closelore.add("the current GUI");
        closemeta.setLore(closelore);
        closemeta.setUnbreakable(true);
        close.setItemMeta(closemeta);
        return close;
    }
    public static ItemStack get(Component title, Material material, String... lores){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        ButtonMeta.displayName(title);
        List<Component> lore = new ArrayList<>();
        for(String s : lores){
            lore.add(Component.text(s));
        }
        ButtonMeta.addItemFlags();
        ButtonMeta.lore(lore);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
    public ItemStack tp(){
        ItemStack close = new ItemStack(Material.ENDER_PEARL);
        ItemMeta closemeta = close.getItemMeta();
        closemeta.displayName(Component.text("Teleport to Waypoint"));
        closemeta.setUnbreakable(true);
        close.setItemMeta(closemeta);
        return close;
    }
    public static ItemStack getCompass(Location location){
        ItemStack is = new ItemStack(Material.COMPASS);
        CompassMeta cm = (CompassMeta) is.getItemMeta();
        cm.setLodestoneTracked(false);
        cm.setLodestone(location);
        is.setItemMeta(cm);
        return is;
    }
    public static ItemStack createTemplate(Material material){
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(namespacedKeys.ItemType, PersistentDataType.STRING, "template");
        im.lore(new ArrayList<>(Collections.singleton(Component.text("Click to set " + material + " as Logo", TextColor.color(255, 128, 0)))));
        is.setItemMeta(im);
        return is;
    }
    public ItemStack[] block(int size){
        ItemStack[] blocked = new ItemStack[size];
        ItemStack placeholder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta plmeta = placeholder.getItemMeta();
        plmeta.displayName(Component.text(""));
        plmeta.setUnbreakable(true);
        plmeta.getPersistentDataContainer().set(namespacedKeys.CustomButton, PersistentDataType.STRING, "empty");
        placeholder.setItemMeta(plmeta);
        Arrays.fill(blocked, placeholder);
        return blocked;
    }
    public ItemStack ButtonON(String name){
        ItemStack button = new ItemStack(Material.LIME_DYE);
        ItemMeta bmeta = button.getItemMeta();
        bmeta.displayName(Component.text(name));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Currently enabled!", NamedTextColor.GREEN));
        lore.add(Component.text("Click here to disable.", NamedTextColor.GREEN));
        bmeta.lore(lore);
        button.setItemMeta(bmeta);
        return button;
    }

    public static ItemStack back(String inv){
        ItemStack b = new ItemStack(Material.ARROW);
        ItemMeta bm = b.getItemMeta();
        bm.displayName(Component.text("BACK", NamedTextColor.GOLD, TextDecoration.BOLD));
        bm.getPersistentDataContainer().set(namespacedKeys.BackButton, PersistentDataType.STRING,inv);
        b.setItemMeta(bm);
        return b;
    }

    public static ItemStack get(String id, String waypointName,Component title, Material material, String... lores){
        ItemStack b = new ItemStack(material);
        ItemMeta bm = b.getItemMeta();
        bm.displayName(title);
        bm.getPersistentDataContainer().set(namespacedKeys.CustomButton, PersistentDataType.STRING,id);
        bm.getPersistentDataContainer().set(namespacedKeys.WaypointName, PersistentDataType.STRING,waypointName);
        List<Component> list = new ArrayList<>();
        for (String s : lores){
            list.add(MiniMessage.miniMessage().deserialize(s));
        }
        bm.lore(list);
        b.setItemMeta(bm);
        return b;
    }
    public ItemStack next(){
        ItemStack b = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta bm = b.getItemMeta();
        bm.displayName(Component.text("NEXT", TextColor.color(255, 128, 0)));
        b.setItemMeta(bm);
        return b;
    }
}
