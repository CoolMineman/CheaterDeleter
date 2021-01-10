package io.github.coolmineman.cheaterdeleter.objects;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface MutableEntityPositionS2CPacket {
    int getId();

    default Entity getEntity(World world) {
        return world.getEntityById(getId());
    }

    double getX();

    double getY();

    double getZ();

    byte getYaw();

    byte getPitch();

    boolean isOnGround();

    void setId(int id);

    void setX(double x);

    void setY(double y);

    void setZ(double z);

    void setYaw(byte yaw);

    void setPitch(byte pitch);

    void setOnGround(boolean onGround);
}
