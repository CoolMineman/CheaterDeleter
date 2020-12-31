package io.github.coolmineman.cheaterdeleter.events;

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
                PlayerEndTickCallback.EVENT.invoker().onPlayerEndTick(player);
            }
        });
    }

    void onPlayerEndTick(ServerPlayerEntity player);
}
