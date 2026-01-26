package in.littlesk.plugin.bukkit.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import in.littlesk.plugin.core.Core;
import in.littlesk.plugin.core.yggdrasil.RemoteGameProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PaperListener implements Listener {
    private final Core core;

    public PaperListener(Core core) {
        this.core = core;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();

        RemoteGameProfile remoteProfile = core.getGameProfileByName(name).orElse(null);
        if (remoteProfile == null) {
            return;
        }

        RemoteGameProfile.Property textureProperty = remoteProfile.getProperty("textures").orElse(null);
        if (textureProperty == null) {
            return;
        }

        PlayerProfile localProfile = event.getPlayerProfile();
        localProfile.setProperty(new ProfileProperty(
                "textures",
                textureProperty.value,
                textureProperty.signature
        ));

        event.setPlayerProfile(localProfile);
    }
}
