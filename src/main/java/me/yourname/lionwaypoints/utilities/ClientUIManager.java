package me.yourname.lionwaypoints.utilities;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.guielements.GUIPlayerManager;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayAttachment;
import me.yourname.lionwaypoints.chat.MessageHandler;
import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.utilities.cd.ClientDisplay;
import me.yourname.lionwaypoints.utilities.cd.ModdedDisplay;
import me.yourname.lionwaypoints.utilities.cd.VanillaDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ClientUIManager implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        data.remove(e.getPlayer());
    }

    private static final ClientUIManager instance = new ClientUIManager();
    private ClientUIManager(){
    }

    public static ClientUIManager getInstance() {
        return instance;
    }
    public static ClientUIManager i(){
        return getInstance();
    }
    public void displayData(ClientDisplay cd){
        if (cd instanceof VanillaDisplay){
            if (!Settings.getInstance().isAllowVanillaTracker()){
                MessageHandler.sendMessage("Vanilla displays are disabled", cd.getPlayer());
                return;
            }
        } else if (cd instanceof ModdedDisplay) {
            if (!Settings.getInstance().isAllowModdedTracker()){
                MessageHandler.sendMessage("Modded displays are disabled", cd.getPlayer());
                return;
            }
        }
        endCurrentDisplay(cd.getPlayer());
        data.put(cd.getPlayer(), cd);
        cd.setEnabled(true);
    }
    public void displayData(Player p, waypoint waypoint){
        if(Bukkit.getPluginManager().isPluginEnabled("lionAPI")){
            if(GUIPlayerManager.getRenderWay(p).equals(GUIPlayerManager.ClientRenderWay.LIONDISPLAYS_MOD)&&
            Settings.getInstance().isAllowModdedTracker()){
                displayData(p, ClientDisplay.ClientDisplayType.MOD, waypoint);
                return;
            }
        }
        displayData(p, ClientDisplay.ClientDisplayType.VANILLA, waypoint);
    }
    public void displayData(Player p, ClientDisplay.ClientDisplayType type, waypoint waypoint){
        switch (type){
            case MOD -> {
                if (Bukkit.getPluginManager().isPluginEnabled("lionAPI")) {
                    if (Settings.getInstance().isAllowModdedTracker()) {
                        displayData(new ModdedDisplay(waypoint, p, 5, DisplayAttachment.TOP_LEFT));
                    }
                }
            }
            case RESOURCE_PACK, VANILLA -> {
                if (Settings.getInstance().isAllowVanillaTracker()){
                    displayData(new VanillaDisplay(waypoint, p));
                }
            }
        }
    }
    public void endCurrentDisplay(Player p){
        if (data.containsKey(p)) data.get(p).setEnabled(false);
        data.remove(p);
    }
    public void toggleVisibility(Player p){
        ClientDisplay cd = getCurrentDisplay(p);
        if (cd != null){
            cd.setEnabled(!cd.isEnabled());
        }
    }

    public ClientDisplay getCurrentDisplay(Player p){
        return data.get(p);
    }

    public boolean hasActiveDisplay(Player p){
        if (!data.containsKey(p)) return false;
        if (data.get(p).isEnabled()) return true;
        return false;
    }
    private final HashMap<Player, ClientDisplay> data = new HashMap<>();

}
