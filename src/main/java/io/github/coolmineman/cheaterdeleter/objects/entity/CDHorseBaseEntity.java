package io.github.coolmineman.cheaterdeleter.objects.entity;

import net.minecraft.entity.passive.HorseBaseEntity;

public interface CDHorseBaseEntity extends CDEntity {

    @Override
    default double getBaseMaxJumpHeight() {
        return ((asMcHorseBaseEntity().getJumpStrength() - 0.4) * 9) + 0.25; //What is this
    }
    
    default HorseBaseEntity asMcHorseBaseEntity() {
        return (HorseBaseEntity)this;
    }

    public static CDPlayer of(HorseBaseEntity mcPlayer) {
        return ((CDPlayer)mcPlayer);
    }
}
