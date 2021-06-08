package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.coolmineman.cheaterdeleter.objects.PlayerInteractEntityC2SPacketView;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.world.ServerWorld;

@Mixin(PlayerInteractEntityC2SPacket.class)
public class PlayerInteractEntityC2SPacketMixin implements PlayerInteractEntityC2SPacketView {
    private PlayerInteractEntityC2SPacketView.InteractType type;

    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void iHateThisHack(PacketByteBuf buf, CallbackInfo bruh, @Coerce Enum interactType) {
        type = PlayerInteractEntityC2SPacketView.InteractType.ALL[interactType.ordinal()];
    } 

    @Override
    public Entity getEntity(ServerWorld world) {
        return ((PlayerInteractEntityC2SPacket)(Object)(this)).getEntity(world);
    }

    @Override
    public InteractType type() {
        return type;
    }

    @Override
    public boolean isPlayerSneaking() {
        return ((PlayerInteractEntityC2SPacket)(Object)(this)).isPlayerSneaking();
    }
    
}
