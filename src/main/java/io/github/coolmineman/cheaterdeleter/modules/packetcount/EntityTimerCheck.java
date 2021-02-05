package io.github.coolmineman.cheaterdeleter.modules.packetcount;

import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.events.TeleportConfirmListener;
import io.github.coolmineman.cheaterdeleter.events.VehicleMoveListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public class EntityTimerCheck extends CDModule
        implements VehicleMoveListener, PlayerEndTickCallback, TeleportConfirmListener {
    private static final int CHECK_PERIOD = 5;
    private static final int MAX_PACKETS = 21 * CHECK_PERIOD; // 20 is target give some wiggle room

    public EntityTimerCheck() {
        super("entity_timer_check");
        VehicleMoveListener.EVENT.register(this);
        PlayerEndTickCallback.EVENT.register(this);
        TeleportConfirmListener.EVENT.register(this);
    }

    private class EntityTimerInfo {
        public AtomicInteger packets = new AtomicInteger(0);
        public long time = System.currentTimeMillis();
    }

    @Override
    public void onVehicleMove(CDPlayer player, CDEntity vehicle, PlayerMoveC2SPacketView playerLook,
            PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket,
            @Nullable VehicleMoveC2SPacket lastVehicleMoveC2SPacket) {
        if (!enabledFor(player))
            return;
        EntityTimerInfo info = player.getOrCreateData(EntityTimerInfo.class, EntityTimerInfo::new);
        info.packets.addAndGet(1);
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        if (!enabledFor(player) || player.getVehicleCd() != null)
            return;
        EntityTimerInfo info = player.getData(EntityTimerInfo.class);
        if (info != null) {
            long timediff = System.currentTimeMillis() - info.time;
            if (timediff > 1000 * CHECK_PERIOD) {
                int movementPackets = info.packets.getAndSet(0);
                info.time = System.currentTimeMillis();
                if (movementPackets > MAX_PACKETS && player.getVehicleCd() != null) {
                    if (flag(player, FlagSeverity.MINOR, "Failed Entity Timer Check"))
                        player.rollback();
                }
            }
        }
    }

    @Override
    public void onTeleportConfirm(CDPlayer player, TeleportConfirmC2SPacket teleportConfirmC2SPacket,
            PlayerMoveC2SPacketView playerMoveC2SPacketView) {
                EntityTimerInfo info = player.getOrCreateData(EntityTimerInfo.class, EntityTimerInfo::new);
                info.packets.decrementAndGet();

    }
}
