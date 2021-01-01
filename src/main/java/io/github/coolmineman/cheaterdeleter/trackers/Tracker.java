package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.trackers.data.Data;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class Tracker<T extends Data> {
    private Class<T> clazz;

    protected Tracker(Class<T> clazz) {
        this.clazz = clazz;
    }

    public final Class<T> getClazz() {
        return clazz;
    }

    @NotNull
    abstract T get(ServerPlayerEntity player);
}
