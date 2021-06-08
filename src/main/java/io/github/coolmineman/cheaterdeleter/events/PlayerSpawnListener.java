package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PlayerSpawnListener {
    Event<PlayerSpawnListener> EVENT = EventFactory.createArrayBacked(PlayerSpawnListener.class,
        listeners -> player -> {
            System.out.println("Spawn " + player.asString());
            for (PlayerSpawnListener listener : listeners) {
                listener.onSpawn(player);
            }
    });
    
    void onSpawn(CDPlayer player);
}
