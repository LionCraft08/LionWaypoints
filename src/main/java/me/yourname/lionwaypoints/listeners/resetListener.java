package me.yourname.lionwaypoints.listeners;

import de.lioncraft.lionutils.events.PluginDataResetEvent;
import me.yourname.lionwaypoints.utilities.waypoint;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class resetListener implements Listener {
    @EventHandler
    public void onReset(PluginDataResetEvent e){
        waypoint.nameWaypoint.clear();
    }
}
