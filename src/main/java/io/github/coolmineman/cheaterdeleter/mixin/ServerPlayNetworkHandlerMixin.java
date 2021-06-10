package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.coolmineman.cheaterdeleter.events.PlayerInteractBlockCallback;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "onPlayerInteractBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo cb) {
        CDPlayer player = CDPlayer.of(((ServerPlayNetworkHandler)(Object)this).player);
        BlockPos pos = packet.getBlockHitResult().getBlockPos();
        if (!PlayerInteractBlockCallback.EVENT.invoker().onPlayerInteractBlock(player, packet)) {
            player.getNetworkHandler().sendPacket(new BlockUpdateS2CPacket(player.getWorld(), pos));
            player.getNetworkHandler().sendPacket(new BlockUpdateS2CPacket(player.getWorld(), pos.offset(packet.getBlockHitResult().getSide())));
            cb.cancel();
        } 
    }
}
