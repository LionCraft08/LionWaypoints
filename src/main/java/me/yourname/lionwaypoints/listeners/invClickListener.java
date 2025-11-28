package me.yourname.lionwaypoints.listeners;

import de.lioncraft.lionapi.guimanagement.guielements.GUIPlayerManager;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayAttachment;
import me.yourname.lionwaypoints.LionWaypoints;
import me.yourname.lionwaypoints.chat.MessageHandler;
import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.data.texts;
import me.yourname.lionwaypoints.data.ButtonCreators;
import me.yourname.lionwaypoints.utilities.ClientUIManager;
import me.yourname.lionwaypoints.utilities.cd.ModdedDisplay;
import me.yourname.lionwaypoints.utilities.cd.VanillaDisplay;
import me.yourname.lionwaypoints.utilities.inventories;
import me.yourname.lionwaypoints.data.namespacedKeys;
import me.yourname.lionwaypoints.utilities.waypoint;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class invClickListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null) {
            return;
        }
        if(e.getCurrentItem() != null){
            if(e.getCurrentItem().getItemMeta() != null) {
                if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(namespacedKeys.ItemType)) {
                    e.setCancelled(true);
                    if (Objects.equals(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.ItemType, PersistentDataType.STRING), "waypoint")) {
                        e.getWhoClicked().openInventory(inventories.getWaypointSettingInv((Player) e.getWhoClicked(), waypoint.nameWaypoint.get(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING))));
                    } else if (Objects.equals(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.ItemType, PersistentDataType.STRING), "template")) {
                        waypoint wp = waypoint.nameWaypoint.get(Objects.requireNonNull(e.getClickedInventory().getItem(4)).getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING));
                        wp.setLogo(e.getCurrentItem().getType());
                        e.getWhoClicked().openInventory(inventories.getWaypointSettingInv((Player) e.getWhoClicked(), wp));
                    } else if (Objects.equals(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.ItemType, PersistentDataType.STRING), "worldbutton")) {
                        World w = Bukkit.getWorld(Objects.requireNonNull(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.WorldName, PersistentDataType.STRING)));
                        waypoint wp = waypoint.nameWaypoint.get(Objects.requireNonNull(e.getClickedInventory().getItem(4)).getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING));
                        wp.changeDimension(w);
                        wp.createFinalItem();
                        e.getWhoClicked().openInventory(inventories.getWaypointSettingInv((Player) e.getWhoClicked(), wp));
                    }
                } else if (e.getView().title().equals(texts.SettingsInvTitle)) {
                    e.setCancelled(true);
                    waypoint wp = waypoint.nameWaypoint.get(e.getView().getTopInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING));
                    Player p = (Player) e.getWhoClicked();
                    if (e.getClickedInventory().equals(e.getView().getTopInventory())) {
                        if (e.getCurrentItem().equals(ButtonCreators.teleportButton)) {
                            e.getWhoClicked().teleport(wp.getLocation());
                            e.getWhoClicked().closeInventory();
                            p.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        } else if (e.getCurrentItem().equals(ButtonCreators.deleteButton)) {
                            waypoint.nameWaypoint.remove(e.getView().getTopInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING));
                            e.getWhoClicked().openInventory(inventories.getWaypointOverview(p, 0));
                            p.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
                        } else if (e.getCurrentItem().equals(ButtonCreators.xaeroButton)) {
                            ((Player) e.getWhoClicked()).performCommand("wp xaero " + wp.getName().replaceAll(" ", "_"));
                        } else if (e.getCurrentItem().equals(ButtonCreators.messageButton)) {
                            inventories.openEnableTrackerInv((Player) e.getWhoClicked(), wp);
                        }
                    } else {
                        wp.setLogo(e.getCurrentItem().getType());
                        e.getWhoClicked().openInventory(inventories.getWaypointSettingInv((Player) e.getWhoClicked(), wp));
                    }
                } else if (e.getView().title().equals(texts.OverviewInvTitle)) {
                    e.setCancelled(true);
                }
            }
        }
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getItemMeta() == null) return;
            if(e.getCurrentItem().equals(ButtonCreators.CloseButton)){
                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }

            if (e.getCurrentItem().equals(ButtonCreators.messageButton)){
                inventories.openEnableTrackerInv((Player) e.getWhoClicked(), waypoint.getWaypoint(e.getClickedInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING)));
                e.setCancelled(true);
            }

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(namespacedKeys.BackButton)) {
                String value = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.BackButton, PersistentDataType.STRING);
                String s = Objects.requireNonNull(value);
                if (s.equals("overview")) {
                    e.getWhoClicked().openInventory(inventories.getWaypointOverview(null, 0));
                } else if (s.startsWith("waypoint")) {
                    e.getWhoClicked().openInventory(inventories.getWaypointSettingInv((Player) e.getWhoClicked(), waypoint.getWaypoint(s.substring(s.indexOf(":")+1))));

                }
                e.setCancelled(true);
            }
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(namespacedKeys.CustomButton)) {
                String value = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.CustomButton, PersistentDataType.STRING);
                String s = Objects.requireNonNull(value);
                waypoint wp = waypoint.getWaypoint(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKeys.WaypointName, PersistentDataType.STRING));
                switch (s){
                    case "send_message" ->{
                        wp.sendMessage(e.getWhoClicked());
                        e.getWhoClicked().closeInventory();
                    }
                    case "open_bossbar"-> {
                        ClientUIManager.i().displayData(new VanillaDisplay(wp, (Player) e.getWhoClicked()));
                        e.getWhoClicked().closeInventory();
                    }
                    case "open_modded" -> {
                        String slot = getData(e.getClickedInventory().getItem(28));
                        int offset = Integer.parseInt(getData(e.getClickedInventory().getItem(34)));
                        Player p = (Player) e.getWhoClicked();
                        if(GUIPlayerManager.getRenderWay(p).equals(GUIPlayerManager.ClientRenderWay.LIONDISPLAYS_MOD))
                            ClientUIManager.i().displayData(new ModdedDisplay(wp, p, offset, DisplayAttachment.valueOf(slot)));
                        else MessageHandler.sendMessage("You need the Mod LionDisplays to do this!", p);
                        p.closeInventory();
                    }
                    case "display_slot" -> inventories.setNextItem(e.getClickedInventory());
                    case "display_offset" -> {
                        changeNumber(e.getClickedInventory());
                    }
                }
                e.setCancelled(true);
            }

        }

    }
    private void changeNumber(Inventory inv){
        ItemStack is = inv.getItem(34);
        int value = Integer.valueOf(is.getItemMeta().getPersistentDataContainer().get(namespacedKeys.DisplayData, PersistentDataType.STRING));
        value+=5;
        if (value > 25) value = 0;
        inv.setItem(34, inventories.getNumberedItem(value));
    }
    private String getData(ItemStack is){
        return is.getItemMeta().getPersistentDataContainer().get(namespacedKeys.DisplayData, PersistentDataType.STRING);
    }
}
