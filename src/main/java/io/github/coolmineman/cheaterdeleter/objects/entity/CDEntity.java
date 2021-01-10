package io.github.coolmineman.cheaterdeleter.objects.entity;

import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface CDEntity {
    <T> void putData(Class<T> clazz, T data);

    @Nullable
    <T> T getData(Class<T> clazz);

    default <T> T getOrCreateData(Class<T> clazz, Supplier<T> supplier) {
        T result = getData(clazz);
        if (result == null) {
            result = supplier.get();
            putData(clazz, result);
        }
        return result;
    }

    default void _init() {
    }

    default double getX() {
        return asMcEntity().getX();
    }

    default double getY() {
        return asMcEntity().getY();
    }

    default double getZ() {
        return asMcEntity().getZ();
    }

    default float getYaw() {
        return asMcEntity().yaw;
    }

    default float getPitch() {
        return asMcEntity().pitch;
    }

    default Vec3d getVelocity() {
        return asMcEntity().getVelocity();
    }

    default boolean isOnGround() {
        return asMcEntity().isOnGround();
    }

    default World getWorld() {
        return asMcEntity().world;
    }

    default UUID getUuid() {
        return asMcEntity().getUuid();
    }

    default Entity asMcEntity() {
        return (Entity) this;
    }

    public static CDEntity of(Entity entity) {
        return (CDEntity)entity;
    }
}
