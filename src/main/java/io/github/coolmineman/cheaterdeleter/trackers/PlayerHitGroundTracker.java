package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerHitGroundData;

public class PlayerHitGroundTracker extends Tracker<PlayerHitGroundData> implements PlayerMovementListener {
    PlayerHitGroundTracker() {
        super(PlayerHitGroundData.class);
        PlayerMovementListener.EVENT.register(this);
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!packet.isOnGround()) {
            get(player).lastInAir.set(System.currentTimeMillis());
        }
    }

    @Override
    @NotNull
    public PlayerHitGroundData get(CDEntity entity) {
        return entity.getOrCreateData(PlayerHitGroundData.class, PlayerHitGroundData::new);
    }
}
