package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.events.PlayerSpawnListener;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements CDPlayer {

    @Inject(method = "damage", at = @At("HEAD"))
    void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cb) {
        PlayerDamageListener.EVENT.invoker().onPlayerDamage((CDPlayer)this, source, amount);
    }

    @Inject(method = "onSpawn", at = @At("TAIL"))
    public void onSpawn(CallbackInfo cb) {
        this.tickRollback(this.getX(), this.getY(), this.getZ(), true);
        PlayerSpawnListener.EVENT.invoker().onSpawn((CDPlayer)this);
    }

    @Inject(method = "closeScreenHandler", at = @At("HEAD"))
    public void onCloseScreenHandler(CallbackInfo cb) {
        this.setHasCurrentPlayerScreenHandler(false);
    }

    @Inject(method = "copyFrom", at = @At("HEAD"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo cb) {
        _setMap(CDPlayer.of(oldPlayer)._getMap());
    }
}
