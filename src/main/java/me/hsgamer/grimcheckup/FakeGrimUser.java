package me.hsgamer.grimcheckup;

import ac.grim.grimac.AbstractCheck;
import ac.grim.grimac.GrimUser;

import java.util.Collection;
import java.util.UUID;

public class FakeGrimUser implements GrimUser {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getBrand() {
        return null;
    }

    @Override
    public int getTransactionPing() {
        return 0;
    }

    @Override
    public int getKeepAlivePing() {
        return 0;
    }

    @Override
    public String getVersionName() {
        return null;
    }

    @Override
    public double getHorizontalSensitivity() {
        return 0;
    }

    @Override
    public double getVerticalSensitivity() {
        return 0;
    }

    @Override
    public boolean isVanillaMath() {
        return false;
    }

    @Override
    public void updatePermissions() {

    }

    @Override
    public Collection<? extends AbstractCheck> getChecks() {
        return null;
    }

    @Override
    public void runSafely(Runnable runnable) {

    }
}
