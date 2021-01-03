package io.github.coolmineman.cheaterdeleter.objects;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.compat.CompatManager;
import io.github.coolmineman.cheaterdeleter.compat.StepHeightEntityAttributeCompat;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class CDPlayer {
    public final ServerPlayerEntity mcPlayer;

    private HashMap<Class<?>, Object> storedData = new HashMap<>();

    public <T> void putData(Class<T> clazz, T data) {
        storedData.put(clazz, data);
    }

    @Nullable
    public <T> T getData(Class<T> clazz) {
        return clazz.cast(storedData.get(clazz));
    }

    public <T> T getOrCreateData(Class<T> clazz, Supplier<T> supplier) {
        T result = getData(clazz);
        if (result == null) {
            result = supplier.get();
            putData(clazz, result);
        }
        return result;
    }

    public double getX() {
        return mcPlayer.getX();
    }

    public double getY() {
        return mcPlayer.getY();
    }

    public double getZ() {
        return mcPlayer.getZ();
    }

    public Vec3d getVelocity() {
        return mcPlayer.getVelocity();
    }

    public boolean isOnGround() {
        return mcPlayer.isOnGround();
    }

    public World getWorld() {
        return mcPlayer.world;
    }

    public float getStepHeight() {
        StepHeightEntityAttributeCompat compat = CompatManager.getCompatHolder(StepHeightEntityAttributeCompat.class).compat;
        if (compat == null) {
            return 0.6f;
        } else {
            return compat.getStepHeightAddition(mcPlayer) + 0.6f;
        }
    }

    /**
     * True if flying with an Elytra or similar
     */
    public boolean isFallFlying() {
        return mcPlayer.isFallFlying();
    }

    public ServerPlayNetworkHandler getNetworkHandler() {
        return mcPlayer.networkHandler;
    }

    public UUID getUuid() {
        return mcPlayer.getUuid();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Player['%s'/%s, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.mcPlayer.getName().asString(), this.getUuid().toString(), this.getWorld() == null ? "~NULL~" : this.getWorld().toString(), this.getX(), this.getY(), this.getZ());
    }

    public static CDPlayer of(ServerPlayerEntity mcPlayer) {
        return ((CDPlayer.Provider)mcPlayer).getCDPlayer();
    }

    //Use CDPlayer.of
    private CDPlayer(ServerPlayerEntity mcPlayer) {
        this.mcPlayer = mcPlayer;
    }

    public static interface Provider {
        default CDPlayer createNewCDPlayer(ServerPlayerEntity mcPlayer) {
            return new CDPlayer(mcPlayer);
        }

        CDPlayer getCDPlayer();
    }
}
