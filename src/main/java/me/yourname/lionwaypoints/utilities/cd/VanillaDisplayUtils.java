package me.yourname.lionwaypoints.utilities.cd;

import me.yourname.lionwaypoints.LionWaypoints;
import me.yourname.lionwaypoints.utilities.ClientUIManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class VanillaDisplayUtils implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ClientUIManager.getInstance().hasActiveDisplay(e.getPlayer())){
                    ClientDisplay cd = ClientUIManager.i().getCurrentDisplay(e.getPlayer());
                    if (cd instanceof VanillaDisplay vd){
                        vd.update();
                    }
                }
            }
        }.runTaskAsynchronously(LionWaypoints.getPlugin());

    }
    @EventHandler
    public void onMove(PlayerChangedWorldEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ClientUIManager.getInstance().hasActiveDisplay(e.getPlayer())){
                    ClientDisplay cd = ClientUIManager.i().getCurrentDisplay(e.getPlayer());
                    if (cd instanceof VanillaDisplay vd){
                        vd.update();
                    }
                }
            }
        }.runTaskAsynchronously(LionWaypoints.getPlugin());

    }
}
