package me.yourname.lionwaypoints.utilities.cd;

import me.yourname.lionwaypoints.chat.MessageHandler;
import me.yourname.lionwaypoints.utilities.waypoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public abstract class ClientDisplay {

    public ClientDisplay(waypoint waypoint, Player p) {
        this.waypoint = waypoint;
        enabled = false;
        this.p = p;
    }

    private final waypoint waypoint;
    private boolean enabled;
    private final Player p;

    public waypoint getWaypoint() {
        return waypoint;
    }

    public Player getPlayer() {
        return p;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled){
            if (this.enabled) MessageHandler.sendMessage(Component.text("The display is already active!", NamedTextColor.DARK_RED), getPlayer());
            else {
                onEnable();
                MessageHandler.sendMessage(Component.text("Now displaying: "+waypoint.getName(), NamedTextColor.DARK_AQUA), getPlayer());
            }
        }else{
            if (!this.enabled) MessageHandler.sendMessage(Component.text("The display is already disabled!", NamedTextColor.DARK_RED), getPlayer());
            else {
                onDisable();
                MessageHandler.sendMessage(Component.text("Paused displaying "+waypoint.getName(), NamedTextColor.DARK_PURPLE), getPlayer());
            }
        }
        this.enabled = enabled;
    }

    protected abstract void onEnable();
    protected abstract void onDisable();

    public enum ClientDisplayType{
        MOD,
        RESOURCE_PACK,
        VANILLA
    }
}
