package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;

public class PacketLimiterCheck extends Check implements PacketCallback {
    private static final long INTERVAL = 7;
    private static final long MAX_PACKETS_PER_SECOND = 500;

    public PacketLimiterCheck() {
        PacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        PacketVolumeData data = player.getOrCreateData(PacketVolumeData.class, PacketVolumeData::new);
        data.packetCount++;
        if (data.packetCount > MAX_PACKETS_PER_SECOND * INTERVAL) {
            player.kick(new LiteralText("Too Many Packets"));
            return ActionResult.FAIL;
        } else if (System.currentTimeMillis() - data.lastCheck >= INTERVAL * 1000) {
            data.packetCount = 0;
            data.lastCheck = System.currentTimeMillis();
        }
        return ActionResult.PASS;
    }

    public static class PacketVolumeData {
        public long packetCount = 0;
        public long lastCheck = System.currentTimeMillis();
    }
}
