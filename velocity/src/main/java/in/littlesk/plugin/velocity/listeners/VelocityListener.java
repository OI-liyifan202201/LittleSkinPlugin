package in.littlesk.plugin.velocity.listeners;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.GameProfileRequestEvent;
import com.velocitypowered.api.util.GameProfile;
import in.littlesk.plugin.core.Core;
import in.littlesk.plugin.core.yggdrasil.RemoteGameProfile;

import java.util.ArrayList;
import java.util.List;

public class VelocityListener {

    private final Core core;

    public VelocityListener(Core core) {
        this.core = core;
    }

    @Subscribe(order = PostOrder.LAST)
    public EventTask onGameProfileRequest(GameProfileRequestEvent event) {
        String name = event.getUsername();

        return EventTask.async(() -> {
            RemoteGameProfile remoteProfile = core.getGameProfileByName(name).orElse(null);
            if (remoteProfile == null) {
                return;
            }

            RemoteGameProfile.Property textureProperty = remoteProfile.getProperty("textures").orElse(null);
            if (textureProperty == null) {
                return;
            }

            GameProfile localProfile = event.getGameProfile();
            List<GameProfile.Property> newProperties = new ArrayList<GameProfile.Property>(localProfile.getProperties());
            newProperties.removeIf(p -> p.getName().equals("textures"));
            newProperties.add(new GameProfile.Property(
                    "textures",
                    textureProperty.value,
                    textureProperty.signature
            ));

            event.setGameProfile(localProfile.withProperties(newProperties));
        });
    }
}
