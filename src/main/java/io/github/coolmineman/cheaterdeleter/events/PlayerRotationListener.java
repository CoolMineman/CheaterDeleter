package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PlayerRotationListener {
    Event<PlayerRotationListener> EVENT = EventFactory.createArrayBacked(PlayerRotationListener.class,
        listeners -> (player, yawDelta, pitchDelta, move, packet) -> {
            for (PlayerRotationListener listener : listeners) {
                listener.onRotate(player, yawDelta, pitchDelta, move, packet);
            }
    });

    public static void init() {
        PlayerMovementListener.EVENT.register((player, packet, cause) -> {
            if (packet.isChangeLook() && !cause.isTeleport()) {
                float yawDelta = packet.getYaw() - player.getPacketYaw();
                float pitchDelta = player.getPacketPitch() - packet.getPitch();
                double move = Math.sqrt((yawDelta * yawDelta) + (pitchDelta * pitchDelta));
                EVENT.invoker().onRotate(player, yawDelta, pitchDelta, move, packet);
            }
        });
    }

    void onRotate(CDPlayer player, float yawDelta, float pitchDelta, double move, PlayerMoveC2SPacketView packet);
}
