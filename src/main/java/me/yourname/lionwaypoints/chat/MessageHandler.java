package me.yourname.lionwaypoints.chat;

import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

public final class MessageHandler {
    private MessageHandler(){}
    public static void sendMessage(Component message, Audience a){
        if (Bukkit.getPluginManager().isPluginEnabled("lionAPI")){
            LionChat.sendMessageOnChannel("waypoints", message, a);
        }else a.sendMessage(Component.text("WP >> ", TextColor.color(0, 150, 255)).append(message));
    }
    public static void sendMessage(String message, Audience a){
        sendMessage(Component.text(message), a);
    }
}
