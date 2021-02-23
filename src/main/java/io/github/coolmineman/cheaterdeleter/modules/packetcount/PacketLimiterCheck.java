package io.github.coolmineman.cheaterdeleter.modules.packetcount;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;

public class PacketLimiterCheck extends CDModule implements PacketCallback {
    private static final long INTERVAL = 7;
    private static final long MAX_PACKETS_PER_SECOND = 500;

    public PacketLimiterCheck() {
        super("packet_limiter");
        PacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        if (!enabledFor(player)) return ActionResult.PASS;
        PacketVolumeData data = player.getOrCreateData(PacketVolumeData.class, PacketVolumeData::new);

        if (packet.getClass().getName().equals("net.minecraft.class_2840")) {
            if (data.craftingCount != 0 ) {
                if (System.currentTimeMillis() - data.lastCraft < 125) {
                    player.getNetworkHandler().disconnect(new LiteralText("Too Many Crafting Packets")); //No Bypass Even In Testing
                    return ActionResult.FAIL;
                } else {
                    data.craftingCount = 0;
                    data.lastCraft = System.currentTimeMillis();
                    return ActionResult.PASS;
                }
            }
            data.craftingCount++;
            data.lastCraft = System.currentTimeMillis();
            return ActionResult.PASS;
        }
        data.packetCount++;
        if (data.packetCount > MAX_PACKETS_PER_SECOND * INTERVAL) {
            player.getNetworkHandler().disconnect(new LiteralText("Too Many Packets")); //No Bypass Even In Testing
            return ActionResult.FAIL;
        } else if (System.currentTimeMillis() - data.lastCheck >= INTERVAL * 1000) {
            data.packetCount = 0;
            data.lastCheck = System.currentTimeMillis();
        }
        return ActionResult.PASS;
    }

    public static class PacketVolumeData {
        public long packetCount = 0;
        public long craftingCount = 0;
        public long lastCheck = System.currentTimeMillis();
        public long lastCraft = System.currentTimeMillis();
    }
}
