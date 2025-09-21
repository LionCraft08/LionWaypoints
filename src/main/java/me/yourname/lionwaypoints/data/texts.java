package me.yourname.lionwaypoints.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class texts {
    public static Component OverviewInvTitle;
    public static Component SettingsInvTitle;

    public static void setTexts(){
        OverviewInvTitle = Component.text("Waypoints", TextColor.color(255, 0, 255));
        SettingsInvTitle = Component.text("Waypoint Settings", TextColor.color(255, 0, 255));
    }
}
