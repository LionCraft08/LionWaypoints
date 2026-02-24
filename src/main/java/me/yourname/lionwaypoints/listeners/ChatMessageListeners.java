package me.yourname.lionwaypoints.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.yourname.lionwaypoints.LionWaypoints;
import me.yourname.lionwaypoints.chat.MessageHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatMessageListeners implements Listener {


    @EventHandler
    public void onChatMessage(AsyncChatEvent event){
        Player p = event.getPlayer();
        if(!p.hasPermission("lionwaypoints.create")){
            return;
        }
        TextComponent ct = (TextComponent) event.message();
        String message = ct.content();
        if (message.contains("xaero-waypoint:")) {
            int first = message.indexOf(":");
            int second = message.indexOf(":", first + 1);
            int third = message.indexOf(":", second + 1);
            int fourth = message.indexOf(":", third + 1);
            int six = message.indexOf(":", fourth + 1);
            int seven = message.indexOf(":", six + 1);

            String name = message.substring(first + 1, second).replace(' ', '_');
            String sx = message.substring(third + 1, fourth);
            String sy = message.substring(fourth + 1, six);
            String sz = message.substring(six + 1, seven);
            String world = message.substring(message.lastIndexOf(':')+1);
            world = world.replace("Internal-", "");
            world = world.replace("-waypoints", "");
            boolean b = true;
            for(World w : LionWaypoints.worlds){
                if(w.getName().equalsIgnoreCase(world)){
                    world = w.getName().toLowerCase();
                    b = false;
                    break;
                } else if (w.getEnvironment().toString().equalsIgnoreCase(world)) {
                    world = w.getName();
                    b = false;
                    break;
                } else if (world.toLowerCase().contains(w.getName().toLowerCase())) {
                    world = w.getName();
                    break;
                } else if (world.toLowerCase().contains(w.getEnvironment().toString().toLowerCase())) {
                    world = w.getName();
                    break;
                }
            }
            if(b || Bukkit.getWorld(world)==null){
                world = "world";
            }
            Component answer = Component.text("You shared a Waypoint called \"" + name + "\" in the Chat! Click to add it to the Server-Waypoints");
            answer = answer.hoverEvent(HoverEvent.showText(Component.text("Waypoint " + name + " at X:" +sx+" Y:"+sy+" Z:"+sz)));
            answer = answer.clickEvent(ClickEvent.runCommand("/wp add " + name + " " + sx + " " + sy +" " + sz+" "+world));
            MessageHandler.sendMessage(answer, p);
        }

    }
}

