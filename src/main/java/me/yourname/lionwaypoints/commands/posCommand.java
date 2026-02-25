package me.yourname.lionwaypoints.commands;

import me.yourname.lionwaypoints.chat.MessageHandler;
import me.yourname.lionwaypoints.utilities.waypoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class posCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        if(args.length == 0){
            if(sender instanceof Player p){
                p.performCommand("wp");
            }else{
                MessageHandler.sendMessage(Component.text("Only Players can open this Inventory!", TextColor.color(255, 90, 0)), sender);
            }
        } else if (args.length == 1) {
            waypoint wp = waypoint.getWaypoint(args[0]);
            if(wp != null){
                wp.sendMessage(sender);
            }else{
                if(sender instanceof Player p){
                    waypoint newWP = new waypoint(p.getLocation(), args[0]);
                    MessageHandler.sendMessage("Successfully created Waypoint " + newWP.getName(), sender);
                }else{
                    MessageHandler.sendMessage(Component.text("Only Players can create Waypoints at their current location!",
                            TextColor.color(255, 90, 0)), sender);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            for(String s : waypoint.nameWaypoint.keySet()){
                list.add(s.replace(" ", "_"));
            }
        }
        return list;
    }
}
