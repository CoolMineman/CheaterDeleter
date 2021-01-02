package io.github.coolmineman.cheaterdeleter.compat;

import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import net.minecraft.server.network.ServerPlayerEntity;

public class StepHeightEntityAttributeCompat implements Compat {
    public float getStepHeightAddition(ServerPlayerEntity player) {
        return (float) player.getAttributeInstance(StepHeightEntityAttributeMain.STEP_HEIGHT).getValue();
    }
}
