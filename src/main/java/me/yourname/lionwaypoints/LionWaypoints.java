package me.yourname.lionwaypoints;

import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionutils.utils.ResetUtils;
import me.yourname.lionwaypoints.commands.posCommand;
import me.yourname.lionwaypoints.commands.wpCommand;

import me.yourname.lionwaypoints.data.Settings;
import me.yourname.lionwaypoints.data.texts;
import me.yourname.lionwaypoints.listeners.*;
import me.yourname.lionwaypoints.data.ButtonCreators;
import me.yourname.lionwaypoints.data.namespacedKeys;
import me.yourname.lionwaypoints.utilities.ClientUIManager;
import me.yourname.lionwaypoints.utilities.cd.VanillaDisplayUtils;
import me.yourname.lionwaypoints.utilities.inventories;
import me.yourname.lionwaypoints.utilities.waypoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class LionWaypoints extends JavaPlugin{

    @Override
    public void onEnable() {
        plugin = this;
        worlds = new ArrayList<>();
        waypoint.nameWaypoint = new HashMap<>();

        saveDefaultConfig();

        texts.setTexts();
        namespacedKeys.setNamespacedKeys();
        ButtonCreators.setButtons();

        Settings.setInstance(getConfig());

        getCommand("wp").setExecutor(new wpCommand());
        getCommand("pos").setExecutor(new posCommand());

        getServer().getPluginManager().registerEvents(new ChatMessageListeners(), this);
        getServer().getPluginManager().registerEvents(new worldListeners(), this);
        getServer().getPluginManager().registerEvents(new invClickListener(), this);
        getServer().getPluginManager().registerEvents(ClientUIManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new VanillaDisplayUtils(), this);

        if (getServer().getPluginManager().isPluginEnabled("LionUtils")){
            ResetUtils.addFileToReset(getDataFolder());
            getServer().getPluginManager().registerEvents(new resetListener(), this);
        }

        if (getServer().getPluginManager().isPluginEnabled("lionAPI")){
            getServer().getPluginManager().registerEvents(new ButtonListeners(), this);
            inventories.init();
            MainMenu.setButton(16, LionButtonFactory.createButton(Items.get("Waypoints", Material.RECOVERY_COMPASS, "Opens the Waypoint menu"),
                    "lionwaypoints_open_menu"));

            LionChat.registerChannel("waypoints",
                    new ChannelConfiguration(false,
                            TextColor.color(0, 150, 255),
                            Component.text("WP", TextColor.color(0, 255, 255)),
                            true));
        }

        BukkitTask t = new delayedStartup().runTaskLater(plugin, 2);
    }

    public static Plugin getPlugin() {
        return plugin;
    }
    public static List<World> worlds;

    static Plugin plugin;

    @Override
    public void onDisable() {
        waypoint.saveWaypoints();
    }
}
