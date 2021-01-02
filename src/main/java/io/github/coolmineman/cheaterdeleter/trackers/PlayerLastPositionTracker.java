package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerLastPositionData;
import net.minecraft.util.ActionResult;

public class PlayerLastPositionTracker extends Tracker<PlayerLastPositionData> implements MovementPacketCallback {

    protected PlayerLastPositionTracker() {
        super(PlayerLastPositionData.class);
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    @NotNull
    PlayerLastPositionData get(CDPlayer player) {
        return player.getOrCreateData(PlayerLastPositionData.class, () -> new PlayerLastPositionData(player.getX(), player.getY(), player.getZ()));
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        PlayerLastPositionData data = get(player);
        if (packet.isChangePosition()) {
            data.lastX = packet.getX();
            data.lastY = packet.getY();
            data.lastZ = packet.getZ();
        }
        return ActionResult.PASS;
    }
    
}
