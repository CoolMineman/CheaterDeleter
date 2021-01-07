package io.github.coolmineman.cheaterdeleter.objects;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.checks.Check;
import io.github.coolmineman.cheaterdeleter.checks.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.compat.CompatManager;
import io.github.coolmineman.cheaterdeleter.compat.LuckoPermissionsCompat;
import io.github.coolmineman.cheaterdeleter.compat.StepHeightEntityAttributeCompat;
import io.github.coolmineman.cheaterdeleter.events.ClickSlotC2SPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.OutgoingTeleportListener;
import io.github.coolmineman.cheaterdeleter.util.BoxUtil;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.network.MessageType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class CDPlayer {
    public final ServerPlayerEntity mcPlayer;

    private HashMap<Class<?>, Object> storedData = new HashMap<>();
    private Object2LongOpenHashMap<Check> lastFlagsMap = new Object2LongOpenHashMap<>();

    private double flags = 0.0;
    private long lastFlag = 0;

    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private boolean hasLastGood;

    private boolean hasCurrentPlayerScreenHandler;

    static {
        OutgoingTeleportListener.EVENT.register((player, packet) -> player.tickRollback(packet.getX(), packet.getY(), packet.getZ(), true));

        ClickSlotC2SPacketCallback.EVENT.register((player, packet) -> {
            if (packet.getSyncId() == 0 && player.mcPlayer.currentScreenHandler == player.mcPlayer.playerScreenHandler) {
                player.setHasCurrentPlayerScreenHandler(true);
            }
            return ActionResult.PASS;
        });
    }

    //Use CDPlayer.of
    private CDPlayer(ServerPlayerEntity mcPlayer) {
        this.mcPlayer = mcPlayer;
        this.lastGoodX = mcPlayer.getX();
        this.lastGoodY = mcPlayer.getY();
        this.lastGoodZ = mcPlayer.getZ();
        this.hasLastGood = false;
    }

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

    public void flag(int amount) {
        if (flags > 0) {
            long timeDelta = System.currentTimeMillis() - lastFlag;
            while (timeDelta > 1000) { //Runs once per second
                timeDelta -= 1000;
                //Max of 16 minor flags or 4 major per 1 min 
                flags -= 1.0 / (16.0 * 5.0 * 60.0);
                if (flags < 0) {
                    flags = 0;
                }
            }
        }
        flags += amount;
        lastFlag = System.currentTimeMillis();
        if (flags > 16) {
            kick(new LiteralText("Flagged Too Much by AC"));
        }
    }

    public void minorFlag() {
        flag(1);
    }

    public void majorFlag() {
        flag(4);
    }

    /**
     * Call on failed Check
     * @return punish if true
     */
    public boolean flag(Check check, Check.FlagSeverity severity) {
        if (check.getFlagCoolDownMs() > System.currentTimeMillis() - lastFlagsMap.getLong(check)) {
            return false;
        }
        lastFlagsMap.put(check, System.currentTimeMillis());
        switch (severity) {
            case MAJOR:
                majorFlag();
            break;
            case MINOR:
                minorFlag();
            break;
        }
        return true;
    }

    @Nullable
    public ScreenHandler getCurrentScreenHandler() {
        return mcPlayer.currentScreenHandler == mcPlayer.playerScreenHandler && !hasCurrentPlayerScreenHandler ? null : mcPlayer.currentScreenHandler;
    }

    public void setHasCurrentPlayerScreenHandler(boolean hasCurrentPlayerScreenHandler) {
        this.hasCurrentPlayerScreenHandler = hasCurrentPlayerScreenHandler;
    }

    public void rollback() {
        tickRollback(mcPlayer.getX(), mcPlayer.getY(), mcPlayer.getZ(), false);
        if (hasLastGood) teleport(lastGoodX, lastGoodY, lastGoodZ);
        hasLastGood = true;
    }

    public void tickRollback(double x, double y, double z, boolean isTeleport) {
        if (System.currentTimeMillis() - lastFlag > 5000 || isTeleport) {
            lastGoodX = x;
            lastGoodY = y;
            lastGoodZ = z;
            if (isTeleport)
                hasLastGood = false;
            else
                hasLastGood = true;
        }
    }

    public TriState getPermission(String permission) {
        LuckoPermissionsCompat compat = CompatManager.getCompatHolder(LuckoPermissionsCompat.class).compat;
        return compat != null ? compat.get(this, permission) : TriState.DEFAULT;
    }

    public boolean shouldBypassAnticheat() {
        return getPermission("cheaterdeleter.bypassanticheat") == TriState.TRUE;
    }

    public boolean shouldSendMajorFlags() {
        return getPermission("cheaterdeleter.sendmajorflags") == TriState.TRUE;
    }

    public boolean shouldSendMinorFlags() {
        return getPermission("cheaterdeleter.cheaterdeleter.sendminorflags") == TriState.TRUE;
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

    public float getYaw() {
        return mcPlayer.yaw;
    }

    public float getPitch() {
        return mcPlayer.pitch;
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

    public void teleport(double x, double y, double z) {
        teleport(x, y, z, getYaw(), getPitch());
    }

    public void teleport(double x, double y, double z, float yaw, float pitch) {
        mcPlayer.networkHandler.requestTeleport(x, y, z, yaw, pitch);
    }

    public float getSpeed() {
        return mcPlayer.getMovementSpeed();
    }

    /**
     * Gets Box For Current Position, Not Cached Use A Local Variable For Calls
     */
    public Box getBox() {
        return BoxUtil.getBoxForPosition(this, this.getX(), this.getY(), this.getZ());
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

    public void kick(Text text) {
        if (GlobalConfig.debugMode >= 2) {
            mcPlayer.sendMessage(new LiteralText("Kicked: ").append(text), MessageType.SYSTEM, Util.NIL_UUID);
            flags = 0;
        } else {
            mcPlayer.networkHandler.disconnect(text);
        }
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

    public static interface Provider {
        default CDPlayer createNewCDPlayer(ServerPlayerEntity mcPlayer) {
            return new CDPlayer(mcPlayer);
        }

        CDPlayer getCDPlayer();
    }
}
