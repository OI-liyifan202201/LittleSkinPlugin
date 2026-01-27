package in.littlesk.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import in.littlesk.plugin.core.Core;
import in.littlesk.plugin.core.logger.Slf4jLogger;
import in.littlesk.plugin.core.Messages;
import in.littlesk.plugin.velocity.listeners.VelocityListener;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

@Plugin(
        id = "littleskinplugin",
        name = "LittleSkinPlugin",
        version = "1.0.0"
)
public class VelocityMain {

    private final ProxyServer server;
    private final Slf4jLogger logger;
    private final Metrics.Factory metricsFactory;
    private Core core;

    @Inject
    public VelocityMain(ProxyServer server, Logger logger, Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = new Slf4jLogger(logger);
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.logger.warning(Messages.MOTD);

        if (this.server.getConfiguration().isOnlineMode()) {
            this.logger.warning(Messages.ONLINE_MODE_MESSAGE);
            return;
        }

        String serverImpl = this.server.getVersion().getName();
        String serverVer = this.server.getVersion().getVersion();
        if (!serverVer.startsWith("3.")) {
            this.logger.error(Messages.INCOMPATIBLE_SERVER_MESSAGE_VELOCITY);
            this.server.shutdown();
        }

        Metrics metrics = this.metricsFactory.make(this, 29008);
        this.core = new Core(this.logger, serverImpl, serverVer);
        server.getEventManager().register(this, new VelocityListener(this.core));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.core != null) {
            this.core.shutdown();
        }
    }
}
