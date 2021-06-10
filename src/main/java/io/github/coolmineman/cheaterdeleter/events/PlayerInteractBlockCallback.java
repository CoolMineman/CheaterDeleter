package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

public interface PlayerInteractBlockCallback {
    Event<PlayerInteractBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerInteractBlockCallback.class,
        listeners -> (player, packet) -> {
            for (PlayerInteractBlockCallback listener : listeners) {
                if (listener.onPlayerInteractBlock(player, packet)) return true; 
            }
            return false;
    });

    /**
     * Called before a player interacts with a block, return false to stop illegal PlayerInteractBlockC2SPacket
     */
    boolean onPlayerInteractBlock(CDPlayer player, PlayerInteractBlockC2SPacket packet);
}
