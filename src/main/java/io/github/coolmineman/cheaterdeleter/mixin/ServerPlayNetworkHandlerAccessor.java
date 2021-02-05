package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.ServerPlayNetworkHandler;

@Mixin(ServerPlayNetworkHandler.class)
public interface ServerPlayNetworkHandlerAccessor {
    @Accessor
    int getRequestedTeleportId();
}
