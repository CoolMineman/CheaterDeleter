package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerEndTickCallback {
    Event<PlayerEndTickCallback> EVENT = EventFactory.createArrayBacked(PlayerEndTickCallback.class,
        listeners -> player -> {
            for (PlayerEndTickCallback listener : listeners) {
                listener.onPlayerEndTick(player);
            }
    });

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerEndTickCallback.EVENT.invoker().onPlayerEndTick(CDPlayer.of(player));
            }
        });
    }

    void onPlayerEndTick(CDPlayer player);
}
