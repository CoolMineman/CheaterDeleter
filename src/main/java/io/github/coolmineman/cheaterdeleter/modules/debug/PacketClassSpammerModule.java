package io.github.coolmineman.cheaterdeleter.modules.debug;

import io.github.coolmineman.cheaterdeleter.CheaterDeleterInit;
import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public class PacketClassSpammerModule extends CDModule implements PacketCallback {

    public PacketClassSpammerModule() {
        PacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        if (!(packet instanceof PlayerMoveC2SPacket))
            CheaterDeleterInit.GLOBAL_LOGGER.info(packet.getClass().getSimpleName());
        return ActionResult.PASS;
    }
    
}
