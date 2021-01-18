package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PlayerStartRidingListener {
    Event<PlayerStartRidingListener> EVENT = EventFactory.createArrayBacked(PlayerStartRidingListener.class,
        listeners -> (player, vehicle) -> {
            for (PlayerStartRidingListener listener : listeners) {
                listener.onStartRiding(player, vehicle);
            }
    });

    void onStartRiding(CDPlayer player, CDEntity vehicle);
}
