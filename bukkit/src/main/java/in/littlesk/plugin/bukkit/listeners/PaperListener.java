package in.littlesk.plugin.bukkit.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import in.littlesk.plugin.core.Core;
import in.littlesk.plugin.core.yggdrasil.RemoteGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.concurrent.TimeUnit;

public class PaperListener implements Listener {
    private final Core core;

    public PaperListener(Core core) {
        this.core = core;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();

        core.getGameProfileByName(name)
            .orTimeout(1000, TimeUnit.MILLISECONDS)
            .thenAccept(optProfile -> {
                if (optProfile.isEmpty()) {
                    return;
                }

                RemoteGameProfile remoteProfile = optProfile.get();
                RemoteGameProfile.Property textureProperty = remoteProfile.getProperty("textures").orElse(null);
                if (textureProperty == null) {
                    return;
                }

                Bukkit.getScheduler().runTask(null, () -> {
                    PlayerProfile localProfile = event.getPlayerProfile();
                    localProfile.setProperty(new ProfileProperty(
                            "textures",
                            textureProperty.value,
                            textureProperty.signature
                    ));
                    event.setPlayerProfile(localProfile);
                });
            });
    }
}
