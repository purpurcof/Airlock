package me.purpurcof.airlock.listener;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import me.purpurcof.airlock.service.IPFilterService;

public class NetworkSessionListener implements PacketListener {

    private final IPFilterService filterService;

    public NetworkSessionListener(IPFilterService filterService) {
        this.filterService = filterService;
    }

    /**
     * Triggered during the TCP connection initialization (Netty ChannelActive).
     * Occurs BEFORE packet decompression and BEFORE any Player object creation.
     */
    @Override
    public void onUserConnect(UserConnectEvent event) {
        // Extract IP from the User object (wrapping the Netty Channel)
        String remoteAddress = event.getUser().getAddress().getHostString();

        // Check against the whitelist service
        if (!filterService.isAuthorized(remoteAddress)) {
            // Hard close the connection immediately.
            // This prevents the server from reading the incoming byte stream.
            event.getUser().closeConnection();

            // Cancel event to stop propagation to other plugins
            event.setCancelled(true);
        }
    }
}
