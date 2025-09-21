package me.yourname.lionwaypoints.listeners;

import de.lioncraft.lionutils.events.PluginDataResetEvent;
import me.yourname.lionwaypoints.utilities.waypoint;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class resetListener implements Listener {
    public void onReset(PluginDataResetEvent e){
        waypoint.nameWaypoint.clear();
    }
}
