package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin implements PlayerMoveC2SPacketView {
    @Shadow
    protected double x;
    @Shadow
    protected double y;
    @Shadow
    protected double z;
    @Shadow
    protected float yaw;
    @Shadow
    protected float pitch;
    @Shadow
    protected boolean onGround;
    @Shadow
    protected boolean changePosition;
    @Shadow
    protected boolean changeLook;

    @Override
    public double getX() {
        if (!changePosition) {
            new Exception().printStackTrace();
            throw new UnsupportedOperationException();
        }
        return x;
    }

    @Override
    public double getY() {
        if (!changePosition) {
            new Exception().printStackTrace();
            throw new UnsupportedOperationException();
        }
        return y;
    }

    @Override
    public double getZ() {
        if (!changePosition) {
            new Exception().printStackTrace();
            throw new UnsupportedOperationException();
        }
        return z;
    }

    @Override
    public float getYaw() {
        if (!changeLook) {
            new Exception().printStackTrace();
            throw new UnsupportedOperationException();
        }
        return yaw;
    }

    @Override
    public float getPitch() {
        if (!changeLook) {
            new Exception().printStackTrace();
            throw new UnsupportedOperationException();
        }
        return pitch;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public boolean isChangePosition() {
        return changePosition;
    }

    @Override
    public boolean isChangeLook() {
        return changeLook;
    }
}
