package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.coolmineman.cheaterdeleter.duck.MutableEntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;

@Mixin(EntityPositionS2CPacket.class)
public class EntityPositionS2CPacketMixin implements MutableEntityPositionS2CPacket {
    @Shadow
    private int id;
    @Shadow
    private double x;
    @Shadow
    private double y;
    @Shadow
    private double z;
    @Shadow
    private byte yaw;
    @Shadow
    private byte pitch;
    @Shadow
    private boolean onGround;

    @Override
    public int getId() {
        return id;
    }

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
    public byte getYaw() {
        return yaw;
    }

    @Override
    public byte getPitch() {
        return pitch;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void setYaw(byte yaw) {
        this.yaw = yaw;
    }

    @Override
    public void setPitch(byte pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

}
