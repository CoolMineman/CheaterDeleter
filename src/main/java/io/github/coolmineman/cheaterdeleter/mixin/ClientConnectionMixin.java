package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.ActionResult;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    private PacketListener packetListener;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet, CallbackInfo cb) {
        if (packetListener instanceof ServerPlayNetworkHandler) {
            ActionResult result = PacketCallback.EVENT.invoker().onPacket(CDPlayer.of(((ServerPlayNetworkHandler)packetListener).player), packet);
            if (result == ActionResult.FAIL) {
                cb.cancel();
            }
        }
    }
}
