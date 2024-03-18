package me.hsgamer.grimcheckup;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.events.FlagEvent;
import ac.grim.grimac.manager.CheckManager;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.google.common.collect.ClassToInstanceMap;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.simpleplugin.SimplePlugin;
import me.hsgamer.hscore.config.PathString;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GrimCheckup extends SimplePlugin implements Listener {
    private final BukkitConfig config = new BukkitConfig(this);
    private final Set<String> disabledChecks = new HashSet<>();

    @Override
    public void enable() {
        config.setup();

        GrimPlayer grimPlayer = fakeGrimPlayer();
        CheckManager checkManager = new CheckManager(grimPlayer);
        ClassToInstanceMap<AbstractCheck> checks = checkManager.allChecks;

        checks.forEach((clazz, check) -> {
            String configName = check.getConfigName();
            PathString pathString = new PathString(configName);
            config.setIfAbsent(pathString, true);

            Object value = config.getNormalized(pathString);
            if (value instanceof Boolean && !(Boolean) value) {
                disabledChecks.add(configName);
            }
        });

        config.save();

        registerListener(this);
    }

    @EventHandler
    public void onFlag(FlagEvent event) {
        AbstractCheck check = event.getCheck();
        if (disabledChecks.contains(check.getConfigName())) {
            event.setCancelled(true);
            check.setEnabled(false);
        }
    }

    private GrimPlayer fakeGrimPlayer() {
        User user = new User(null, null, null, new UserProfile(UUID.randomUUID(), "Fake"));
        return new GrimPlayer(user);
    }
}
