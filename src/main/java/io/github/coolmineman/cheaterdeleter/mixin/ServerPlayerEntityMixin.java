package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements CDPlayer.Provider {
    @Unique
    CDPlayer cdPlayer = createNewCDPlayer((ServerPlayerEntity) (Object) this);

    @Override
    public CDPlayer getCDPlayer() {
        return cdPlayer;
    }

    @Inject(method = "damage", at = @At("HEAD"))
    void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cb) {
        PlayerDamageListener.EVENT.invoker().onPlayerDamage(cdPlayer, source, amount);
    }

    @Inject(method = "onSpawn", at = @At("TAIL"))
    public void onSpawn(CallbackInfo cb) {
        cdPlayer.tickRollback(cdPlayer.getX(), cdPlayer.getY(), cdPlayer.getZ(), true);
    }
}
