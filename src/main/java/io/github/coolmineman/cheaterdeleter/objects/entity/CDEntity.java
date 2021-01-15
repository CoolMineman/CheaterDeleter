package io.github.coolmineman.cheaterdeleter.objects.entity;

import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.trackers.Tracker;
import io.github.coolmineman.cheaterdeleter.trackers.data.Data;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
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

    default <T extends Data> T getTracked(Tracker<T> tracker) {
        return tracker.get(this);
    }

    /**
     * Gets the rigid collision box that acts like a block (Shulkers and Boats)
     */
    @Nullable
    default Box getRigidCollision() {
        return asMcEntity().isCollidable() ? asMcEntity().getBoundingBox() : null;
    }

    /**
     * Gets Box For Current Position, Not Cached Use A Local Variable For Calls
     */
    default Box getBox() {
        return getBoxForPosition(this.getX(), this.getY(), this.getZ());
    }

    default Box getBoxForPosition(double posx, double posy, double posz) {
        return asMcEntity().getDimensions(asMcEntity().getPose()).method_30231(posx, posy, posz); // method_30231 -> withPos
    }

    default void _init() {
    }

    @Nullable
    default CDEntity getVehicleCd() {
        return CDEntity.of(asMcEntity().getVehicle());
    }

    default float getStepHeight() {
        return asMcEntity().stepHeight;
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
