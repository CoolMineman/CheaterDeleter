package io.github.coolmineman.cheaterdeleter.modules.debug;

import io.github.coolmineman.cheaterdeleter.LoggerThread;
import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public class PacketClassSpammerModule extends CDModule implements PacketCallback {

    public PacketClassSpammerModule() {
        super("packet_class_spammer_module");
        PacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        if (!enabledFor(player) || GlobalConfig.getDebugMode() < 3) return ActionResult.PASS;
        if (!(packet instanceof PlayerMoveC2SPacket))
            LoggerThread.info(packet.getClass().getSimpleName());
        return ActionResult.PASS;
    }
    
}
