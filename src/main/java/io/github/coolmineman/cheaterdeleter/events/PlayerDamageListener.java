package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;

public interface PlayerDamageListener {
    Event<PlayerDamageListener> EVENT = EventFactory.createArrayBacked(PlayerDamageListener.class,
        listeners -> (player, source, amount) -> {
            for (PlayerDamageListener listener : listeners) {
                listener.onPlayerDamage(player, source, amount);
            }
    });

    void onPlayerDamage(CDPlayer player, DamageSource source, float amount);
}
