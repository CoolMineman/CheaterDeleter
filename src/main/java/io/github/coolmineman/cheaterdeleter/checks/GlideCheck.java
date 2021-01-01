package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.PlayerDataManager;
import io.github.coolmineman.cheaterdeleter.duck.IPlayerMoveC2SPacket;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class GlideCheck extends Check implements MovementPacketCallback {
    public GlideCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(ServerPlayerEntity player, IPlayerMoveC2SPacket packet) {
        GlideCheckData data = PlayerDataManager.getOrCreate(player, GlideCheckData.class, GlideCheckData::new);
        if (!packet.isOnGround() && packet.isChangePosition()) {
            if (data.isActive) {
                double velocity = player.getVelocity().getY();
                if (velocity < -1) {
                    boolean failedCheck = data.lasty - packet.getY() < 0.9;
                    if (failedCheck) {
                        data.violations++;
                    } else {
                        data.violations -= 1;
                    }
                    if (data.violations >= 4) {
                        flag(player, "Failed Glide Check");
                        data.violations = 0;
                    }
                    if (data.violations < 0) data.violations = 0;
                }
            }
            data.lasty = packet.getY();
            data.isActive = true;
        }
        return ActionResult.PASS;
    }

    private class GlideCheckData {
        double lasty = 0.0;
        int violations = 0;
        boolean isActive = false;
    }
    
}
