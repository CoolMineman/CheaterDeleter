package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

public interface TeleportConfirmListener {
    Event<TeleportConfirmListener> EVENT = EventFactory.createArrayBacked(TeleportConfirmListener.class,
        listeners -> (player, teleportConfirmC2SPacket, playerMoveC2SPacketView) -> {
            for (TeleportConfirmListener listener : listeners) {
                listener.onTeleportConfirm(player, teleportConfirmC2SPacket, playerMoveC2SPacketView);
            }
            player.tickRollback(playerMoveC2SPacketView.getX(), playerMoveC2SPacketView.getY(), playerMoveC2SPacketView.getZ(), true);
    });
    
    void onTeleportConfirm(CDPlayer player, TeleportConfirmC2SPacket teleportConfirmC2SPacket, PlayerMoveC2SPacketView playerMoveC2SPacketView);
}
