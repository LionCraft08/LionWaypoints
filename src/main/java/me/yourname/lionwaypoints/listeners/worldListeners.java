package me.yourname.lionwaypoints.listeners;

import me.yourname.lionwaypoints.LionWaypoints;
import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.utilities.waypoint;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class worldListeners implements Listener {
    @EventHandler
    public void onWorldSave(WorldSaveEvent e){
        waypoint.saveWaypoints();
        Settings.getInstance().save(LionWaypoints.getPlugin().getConfig());
        LionWaypoints.getPlugin().saveConfig();
    }
    @EventHandler
    public void onWorldLoad(WorldInitEvent e){
        Bukkit.getConsoleSender().sendMessage("<LionWaypoints> Detected World " + e.getWorld().getName() + " from World Type " + e.getWorld().getEnvironment());
        LionWaypoints.worlds.add(e.getWorld());
    }
}
