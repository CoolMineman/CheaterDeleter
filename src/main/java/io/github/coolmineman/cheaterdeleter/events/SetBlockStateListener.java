package io.github.coolmineman.cheaterdeleter.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface SetBlockStateListener {
    Event<SetBlockStateListener> EVENT = EventFactory.createArrayBacked(SetBlockStateListener.class,
        listeners -> (world, pos, state) -> {
            for (SetBlockStateListener listener : listeners) {
                listener.onSetBlockState(world, pos, state);
            }
    });
    
    void onSetBlockState(ServerWorld world, BlockPos pos, BlockState state);
}
