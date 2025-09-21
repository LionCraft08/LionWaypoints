package me.yourname.lionwaypoints.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
    private static Settings instance;

    public Settings(FileConfiguration config) {
        allowVanillaTracker = config.getBoolean("display-settings.allow-vanilla-tracker", true);
        allowModdedTracker = config.getBoolean("display-settings.allow-modded-tracker", true);
        yDistance = config.getDouble("display-settings.y-distance-buffer", 4.0);
        xzDistance = config.getDouble("display-settings.xz-distance-buffer", 10.0);
    }

    public void save(FileConfiguration config){
         config.set("display-settings.allow-vanilla-tracker", allowVanillaTracker);
         config.set("display-settings.allow-modded-tracker", allowModdedTracker);
         config.set("display-settings.y-distance-buffer", yDistance);
         config.set("display-settings.xz-distance-buffer", xzDistance);
    }

    public static void setInstance(FileConfiguration config){
        instance = new Settings(config);
    }

    private boolean allowVanillaTracker;
    private boolean allowModdedTracker;
    private double yDistance;
    private double xzDistance;

    public boolean isAllowVanillaTracker() {
        return allowVanillaTracker;
    }

    public void setAllowVanillaTracker(boolean allowVanillaTracker) {
        this.allowVanillaTracker = allowVanillaTracker;
    }

    public boolean isAllowModdedTracker() {
        return allowModdedTracker;
    }

    public void setAllowModdedTracker(boolean allowModdedTracker) {
        this.allowModdedTracker = allowModdedTracker;
    }

    public double getyDistance() {
        return yDistance;
    }

    public void setyDistance(double yDistance) {
        this.yDistance = yDistance;
    }

    public double getXzDistance() {
        return xzDistance;
    }

    public void setXzDistance(double xzDistance) {
        this.xzDistance = xzDistance;
    }

    public static Settings getInstance() {
        return instance;
    }
}
