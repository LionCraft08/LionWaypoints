package me.yourname.lionwaypoints.utilities;

import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayAttachment;
import me.yourname.lionwaypoints.data.ButtonCreators;
import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.data.namespacedKeys;
import me.yourname.lionwaypoints.data.texts;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class inventories {
    public static Inventory getWaypointOverview(Player owner, int page){
        Inventory inv = Bukkit.createInventory(owner, 54, texts.OverviewInvTitle);
        inv.setContents(ButtonCreators.BlockButtons);
        inv.setItem(49, ButtonCreators.CloseButton);

        if (Bukkit.getPluginManager().isPluginEnabled("lionAPI")){
            inv.setItem(45, MainMenu.getToMainMenuButton());
        }

        int i = 0, slot = 9;
        for(waypoint wp : waypoint.nameWaypoint.values()){
            if(i < page * 36 + 36){
                inv.setItem(slot, wp.getFinalItem());
                slot++;
            }
            if (i == page * 36 + 36) {
                inv.setItem(53, ButtonCreators.NextButton);
            }
            i++;
        }

        return inv;
    }
    public static Inventory getWaypointSettingInv(Player owner, waypoint wp){
        Inventory inv = Bukkit.createInventory(owner, 54, texts.SettingsInvTitle);
        inv.setContents(ButtonCreators.BlockButtons);
        inv.setItem(49, ButtonCreators.CloseButton);
        inv.setItem(4, wp.getFinalItem());
        inv.setItem(1, ButtonCreators.getCompass(wp.getLocation()));
        inv.setItem(7, ButtonCreators.getCompass(wp.getLocation()));
        if(owner.hasPermission("lionwaypoints.create")){
            inv.setItem(18, ButtonCreators.createTemplate(Material.IRON_SWORD));
            inv.setItem(19, ButtonCreators.createTemplate(Material.CHEST));
            inv.setItem(20, ButtonCreators.createTemplate(Material.NETHERRACK));
            inv.setItem(21, ButtonCreators.createTemplate(Material.DIAMOND_PICKAXE));
            inv.setItem(22, ButtonCreators.createTemplate(Material.WATER_BUCKET));
            inv.setItem(23, ButtonCreators.createTemplate(Material.OAK_LOG));
            inv.setItem(24, ButtonCreators.createTemplate(Material.SPAWNER));
            inv.setItem(25, ButtonCreators.createTemplate(Material.END_PORTAL_FRAME));
            inv.setItem(26, ButtonCreators.createTemplate(Material.DIAMOND));
            inv.setItem(36, ButtonCreators.getWorldButton(wp.getLocation().getWorld()));
            if(owner.isOp()){
                inv.setItem(44, ButtonCreators.teleportButton);
            }
            inv.setItem(38, ButtonCreators.deleteButton);
        }
        inv.setItem(42, ButtonCreators.xaeroButton);
        inv.setItem(40, ButtonCreators.messageButton);
        inv.setItem(45, ButtonCreators.BackButton);
        return inv;
    }
    public static void openEnableTrackerInv(Player player, waypoint waypoint){
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Track Waypoint"));
        inv.setContents(ButtonCreators.BlockButtons);
        inv.setItem(45, ButtonCreators.back("waypoint:"+waypoint.name));
        inv.setItem(49, ButtonCreators.CloseButton);
        inv.setItem(4, waypoint.getFinalItem());
        inv.setItem(10, ButtonCreators.get("send_message",
                waypoint.getName(),
                Component.text("Send Message"),
                Material.GOAT_HORN,
                "<white>Sends a Message in Chat containing ","<white>the Coordinates"));
        if (Settings.getInstance().isAllowVanillaTracker()){
            inv.setItem(12, ButtonCreators.get("open_bossbar",
                    waypoint.getName(),
                    Component.text("Open Vanilla compass"),
                    Material.COMPASS,
                    "<white>Displays a Compass using the Vanilla ","<white>Minecraft Bossbar"));
        }

        if (Settings.getInstance().isAllowModdedTracker()){
            if (Bukkit.getPluginManager().isPluginEnabled("lionAPI")){
                inv.setItem(28, displaySlots.get(0));
                inv.setItem(14, ButtonCreators.get("open_modded",
                        waypoint.getName(),
                        Component.text("Open LionCompass"),
                        Material.RECOVERY_COMPASS,
                        "<white>Opens a Compass provided by the ","<white>LionDisplays Mod",
                        "<red>(Requires LionDisplays to be installed", "<red>on the Client)"));
                inv.setItem(34, getNumberedItem(5));
            }
        }

        inv.setItem(16, ButtonCreators.get("open_resource_pack",
                waypoint.getName(),
                Component.text("Open RP Compass"),
                Material.PAINTING,
                "<white>Opens a Compass only displayable ","<white>with a Resource Pack",
                "<red>(Requires a custom Resource Pack)",
                "<#FF6600>Currently not available"));

        player.openInventory(inv);


    }

    public static ItemStack getNumberedItem(int value){
        ItemStack is = getItem(Component.text("Offset: "+value),
                Material.AMETHYST_SHARD,
                "display_offset",
                ""+value,
                "Click to increase the offset"," (distance to the window frame)");
        return is;
    }

    public static void setNextItem(Inventory inv){
        inv.setItem(28, displaySlots.get(getNextIndex(inv.getItem(28))));
    }
    private static int getNextIndex(ItemStack is){
        int i = displaySlots.indexOf(is);
        if (i >= displaySlots.size()){
            return 0;
        }
        return i+1;
    }

    private static List<ItemStack> displaySlots;

    public static void init(){
        displaySlots = new ArrayList<>();
        for (DisplayAttachment a : DisplayAttachment.values()) {
            displaySlots.add(getItem(
                    Component.text(a.toString().replace("_", " ")),
                    Material.SPYGLASS,
                    "display_slot",
                    a.toString(),
                    "Click to change the Slot where ", "the compass gets displayed",
                    "(Only required for Displaytype: Mod)"));
        }
    }

    private static ItemStack getItem(Component name, Material material, String id,String data, String... lore){
        ItemStack is = ButtonCreators.get(name, material, lore);
        is.editMeta(itemMeta -> {
            itemMeta.getPersistentDataContainer().set(namespacedKeys.DisplayData, PersistentDataType.STRING, data);
            itemMeta.getPersistentDataContainer().set(namespacedKeys.CustomButton, PersistentDataType.STRING, id);
        });
        return is;
    }

}
