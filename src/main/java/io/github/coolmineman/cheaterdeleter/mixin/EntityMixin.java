package io.github.coolmineman.cheaterdeleter.mixin;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin implements CDEntity {
    @Shadow
    private long pistonMovementTick;

    @Unique
    private HashMap<Class<?>, Object> storedData = new HashMap<>();

    @Override
    public <T> void putData(Class<T> clazz, T data) {
        storedData.put(clazz, data);
    }

    @Override
    public <T> @Nullable T getData(Class<T> clazz) {
        return clazz.cast(storedData.get(clazz));
    }

    @Inject(method = "<init>()V", at = @At("TAIL"))
    void onConstruct(CallbackInfo cb) {
        _init();
    }

    @Override
    public long getPistonMovementTick() {
        return pistonMovementTick;
    }
}
