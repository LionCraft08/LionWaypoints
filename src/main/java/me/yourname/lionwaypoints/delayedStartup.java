package me.yourname.lionwaypoints;

import me.yourname.lionwaypoints.utilities.waypoint;
import org.bukkit.scheduler.BukkitRunnable;

public class delayedStartup extends BukkitRunnable {
    @Override
    public void run() {
        waypoint.recreateWaypoints();
    }
}
