package io.github.coolmineman.cheaterdeleter.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.cheaterdeleter.duck.DataStorage;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements DataStorage {
    @Unique
    HashMap<Class<?>, Object> storedData = new HashMap<>();

    @Override
    public Map<Class<?>, Object> getStoredData() {
        return storedData;
    }

    @Inject(method = "damage", at = @At("HEAD"))
    void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cb) {
        PlayerDamageListener.EVENT.invoker().onPlayerDamage((ServerPlayerEntity)(Object)this, source, amount);
    }
}
