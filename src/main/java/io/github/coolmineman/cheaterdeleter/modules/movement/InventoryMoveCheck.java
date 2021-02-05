package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.util.ActionResult;

public class InventoryMoveCheck extends CDModule
        implements PlayerMovementListener, PacketCallback, PlayerDamageListener {
    public InventoryMoveCheck() {
        super("inventorymove_check");
        PlayerMovementListener.EVENT.register(this);
        PacketCallback.EVENT.register(this);
        PlayerDamageListener.EVENT.register(this);
    }

    @Override
    public long getFlagCoolDownMs() {
        return 100;
    }

    // TODO Damage
    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || player.isFallFlying()
                || BlockCollisionUtil.isNearby(player, 2, 4, BlockCollisionUtil.NON_SOLID_COLLISION))
            return;
        InventoryMoveCheckData data = player.getOrCreateData(InventoryMoveCheckData.class, InventoryMoveCheckData::new);
        if (packet.isChangePosition() && player.getCurrentScreenHandler() != null) {
            if (!data.wasOpen) {
                data.wasOpen = true;
                data.openTime = System.currentTimeMillis();
            }
            if (invalidateMove(player, packet)) {
                flag(player, FlagSeverity.MINOR, "Inventory Move");
            }
        } else if (player.getCurrentScreenHandler() == null) {
            data.lastDeltaX = Double.MAX_VALUE;
            data.lastDeltaZ = Double.MAX_VALUE;
            data.wasOpen = false;
        }
    }

    private boolean invalidateMove(CDPlayer player, PlayerMoveC2SPacketView packet) {
        InventoryMoveCheckData data = player.getOrCreateData(InventoryMoveCheckData.class, InventoryMoveCheckData::new);
        if (System.currentTimeMillis() - data.openTime < 500)
            return false;
        if (player.getVelocity().getY() < 0.0 && packet.getY() > player.getY())
            return true;
        double delaX = Math.abs(packet.getX() - player.getX());
        double delaZ = Math.abs(packet.getZ() - player.getZ());
        if (player.getVelocity().getX() == 0.0 && delaX > 0.0 && delaX > data.lastDeltaX)
            return true;
        if (player.getVelocity().getX() != 0.0) {
            data.lastDeltaX = Double.MAX_VALUE;
        } else {
            data.lastDeltaX = delaX;
        }
        if (player.getVelocity().getZ() == 0.0 && delaZ > 0.0 && delaZ > data.lastDeltaZ)
            return true;
        if (player.getVelocity().getZ() != 0.0) {
            data.lastDeltaZ = Double.MAX_VALUE;
        } else {
            data.lastDeltaZ = delaZ;
        }
        return false;
    }

    public static class InventoryMoveCheckData {
        volatile double lastDeltaX = Double.MAX_VALUE;
        volatile double lastDeltaZ = Double.MAX_VALUE;
        boolean wasOpen = false;
        volatile long openTime = 0;
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        if (packet instanceof CloseHandledScreenC2SPacket) {
            InventoryMoveCheckData data = player.getOrCreateData(InventoryMoveCheckData.class,
                    InventoryMoveCheckData::new);
            data.wasOpen = false;
        }
        return ActionResult.PASS;
    }

    @Override
    public void onPlayerDamage(CDPlayer player, DamageSource source, float amount) {
        InventoryMoveCheckData data = player.getOrCreateData(InventoryMoveCheckData.class, InventoryMoveCheckData::new);
        data.lastDeltaX = Double.MAX_VALUE;
        data.lastDeltaZ = Double.MAX_VALUE;
        data.openTime = System.currentTimeMillis() + 1000;
    }
    
}
