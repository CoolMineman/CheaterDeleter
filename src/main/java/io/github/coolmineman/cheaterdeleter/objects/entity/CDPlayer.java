package io.github.coolmineman.cheaterdeleter.objects.entity;

import java.util.Locale;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.CheaterDeleterInit;
import io.github.coolmineman.cheaterdeleter.LoggerThread;
import io.github.coolmineman.cheaterdeleter.compat.CompatManager;
import io.github.coolmineman.cheaterdeleter.compat.LuckoPermissionsCompat;
import io.github.coolmineman.cheaterdeleter.compat.StepHeightEntityAttributeCompat;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.network.MessageType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public interface CDPlayer extends CDEntity {

    @Override
    default void _init() {
        CDEntity.super._init();
        CDPlayerEx ex = new CDPlayerEx(this);
        putData(CDPlayerEx.class, ex);
    }

    default void flag(int amount) {
        CDPlayerEx ex = getData(CDPlayerEx.class);
        if (ex.flags > 0) {
            long timeDelta = System.currentTimeMillis() - ex.lastFlag;
            while (timeDelta > 1000) { //Runs once per second
                timeDelta -= 1000;
                //Max of 16 minor flags or 4 major per 1 min 
                ex.flags -= 1.0 / (16.0 * 5.0 * 60.0);
                if (ex.flags < 0) {
                    ex.flags = 0;
                }
            }
        }
        ex.flags += amount;
        ex.lastFlag = System.currentTimeMillis();
        if (ex.flags > 16) {
            kick(new LiteralText("Flagged Too Much by AC"));
        }
    }

    default void minorFlag() {
        flag(1);
    }

    default void majorFlag() {
        flag(4);
    }

    /**
     * Call on failed Check
     * @return punish if true
     */
    default boolean flag(CDModule check, CDModule.FlagSeverity severity) {
        CDPlayerEx ex = getData(CDPlayerEx.class);
        if (check.getFlagCoolDownMs() > System.currentTimeMillis() - ex.lastFlagsMap.getLong(check)) {
            return false;
        }
        ex.lastFlagsMap.put(check, System.currentTimeMillis());
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
    default ScreenHandler getCurrentScreenHandler() {
        CDPlayerEx ex = getData(CDPlayerEx.class);
        return asMcPlayer().currentScreenHandler == asMcPlayer().playerScreenHandler && !ex.hasCurrentPlayerScreenHandler ? null : asMcPlayer().currentScreenHandler;
    }

    default void setHasCurrentPlayerScreenHandler(boolean hasCurrentPlayerScreenHandler) {
        CDPlayerEx ex = getData(CDPlayerEx.class);
        ex.hasCurrentPlayerScreenHandler = hasCurrentPlayerScreenHandler;
    }

    default void rollback() {
        CDPlayerEx ex = getData(CDPlayerEx.class);
        if (ex.hasLastGood) {
            teleportCd(ex.lastGoodX, ex.lastGoodY, ex.lastGoodZ);
            LoggerThread.info(String.format("Rolled %s back to %f %f %f", asString(), ex.lastGoodX, ex.lastGoodY, ex.lastGoodZ));
        }
    }

    default void tickRollback(double x, double y, double z, boolean isTeleport) {
        CDPlayerEx ex = getData(CDPlayerEx.class);
        if (System.currentTimeMillis() - ex.lastFlag > 5000 || isTeleport || !ex.hasLastGood) {
            ex.lastGoodX = x;
            ex.lastGoodY = y;
            ex.lastGoodZ = z;
            ex.hasLastGood = true;
        }
    }

    default TriState getPermission(String permission) {
        LuckoPermissionsCompat compat = CompatManager.getCompatHolder(LuckoPermissionsCompat.class).compat;
        return compat != null ? compat.get(this, permission) : TriState.DEFAULT;
    }

    default boolean shouldBypassAnticheat() {
        return getPermission("cheaterdeleter.bypassanticheat") == TriState.TRUE;
    }

    default boolean shouldSendMajorFlags() {
        return getPermission("cheaterdeleter.sendmajorflags") == TriState.TRUE;
    }

    default boolean shouldSendMinorFlags() {
        return getPermission("cheaterdeleter.cheaterdeleter.sendminorflags") == TriState.TRUE;
    }

    default void teleportCd(double x, double y, double z) {
        teleportCd(x, y, z, getYaw(), getPitch());
        CDPlayerEx ex = getData(CDPlayerEx.class);
        ex.lastGoodX = x;
        ex.lastGoodY = y;
        ex.lastGoodZ = z;
        ex.hasLastGood = true;
    }

    //TODO: Still breaks boats somehow
    default void teleportCd(double x, double y, double z, float yaw, float pitch) {
        asMcPlayer().stopRiding();
        asMcPlayer().teleport(getWorld(), x, y, z, yaw, pitch);
    }

    @Override
    default float getStepHeight() {
        StepHeightEntityAttributeCompat compat = CompatManager.getCompatHolder(StepHeightEntityAttributeCompat.class).compat;
        if (compat == null) {
            return 0.6f;
        } else {
            return compat.getStepHeightAddition(asMcPlayer()) + 0.6f;
        }
    }

    default void setPacketPos(PlayerMoveC2SPacketView packet) {
        if (packet.isChangePosition()) {
            CDPlayerEx ex = getData(CDPlayerEx.class);
            ex.lastPacketX = packet.getX();
            ex.lastPacketY = packet.getY();
            ex.lastPacketZ = packet.getZ();
        }
    }

    default double getPacketX() {
        return getData(CDPlayerEx.class).lastPacketX;
    }

    default double getPacketY() {
        return getData(CDPlayerEx.class).lastPacketY;
    }

    default double getPacketZ() {
        return getData(CDPlayerEx.class).lastPacketZ;
    }

    /**
     * True if flying with an Elytra or similar
     */
    default boolean isFallFlying() {
        return asMcPlayer().isFallFlying();
    }

    default ServerPlayNetworkHandler getNetworkHandler() {
        return asMcPlayer().networkHandler;
    }

    default void kick(Text text) {
        if (GlobalConfig.getDebugMode() >= 2) {
            asMcPlayer().sendMessage(new LiteralText("Kicked: ").append(text), MessageType.SYSTEM, Util.NIL_UUID);
            CDPlayerEx ex = getData(CDPlayerEx.class);
            ex.flags = 0;
        } else {
            asMcPlayer().networkHandler.disconnect(text);
        }
    }

    default boolean isCreative() {
        return asMcPlayer().isCreative();
    }

    default boolean isSpectator() {
        return asMcPlayer().isSpectator();
    }

    default String asString() {
        return String.format(Locale.ROOT, "Player['%s'/%s, w='%s', x=%.2f, y=%.2f, z=%.2f]", asMcPlayer().getName().asString(), this.getUuid().toString(), this.getWorld() == null ? "~NULL~" : this.getWorld().getRegistryKey().getValue().toString(), this.getX(), this.getY(), this.getZ());
    }

    default ServerPlayerEntity asMcPlayer() {
        return (ServerPlayerEntity)this;
    }

    public static CDPlayer of(ServerPlayerEntity mcPlayer) {
        return ((CDPlayer)mcPlayer);
    }
}
