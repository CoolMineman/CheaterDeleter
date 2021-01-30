package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.entity.LivingEntity;

/**
 * AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements CDPlayer {
    @Unique
    volatile float speed = 0; //I need this volatile or it doesn't work on the networking thread ahhhh

    @Inject(method = "setMovementSpeed", at = @At("HEAD"))
    public void setMovementSpeed(float movementSpeed, CallbackInfo cb) {
        this.speed = movementSpeed;
    }

    @Override
    public float getSpeed() {
        return speed;
    }
}
