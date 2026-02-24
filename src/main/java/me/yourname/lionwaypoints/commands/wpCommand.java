package me.yourname.lionwaypoints.commands;

import me.yourname.lionwaypoints.LionWaypoints;
import me.yourname.lionwaypoints.chat.MessageHandler;
import me.yourname.lionwaypoints.utilities.ClientUIManager;
import me.yourname.lionwaypoints.utilities.inventories;
import me.yourname.lionwaypoints.utilities.waypoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class wpCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length){
            case 0:
                if(sender instanceof Player){
                    ((Player) sender).openInventory(inventories.getWaypointOverview((Player) sender, 0));
                }else{
                    MessageHandler.sendMessage(Component.text("Only Players can open this Inventory!", TextColor.color(255, 90, 0)), sender);
                }
                break;
            case 1:
                switch (args[0]){
                    case "add":
                        MessageHandler.sendMessage("Specify a name for the Waypoint", sender);
                    break;
                    case "get":
                        for (waypoint wp : waypoint.nameWaypoint.values()) {
                            wp.sendMessage(sender);
                        }
                        break;
                    case "xaero":
                        for(waypoint wp : waypoint.nameWaypoint.values()){
                            sender.sendMessage(wp.getXaeroMessage());
                        }
                        break;
                    default:
                        MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|display <waypoint>", TextColor.color(255, 100, 0)), sender);
                        break;
                }
                break;
            case 2:
                waypoint wp = waypoint.getWaypoint(args[1]);
                if(wp == null && !(args[0].equals("add")||args[0].equals("display"))){
                    MessageHandler.sendMessage("This Waypoint does not exist!", sender);
                    break;
                }
                switch (args[0]){
                    case "get":
                        wp.sendMessage(sender);
                        break;
                    case "xaero":
                        sender.sendMessage(wp.getXaeroMessage());
                        break;
                    case "remove", "delete":
                        for (String s : waypoint.nameWaypoint.keySet()){
                            if(waypoint.nameWaypoint.get(s)==wp){
                                waypoint.nameWaypoint.remove(s);
                                MessageHandler.sendMessage(Component.text("Successfully removed Waypoint " + wp.getName(), TextColor.color(255, 0, 255)), sender);
                                break;
                            }
                        }
                        break;
                    case "add":
                        if(sender instanceof Player){
                            if(waypoint.nameWaypoint.containsKey(args[1])){
                                MessageHandler.sendMessage(Component.text("This Waypoint already exists!", TextColor.color(255, 90, 0)), sender);
                            }else {
                                waypoint newWP = new waypoint(((Player) sender).getLocation(), args[1]);
                                MessageHandler.sendMessage(Component.text("Successfully created Waypoint " + newWP.getName(), TextColor.color(0, 255, 255)), sender);
                            }
                        }else{
                            MessageHandler.sendMessage(Component.text("Only Players can create Waypoints at their current location!", TextColor.color(255, 90, 0)), sender);
                            MessageHandler.sendMessage(Component.text("Please use /wp create <name> <x> <y> <z> [world] instead", TextColor.color(255, 90, 0)), sender);
                        }
                        break;
                    case "compass":
                        if(sender.hasPermission("lionwaypoints.compass")){
                            if(sender instanceof Player p){
                                ItemStack is = p.getInventory().getItem(EquipmentSlot.HAND);
                                if (is.getType().equals(Material.COMPASS)) {
                                    CompassMeta cm = (CompassMeta) is.getItemMeta();
                                    cm.setLodestoneTracked(false);
                                    cm.setLodestone(wp.getLocation());
                                    is.setItemMeta(cm);
                                    p.sendMessage(Component.text("This Compass now points to " + wp.getName(), TextColor.color(255, 0, 255)));
                                }else{
                                    MessageHandler.sendMessage(Component.text("Put a Compass in Your Main-Hand", TextColor.color(255, 90, 0)), sender);
                                }
                            }else{
                                MessageHandler.sendMessage(Component.text("Only players can execute this Command", TextColor.color(255, 90, 0)), sender);
                            }
                        }else{
                            MessageHandler.sendMessage(Component.text("You do not have the Permission to execute this Command! Thought about using Lodestones?", TextColor.color(255, 90, 0)), sender);
                        }
                        break;
                    case "display":
                        if (sender instanceof Player p){
                            switch (args[1]){
                                case "toggle_visibility" -> {
                                    if (ClientUIManager.i().getCurrentDisplay(p) != null){
                                        ClientUIManager.i().toggleVisibility(p);
                                    }else MessageHandler.sendMessage("No Display was found!", p);

                                }
                                case "disable" -> ClientUIManager.i().endCurrentDisplay(p);
                                default -> MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>"), sender);
                            }
                        }else MessageHandler.sendMessage(Component.text("Only players can execute this Command", TextColor.color(255, 90, 0)), sender);
                        break;
                    default:
                        MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>"), sender);
                        break;
                }
                break;
            case 3:
                if(args[0].equals("add")){
                    createWaypoint(sender, args[1], args[2] , "0", "0", "");
                }else if(args[0].equals("display")){
                    if(args[1].equals("disable")){
                        waypoint wayp = waypoint.getWaypoint(args[2]);
                        if(wayp == null){
                            MessageHandler.sendMessage("This Waypoint does not exist!", sender);
                            break;
                        }
                        if (sender instanceof Player p){
                            ClientUIManager.i().displayData(p, wayp);
                        }else MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>"), sender);
                    } else MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>"), sender);
                } else{
                    MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>", TextColor.color(255, 90, 0)), sender);
                }
                break;
            case 4:
                if(args[0].equals("add")){
                    createWaypoint(sender, args[1], args[2] , args[3], "0", "");
                }else{
                    MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>", TextColor.color(255, 90, 0)), sender);
                }
                break;
            case 5:
                if(args[0].equals("add")){
                    createWaypoint(sender, args[1], args[2] , args[3], args[4], "");
                }else{
                    MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>", TextColor.color(255, 90, 0)), sender);
                }
                break;
            default:
                if(args[0].equals("add")){
                    createWaypoint(sender, args[1], args[2] , args[3], args[4], args[5]);
                }else{
                    MessageHandler.sendMessage(Component.text("Usage: /wp remove|delete|compass|get|xaero|add <waypoint>", TextColor.color(255, 90, 0)), sender);
                }
        }
        return true;
    }
    public static void createWaypoint(CommandSender sender, String name, String x, String y, String z, @NotNull String worldName){
        int X, Y, Z;
        World world = Bukkit.getWorld(worldName);
        if(worldName.isEmpty()){
            if(sender instanceof Player && world == null){
                world = ((Player) sender).getWorld();
            }
            if(world==null){
                for(World w : LionWaypoints.worlds){
                    if(w.getEnvironment().equals(World.Environment.NORMAL)){
                        world = w;
                    }
                }
                if(world == null){
                    world = LionWaypoints.worlds.get(0);
                }
            }
        }else {
            if (world == null) {
                for (World w : LionWaypoints.worlds) {
                    if (w.getName().equalsIgnoreCase(worldName)) {
                        world = w;
                        break;
                    } else if (w.getEnvironment().toString().equalsIgnoreCase(worldName)) {
                        world = w;
                        break;
                    } else if (worldName.toLowerCase().contains(w.getName().toLowerCase())) {
                        world = w;
                        break;
                    } else if (worldName.toLowerCase().contains(w.getEnvironment().toString().toLowerCase())) {
                        world = w;
                        break;
                    }
                }
            }
        }
        try {
            X = Integer.parseInt(x);
        }catch (NumberFormatException e){
            MessageHandler.sendMessage(x + " is not a Number!", sender);
            X = 0;
        }
        try {
            Y = Integer.parseInt(y);
        }catch (NumberFormatException e){
            MessageHandler.sendMessage(y + " is not a Number!", sender);
            Y = 0;
        }
        try {
            Z = Integer.parseInt(z);
        }catch (NumberFormatException e){
            MessageHandler.sendMessage(z + " is not a Number!", sender);
            Z = 0;
        }
        if(waypoint.nameWaypoint.containsKey(name)){
            MessageHandler.sendMessage(Component.text("This Waypoint already exists!", TextColor.color(255, 90, 0)), sender);
        }else{
            waypoint wp = new waypoint(X, Y, Z, world, name);
            MessageHandler.sendMessage(Component.text("Successfully created Waypoint " + wp.getName(), TextColor.color(0, 255, 255)), sender);
        }


    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        List<String> Waypoints = new ArrayList<>();
        if (args.length == 1) {
            Waypoints.add("get");
            Waypoints.add("compass");
            Waypoints.add("remove");
            Waypoints.add("xaero");
            Waypoints.add("add");
            Waypoints.add("display");
        } else if (args.length == 2) {
            if (args[0].equals("display")){
                Waypoints.add("set");
                Waypoints.add("toggle_visibility");
                Waypoints.add("disable");
            }
            else if (!args[0].equals("add")) {
                for (String name : waypoint.nameWaypoint.keySet()) {
                    if (!args[1].isEmpty()) {
                        if (name.startsWith(args[1])) {
                            Waypoints.add(name);
                        }
                    } else {
                        Waypoints.add(name);
                    }
                }
            }
        }
        if(args.length > 1){
            if(args[0].equals("add")){
                switch (args.length){
                    case 2:
                        Waypoints.add("<name>");
                        Waypoints.add("Spawn");
                        Waypoints.add("Base");
                        Waypoints.add("Village");
                        break;
                    case 3:
                        Waypoints.add("<x>");
                        if(sender instanceof Player){
                            Waypoints.add(String.valueOf((int) ((Player) sender).getX()));
                        }
                        break;
                    case 4:
                        Waypoints.add("<y>");
                        if(sender instanceof Player){
                            Waypoints.add(String.valueOf((int) ((Player) sender).getY()));
                        }
                        break;
                    case 5:
                        Waypoints.add("<z>");
                        if(sender instanceof Player){
                            Waypoints.add(String.valueOf((int) ((Player) sender).getZ()));
                        }
                        break;
                    case 6:
                        Waypoints.add("<world>");
                        for (World w : LionWaypoints.worlds){
                            Waypoints.add(w.getName());
                        }
                        break;
                }
            }else if (args[0].equals("display")&&args.length==3){
                for (String name : waypoint.nameWaypoint.keySet()) {
                    if (!args[2].isEmpty()) {
                        if (name.startsWith(args[2])) {
                            Waypoints.add(name);
                        }
                    } else {
                        Waypoints.add(name);
                    }
                }
            }
        }
        return Waypoints;
    }
}
