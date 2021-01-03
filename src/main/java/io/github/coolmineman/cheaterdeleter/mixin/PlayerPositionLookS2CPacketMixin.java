package io.github.coolmineman.cheaterdeleter.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.coolmineman.cheaterdeleter.duck.PlayerPositionLookS2CPacketView;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket.Flag;

@Mixin(PlayerPositionLookS2CPacket.class)
public class PlayerPositionLookS2CPacketMixin implements PlayerPositionLookS2CPacketView {
    @Shadow
    private double x;
    @Shadow
    private double y;
    @Shadow
    private double z;
    @Shadow
    private float yaw;
    @Shadow
    private float pitch;
    @Shadow
    private Set<PlayerPositionLookS2CPacket.Flag> flags;
    @Shadow
    private int teleportId;

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public Set<Flag> getFlags() {
        return flags;
    }

    @Override
    public int getTeleportId() {
        return teleportId;
    }

}
