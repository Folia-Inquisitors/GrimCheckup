package me.hsgamer.grimcheckup;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimAPIProvider;
import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.api.event.events.GrimJoinEvent;
import ac.grim.grimac.api.plugin.BasicGrimPlugin;
import ac.grim.grimac.api.plugin.GrimPlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.simpleplugin.SimplePlugin;
import me.hsgamer.hscore.config.PathString;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public final class GrimCheckup extends SimplePlugin implements Listener {
    private final BukkitConfig config = new BukkitConfig(this);
    private final Set<String> disabledChecks = new HashSet<>();

    @Override
    public void enable() {
        config.setup();

        config.getNormalizedValues(false).forEach((key, value) -> {
            String check = PathString.toPath(key);
            if (value instanceof Boolean && !(Boolean) value) {
                disabledChecks.add(check);
            }
        });

        GrimAbstractAPI api = GrimAPIProvider.get();
        GrimPlugin plugin = new BasicGrimPlugin(
                this.getLogger(),
                this.getDataFolder(),
                this.getDescription().getVersion(),
                this.getDescription().getDescription(),
                this.getDescription().getAuthors()
        );

        EventBus eventBus = api.getEventBus();

        eventBus.subscribe(plugin, FlagEvent.class, event -> {
            String check = event.getCheck().getConfigName();
            if (disabledChecks.contains(check)) {
                event.setCancelled(true);
            }
        });

        eventBus.subscribe(plugin, GrimJoinEvent.class, event -> {
            GrimUser grimUser = event.getUser();

            boolean configChanged = false;
            for (AbstractCheck abstractCheck : grimUser.getChecks()) {
                PathString path = new PathString(abstractCheck.getConfigName());
                if (!config.contains(path)) {
                    config.set(path, true);
                    configChanged = true;
                }
            }

            if (configChanged) {
                config.save();
            }
        });
    }
}
