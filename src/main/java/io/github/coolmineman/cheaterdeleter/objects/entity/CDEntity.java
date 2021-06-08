package io.github.coolmineman.cheaterdeleter.objects.entity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.trackers.Tracker;
import io.github.coolmineman.cheaterdeleter.trackers.data.Data;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public interface CDEntity {
    <T> void putData(Class<T> clazz, T data);

    @Nullable
    <T> T getData(Class<T> clazz);

    <T> T getOrCreateData(Class<T> clazz, Supplier<T> supplier);

    ConcurrentHashMap<Class<?>, Object> _getMap();

    void _setMap(ConcurrentHashMap<Class<?>, Object> map);

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
        return asMcEntity().getDimensions(asMcEntity().getPose()).getBoxAt(posx, posy, posz);
    }

    default double getMaxJumpHeight() {
        double result = getBaseMaxJumpHeight();
        if (asMcEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)asMcEntity();
            if (livingEntity.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                result += Math.pow(1.5, (livingEntity.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1)) - 1; //Acumulates Error With High Jump Boosts, oh well
            }
        }
        result += 0.2; // Give a bit of wiggle room
        return result;
    }

    default double getBaseMaxJumpHeight() {
        return 1.25;
    }

    default float getSpeed() {
        if (this instanceof LivingEntity) return ((LivingEntity)this).getMovementSpeed();
        if (this instanceof BoatEntity) return 0.2f; //Guestimation
        return 0;
    }

    default void _init() {
    }

    @Nullable
    default CDEntity getVehicleCd() {
        return CDEntity.of(asMcEntity().getVehicle());
    }

    default HitResult raycast(double x, double y, double z, float yaw, float pitch, double maxDistance, RaycastContext.FluidHandling fluidHandling) {
        return MathUtil.raycastInDirection(this, x, y, z, MathUtil.getRotationVector(MathHelper.wrapDegrees(pitch), MathHelper.wrapDegrees(yaw)), maxDistance, fluidHandling);
    }

    float _getEyeHeight(EntityPose pose, EntityDimensions dimensions);

    default float getEyeHeight(EntityPose pose) {
        return _getEyeHeight(pose, asMcEntity().getDimensions(pose));
    }

    default float getEyeHeight() {
        return getEyeHeight(asMcEntity().getPose());
    }

    /**
     * When last pushed by a piston in world ticks
     */
    long getPistonMovementTick();

    default float getStepHeight() {
        return asMcEntity().stepHeight;
    }

    @Deprecated
    default double getX() {
        return asMcEntity().getX();
    }

    @Deprecated
    default double getY() {
        return asMcEntity().getY();
    }

    @Deprecated
    default double getZ() {
        return asMcEntity().getZ();
    }

    @Deprecated
    default float getYaw() {
        return asMcEntity().getYaw();
    }

    @Deprecated
    default float getPitch() {
        return asMcEntity().getPitch();
    }

    default Vec3d getVelocity() {
        return asMcEntity().getVelocity();
    }

    default boolean isOnGround() {
        return asMcEntity().isOnGround();
    }

    default ServerWorld getWorld() {
        return (ServerWorld)asMcEntity().world;
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
