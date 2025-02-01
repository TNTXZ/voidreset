package voidreset;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Voidreset extends JavaPlugin implements Listener {

    private int yThreshold;
    private Location customSpawnLocation;
    private boolean useCustomSpawn;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        FileConfiguration config = getConfig();
        yThreshold = config.getInt("y-threshold", -64);
        useCustomSpawn = config.getBoolean("use-custom-spawn", false); // 默认不启用自定义出生点

        if (useCustomSpawn) {
            double x = config.getDouble("custom-spawn.x", 0.0);
            double y = config.getDouble("custom-spawn.y", 0.0);
            double z = config.getDouble("custom-spawn.z", 0.0);
            float yaw = (float) config.getDouble("custom-spawn.yaw", 0.0);
            float pitch = (float) config.getDouble("custom-spawn.pitch", 0.0);
            String worldName = config.getString("custom-spawn.world", "world");

            customSpawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }

        getLogger().info("By TNTXZ");

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < yThreshold) {
            Location spawnLocation;
            if (useCustomSpawn) {
                spawnLocation = customSpawnLocation;
            } else {
                spawnLocation = player.getWorld().getSpawnLocation();
            }
            player.teleport(spawnLocation);
        }
    }
}