package in.littlesk.plugin.bukkit;

import in.littlesk.plugin.bukkit.listeners.PaperListener;
import in.littlesk.plugin.core.Core;
import in.littlesk.plugin.core.logger.JulLogger;
import in.littlesk.plugin.core.yggdrasil.Messages;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitMain extends JavaPlugin {

    private Server server;
    private JulLogger logger;
    private Core core;

    @Override
    public void onEnable() {
        this.server = this.getServer();
        this.logger = new JulLogger(this.getLogger());

        this.logger.warning(Messages.MOTD);

        PluginManager pm = this.getServer().getPluginManager();

        if (Bukkit.getOnlineMode()) {
            this.logger.warning(Messages.ONLINE_MODE_MESSAGE);
            pm.disablePlugin(this);
            return;
        }

        Metrics metrics = new Metrics(this, 29005);

        if (checkIsPaper()) {
            this.core = new Core(this.logger, this.server.getName(), this.server.getVersion());
            pm.registerEvents(new PaperListener(this.core), this);
        } else {
            this.logger.error(Messages.INCOMPATIBLE_SERVER_MESSAGE_BUKKIT);
            this.logger.warning(Messages.INCOMPATIBLE_SERVER_STAY_TUNED_MESSAGE);
            this.getServer().shutdown();
        }
    }

    @Override
    public void onDisable() {
        this.logger.warning(Messages.DISABLE_MESSAGE);
        if (this.core != null) {
            this.core.shutdown();
        }
    }

    private static boolean checkIsPaper() {
        try {
            Class.forName("com.destroystokyo.paper.profile.PlayerProfile");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
