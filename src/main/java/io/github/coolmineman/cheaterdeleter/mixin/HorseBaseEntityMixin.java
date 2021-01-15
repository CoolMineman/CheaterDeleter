package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDHorseBaseEntity;
import net.minecraft.entity.passive.HorseBaseEntity;

@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin implements CDHorseBaseEntity {
    
}
