package io.github.coolmineman.cheaterdeleter.modules.movement;

import java.util.Objects;

import io.github.coolmineman.cheaterdeleter.events.OutgoingTeleportListener;
import io.github.coolmineman.cheaterdeleter.events.TeleportConfirmListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.PlayerPositionLookS2CPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket.Flag;

public class TeleportVerifierCheck extends CDModule implements TeleportConfirmListener, OutgoingTeleportListener {

    public TeleportVerifierCheck() {
        super("teleport_verifier_check");
        TeleportConfirmListener.EVENT.register(this);
        OutgoingTeleportListener.EVENT.register(this);
    }

    private static class TeleportVerifierCheckData {
        public Int2ObjectMap<TeleportInfo> teleports = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());
    }

    private static class TeleportInfo {
        public final double x;
        public final double y;
        public final double z;
        public final boolean exactX;
        public final boolean exactY;
        public final boolean exactZ;

        public TeleportInfo(CDPlayer player, PlayerPositionLookS2CPacketView view) {
            if (view.getFlags().contains(Flag.X)) {
                exactX = false;
                x = view.getX() + player.getX();
            } else {
                exactX = true;
                x = view.getX();
            }
            if (view.getFlags().contains(Flag.Y)) {
                exactY = false;
                y = view.getY() + player.getY();
            } else {
                exactY = true;
                y = view.getY();
            }
            if (view.getFlags().contains(Flag.Z)) {
                exactZ = false;
                z = view.getZ() + player.getZ();
            } else {
                exactZ = true;
                z = view.getZ();
            }
        }
    }

    @Override
    public void onTeleportConfirm(CDPlayer player, TeleportConfirmC2SPacket teleportConfirmC2SPacket, PlayerMoveC2SPacketView playerMoveC2SPacketView) {
        TeleportInfo teleport = player.getData(TeleportVerifierCheckData.class).teleports.get(teleportConfirmC2SPacket.getTeleportId());
        player.getData(TeleportVerifierCheckData.class).teleports.remove(teleportConfirmC2SPacket.getTeleportId());
        if (enabledFor(player)) {
            Objects.requireNonNull(teleport, "If this is null you should panic");
            assertAxis(player, "x", teleport.exactX, playerMoveC2SPacketView.getX(), teleport.x);
            assertAxis(player, "y", teleport.exactY, playerMoveC2SPacketView.getY(), teleport.y);
            assertAxis(player, "z", teleport.exactZ, playerMoveC2SPacketView.getZ(), teleport.z);
        }
    }

    private void assertAxis(CDPlayer player, String axis, boolean exact, double obtained, double target) {
        if (exact) {
            assertOrKick(obtained == target, player, "Bad Teleport " + axis + " expected: " + target + " got: " + obtained);
        } else {
            assertOrKick(Math.abs(target - obtained) < 10, player, "Bad Teleport " + axis + " expected: " + target + " got: " + obtained); // TODO this is bad
        }
    }

    @Override
    public void onOutgoingTeleport(CDPlayer player, PlayerPositionLookS2CPacketView packet) {
        TeleportVerifierCheckData data = player.getOrCreateData(TeleportVerifierCheckData.class, TeleportVerifierCheckData::new);
        data.teleports.put(packet.getTeleportId(), new TeleportInfo(player, packet));
    }
}
