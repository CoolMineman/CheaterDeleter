package io.github.coolmineman.cheaterdeleter.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerDamageListener {
    Event<PlayerDamageListener> EVENT = EventFactory.createArrayBacked(PlayerDamageListener.class,
        listeners -> (player, source, amount) -> {
            for (PlayerDamageListener listener : listeners) {
                listener.onPlayerDamage(player, source, amount);
            }
    });

    void onPlayerDamage(ServerPlayerEntity player, DamageSource source, float amount);
}
