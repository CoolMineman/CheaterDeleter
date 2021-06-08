package io.github.coolmineman.cheaterdeleter.objects;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public interface PlayerInteractEntityC2SPacketView {
    Entity getEntity(ServerWorld world);
    InteractType type();
    boolean isPlayerSneaking();

    enum InteractType {
        INTERACT,
        ATTACK,
        INTERACT_AT;

        public static final InteractType[] ALL = InteractType.values(); 
    }
}
