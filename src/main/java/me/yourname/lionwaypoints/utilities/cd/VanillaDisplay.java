package me.yourname.lionwaypoints.utilities.cd;

import io.papermc.paper.math.Position;
import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.utilities.waypoint;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class VanillaDisplay extends ClientDisplay {
    private BossBar bb;
    public VanillaDisplay(waypoint waypoint, Player p) {
        super(waypoint, p);
        bb = BossBar.bossBar(Component.text("Waiting"), 1.0f, BossBar.Color.YELLOW, BossBar.Overlay.NOTCHED_10);
    }

    private double startDistance;
    private boolean reached = false;

    @Override
    protected void onEnable() {
        if ( startDistance == 0){
            startDistance = getDistance();
        }
        getPlayer().showBossBar(bb);
        update();
    }

    @Override
    protected void onDisable() {
        getPlayer().hideBossBar(bb);
    }

    private double getDistance(){
        Player p = getPlayer();
        Location target = getWaypoint().getLocation();
        if (p.getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)
                &&getWaypoint().getLocation().getWorld().getEnvironment().equals(World.Environment.NORMAL)) {

            target = new Location(p.getWorld(), getWaypoint().getLocation().x()/8,
                    getWaypoint().getLocation().y(),
                    getWaypoint().getLocation().z()/8);


        } else if (p.getLocation().getWorld().getEnvironment().equals(World.Environment.NORMAL)
                &&getWaypoint().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)) {

            target = new Location(p.getWorld(), getWaypoint().getLocation().x()*8,
                    getWaypoint().getLocation().y(),
                    getWaypoint().getLocation().z()*8);
        }else if(!(p.getLocation().getWorld() == target.getWorld())) {
            return 0;
        }
        return Math.abs(getPlayer().getLocation().distance(target));
    }

    public void update(){
        Player p = getPlayer();

        Component message = Component.text("");
        Location target = getWaypoint().getLocation();


        if (p.getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)
            &&getWaypoint().getLocation().getWorld().getEnvironment().equals(World.Environment.NORMAL)) {

            target = new Location(p.getWorld(), getWaypoint().getLocation().x()/8,
                    getWaypoint().getLocation().y(),
                    getWaypoint().getLocation().z()/8);
            message = Component.text(" (Overworld)", TextColor.color(200, 128, 0));


        } else if (p.getLocation().getWorld().getEnvironment().equals(World.Environment.NORMAL)
                &&getWaypoint().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)) {

            target = new Location(p.getWorld(), getWaypoint().getLocation().x()*8,
                    getWaypoint().getLocation().y(),
                    getWaypoint().getLocation().z()*8);
            message = Component.text(" (Nether)", TextColor.color(200, 128, 0));


        }else if(!(p.getLocation().getWorld() == target.getWorld())) {
            message = Component.text("Waypoint is in another dimension ("+getDimensionName(target.getWorld().getName())+")", TextColor.color(200, 80, 80));
            bb.name(message);
            return;
        }
        double distance = p.getLocation().distance(target);
        double distanceY = getWaypoint().getLocation().getY()-getPlayer().getLocation().getY();
        String arrow = getDirectionArrow(p, target);
        String upArrow = getHeightArrow(distanceY);
        TextColor defColor = TextColor.color(255, 255, 255);

        float progress = (float) (distance/startDistance);
        if (distance > startDistance) {
            defColor = TextColor.color(200, 0, 0);
            progress = 1.0f;
        }
        else if (distance < Settings.getInstance().getXzDistance() &&Math.abs(distanceY)<Settings.getInstance().getyDistance()) {
            defColor = TextColor.color(TextColor.color(0, 150, 0));
            if (!reached){
                reached = true;
            }
        }

        bb.progress(Math.abs(progress));

        bb.name(Component.text(getWaypoint().getName(), defColor)
                .append(message)
                .append(Component.text(": "+Math.round(distance)+"m", defColor))
                .appendSpace()
                .append(Component.text("    "+arrow, TextColor.color(255, 255, 255), TextDecoration.BOLD))
                .append(Component.text(upArrow, TextColor.color(150, 150, 150), TextDecoration.BOLD)));
    }

    private String getDimensionName(String s){
        if (s.equalsIgnoreCase("world")){
            return "overworld";
        }
        if (s.equalsIgnoreCase("world_nether")){
            return "nether";
        }
        return s;
    }

    private String getHeightArrow(double distance){
        if (Math.abs(distance) < Settings.getInstance().getyDistance()) {
            return "   ";
        }
        if (distance > 0){
            return " |  ↑";
        }
        return " |  ↓";

    }
    // Array of arrows representing 8 directions.
    // The order is crucial: N, NE, E, SE, S, SW, W, NW.
    // We start with North (↑) at index 0 and go clockwise.
    private static final String[] ARROWS = {"↑", "↗", "→", "↘", "↓", "↙", "←", "↖"};

    /**
     * Calculates the direction a player needs to face to look at a target location
     * and returns a corresponding arrow character.
     *
     * @param player The player whose perspective is used.
     * @param target The target location to point towards.
     * @return A string containing a Unicode arrow (e.g., "↑", "↗", "→").
     */
    private static String getDirectionArrow(Player player, Location target) {
        // Ensure the locations are in the same world to prevent calculation errors.
        if (!player.getWorld().equals(target.getWorld())) {
            return "?"; // Or handle this error as you see fit.
        }

        // --- 1. Calculate Vector ---
        // Get the vector pointing from the player to the target location.
        // We only care about the horizontal plane (x, z), so we can ignore the y-axis.
        Vector direction = target.toVector().subtract(player.getLocation().toVector());

        // --- 2. Get Player's Direction (Yaw) ---
        // Player's yaw is their rotation on the horizontal plane.
        // We normalize it to be within the 0-360 degree range.
        double delta = getDelta(player, direction);

        // --- 5. Map Angle to Arrow ---
        // We add half of a slice (22.5 degrees) to the angle to align our 8 slices
        // correctly. For example, the "forward" arrow (↑) should be centered at 0/360 degrees
        // and cover the range from 337.5 to 22.5 degrees.
        // Dividing by 45 (which is 360 / 8) gives us the index for our arrow array.
        int index = (int) Math.round(delta / 45.0);

        // Use modulo 8 to handle the wrap-around case (e.g., index 8 becomes 0).
        if (index%8<0){
            return ARROWS[(index % 8) + 8];
        }
        return ARROWS[index % 8];
    }

    private static double getDelta(Player player, Vector direction) {
        float playerYaw = player.getLocation().getYaw();
        if (playerYaw < 0) {
            playerYaw += 360;
        }

        // --- 3. Calculate Angle of Target Vector ---
        // Math.atan2 gives us the angle in radians from the positive Z-axis.
        // We convert it to degrees and adjust it to match Minecraft's yaw system,
        // where 0 degrees is South.
        double targetAngle = Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));

        // --- 4. Calculate Relative Angle ---
        // This is the angle the player needs to turn to face the target.
        double delta = targetAngle - playerYaw;

        // Normalize the delta to be between 0 and 360 degrees.
        delta = (delta + 360) % 360;
        return delta;
    }

}
