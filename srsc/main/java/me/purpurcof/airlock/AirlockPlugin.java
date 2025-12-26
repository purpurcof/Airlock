package me.purpurcof.airlock;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.purpurcof.airlock.listener.NetworkSessionListener;
import me.purpurcof.airlock.service.IPFilterService;
import org.bukkit.plugin.java.JavaPlugin;

public final class AirlockPlugin extends JavaPlugin {

    private IPFilterService filterService;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();

        saveDefaultConfig();

        this.filterService = new IPFilterService(this);
        this.filterService.reload();

        NetworkSessionListener sessionListener = new NetworkSessionListener(this.filterService);

        PacketEvents.getAPI().getEventManager()
                .registerListener(sessionListener, PacketListenerPriority.LOWEST);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    public IPFilterService getFilterService() {
        return filterService;
    }
}
