package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerHitGroundData;
import net.minecraft.util.ActionResult;

public class PlayerHitGroundTracker extends Tracker<PlayerHitGroundData> implements MovementPacketCallback {
    PlayerHitGroundTracker() {
        super(PlayerHitGroundData.class);
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (!packet.isOnGround()) {
            get(player).lastInAir.set(System.currentTimeMillis());
        }
        return ActionResult.PASS;
    }

    @Override
    @NotNull
    PlayerHitGroundData get(CDPlayer player) {
        return player.getOrCreateData(PlayerHitGroundData.class, PlayerHitGroundData::new);
    }
}
