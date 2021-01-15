package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.trackers.data.Data;

public abstract class Tracker<T extends Data> {
    private Class<T> clazz;

    protected Tracker(Class<T> clazz) {
        this.clazz = clazz;
    }

    public final Class<T> getClazz() {
        return clazz;
    }

    @NotNull
    public abstract T get(CDEntity entity);
}
