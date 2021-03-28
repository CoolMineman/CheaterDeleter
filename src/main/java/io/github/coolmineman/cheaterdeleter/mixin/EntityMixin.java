package io.github.coolmineman.cheaterdeleter.mixin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.cheaterdeleter.events.PlayerStartRidingListener;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;

@Mixin(Entity.class)
public class EntityMixin implements CDEntity {
    @Shadow
    private long pistonMovementTick;

    @Shadow
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) { throw new UnsupportedOperationException();}

    @Unique
    private final ConcurrentHashMap<Class<?>, Object> storedData = new ConcurrentHashMap<>();

    @Override
    public <T> void putData(Class<T> clazz, T data) {
        storedData.put(clazz, data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOrCreateData(Class<T> clazz, Supplier<T> supplier) {
        return (T) storedData.computeIfAbsent(clazz, e -> supplier.get());
    }

    @Override
    public ConcurrentHashMap<Class<?>, Object> _getMap() {
        return storedData;
    }

    @Override
    public void _setMap(ConcurrentHashMap<Class<?>, Object> map) {
        storedData.putAll(map);
    }

    @Override
    public <T> @Nullable T getData(Class<T> clazz) {
        return clazz.cast(storedData.get(clazz));
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void onConstruct(CallbackInfo cb) {
        _init();
    }

    @Override
    public long getPistonMovementTick() {
        return pistonMovementTick;
    }

    @Inject(method = "Lnet/minecraft/entity/Entity;startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At("RETURN"))
    public void onStartRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cb) {
        if (cb.getReturnValueZ() && this instanceof CDPlayer) {
            PlayerStartRidingListener.EVENT.invoker().onStartRiding((CDPlayer) this, (CDEntity) entity);
        }
    }

    @Override
    public float _getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return getEyeHeight(pose, dimensions);
    }
}
