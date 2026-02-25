package me.yourname.lionwaypoints.data;

import me.yourname.lionwaypoints.LionWaypoints;
import org.bukkit.NamespacedKey;

public class namespacedKeys {
    public static NamespacedKey WaypointName;
    /**can contain:
     * - WorldButton
     * - template
     * - waypoint
     */
    public static NamespacedKey ItemType;
    public static NamespacedKey WorldName;
    public static NamespacedKey BackButton;
    public static NamespacedKey CustomButton;
    public static NamespacedKey DisplayData;
    public static void setNamespacedKeys(){
        WaypointName = new NamespacedKey(LionWaypoints.getPlugin(), "WaypointName");
        ItemType = new NamespacedKey(LionWaypoints.getPlugin(), "ItemType");
        WorldName = new NamespacedKey(LionWaypoints.getPlugin(), "WorldName");
        BackButton = new NamespacedKey(LionWaypoints.getPlugin(), "BackButton");
        CustomButton = new NamespacedKey(LionWaypoints.getPlugin(), "custom_button");
        DisplayData = new NamespacedKey(LionWaypoints.getPlugin(), "display_data");
    }
}
