package io.github.coolmineman.cheaterdeleter.modules.movement;

import com.google.common.util.concurrent.AtomicDouble;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.config.IntConfigValue;
import io.github.coolmineman.cheaterdeleter.events.OutgoingTeleportListener;
import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.PlayerPositionLookS2CPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.util.math.Box;

//TODO This is garbage
//TODO Ice
//TODO Velocity/Inertia (not the mod)
//TODO Block on head 
public class SpeedCheck extends CDModule implements PlayerMovementListener, PlayerEndTickCallback, OutgoingTeleportListener {
    private IntConfigValue maxSpeedMagicNumber = intConfig("max_speed_magic_number", 13);

    public SpeedCheck() {
        super("speed_check");
        PlayerMovementListener.EVENT.register(this);
        PlayerEndTickCallback.EVENT.register(this);
        OutgoingTeleportListener.EVENT.register(this);
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || player.isFallFlying() || player.isSpectator()) return;
        if (packet.isChangePosition() &&
            !(player.isCreative() || System.currentTimeMillis() - player.getTracked(Trackers.PLAYER_LAST_TELEPORT_TRACKER).lastTeleport < 100 || BlockCollisionUtil.isNearby(player, 2, 4, BlockCollisionUtil.SPLIPPERY))
        ) {
            double distanceSquared = MathUtil.getDistanceSquared(player.getPacketX(), player.getPacketZ(), packet.getX(),
                    packet.getZ());
            double distance = Math.sqrt(distanceSquared);
            Box box = player.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(0.1, -0.1,
                    0.1);
            if (!BlockCollisionUtil.isTouching(box, player.getWorld(), BlockCollisionUtil.touchingPredicate(box)))
                player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new).distance.addAndGet(distance); // Bruh
        }
    }

    public static class SpeedCheckData {
        AtomicDouble distance = new AtomicDouble(0.0);
        long time = System.currentTimeMillis();
    }

    @Override
    public long getFlagCoolDownMs() {
        return 0;
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        if (!enabledFor(player))
            return;
        SpeedCheckData data = player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new);
        if (System.currentTimeMillis() - data.time >= 2000) {
            double distance = data.distance.getAndSet(0.0);
            double magicNumber = (distance / (1 + (player.getSpeed() * 1.2))) * (2000.0 / (System.currentTimeMillis() - data.time)); // TODO Lmao what is this
            if (magicNumber > maxSpeedMagicNumber.get()) {
                if (flag(player, FlagSeverity.MINOR, "Speed Check " + magicNumber))
                    player.rollback();
            }
            data.time = System.currentTimeMillis();
        }
    }

    @Override
    public void onOutgoingTeleport(CDPlayer player, PlayerPositionLookS2CPacketView packet) {
        player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new).distance.set(0);
    }
}
