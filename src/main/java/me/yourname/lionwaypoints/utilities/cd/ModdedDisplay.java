package me.yourname.lionwaypoints.utilities.cd;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayAttachment;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayManager;
import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.utilities.waypoint;
import org.bukkit.entity.Player;

public class ModdedDisplay extends ClientDisplay {

    public ModdedDisplay(waypoint waypoint, Player p, int offset, DisplayAttachment attachment) {
        super(waypoint, p);
        DisplayManager.sendCompassData(p, Settings.getInstance().getXzDistance(), Settings.getInstance().getyDistance());
        this.attachment = attachment;
        this.offset = offset;
    }

    int offset;
    DisplayAttachment attachment;

    @Override
    protected void onEnable() {
        try {
            DisplayManager.sendDisplayCompass(getPlayer(), "lionwaypoints_compass", offset, offset, attachment, (int) getWaypoint().getLocation().x(), (int) getWaypoint().getLocation().y(), (int) getWaypoint().getLocation().z(), getWaypoint().getLocation().getWorld().getName());
        }catch (Exception e){
            DisplayManager.sendDisplayCompass(getPlayer(), "lionwaypoints_compass", offset, offset, attachment, (int) getWaypoint().getLocation().x(), (int) getWaypoint().getLocation().y(), (int) getWaypoint().getLocation().z());
        }
    }

    @Override
    protected void onDisable() {
        DisplayManager.sendDisplayReset(getPlayer(), "lionwaypoints_compass");
    }
}
