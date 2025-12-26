package me.purpurcof.airlock.service;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Service responsible for maintaining the whitelist state.
 * Uses synchronized set for thread-safe O(1) lookups.
 */
public class IPFilterService {

    private final JavaPlugin plugin;
    private final Set<String> allowedAddresses;

    public IPFilterService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.allowedAddresses = Collections.synchronizedSet(new HashSet<>());
    }

    public void reload() {
        allowedAddresses.clear();
        plugin.reloadConfig();

        List<String> configIps = plugin.getConfig().getStringList("allowed-ips");

        if (configIps.isEmpty()) {
            plugin.getLogger().log(Level.SEVERE, "Whitelist is empty! Server will reject ALL connections (including localhost).");
        } else {
            allowedAddresses.addAll(configIps);
            plugin.getLogger().info("IP Filter updated. " + allowedAddresses.size() + " authorized addresses loaded.");
        }
    }

    /**
     * Checks if the provided IP address is authorized.
     * @param ipAddress The raw IPv4 string from the Netty channel.
     * @return true if connection should be accepted.
     */
    public boolean isAuthorized(String ipAddress) {
        return allowedAddresses.contains(ipAddress);
    }
}
