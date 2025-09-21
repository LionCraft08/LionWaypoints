package me.yourname.lionwaypoints.listeners;

import de.lioncraft.lionapi.events.invs.LionButtonClickEvent;
import me.yourname.lionwaypoints.utilities.inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ButtonListeners implements Listener {
    @EventHandler
    public void onClick(LionButtonClickEvent e){
        switch (e.getID()){
            case "lionwaypoints_open_menu":
                e.getPlayer().openInventory(inventories.getWaypointOverview(null, 0));
                break;
        }
    }
}
