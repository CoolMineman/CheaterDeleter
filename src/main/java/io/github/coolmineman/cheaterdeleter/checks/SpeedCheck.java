package io.github.coolmineman.cheaterdeleter.checks;

import com.google.common.util.concurrent.AtomicDouble;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.duck.PlayerPositionLookS2CPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.TrackerManager;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerLastTeleportData;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.util.ActionResult;

//TODO This is garbage
//TODO Ice
//TODO Block on head 
public class SpeedCheck extends Check
        implements MovementPacketCallback, PlayerEndTickCallback {
    public SpeedCheck() {
        MovementPacketCallback.EVENT.register(this);
        PlayerEndTickCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (player.shouldBypassAnticheat()) return ActionResult.PASS;
        if (!(player.mcPlayer.isCreative() || System.currentTimeMillis() - TrackerManager.get(PlayerLastTeleportData.class, player).lastTeleport < 3000 || CollisionUtil.isNearby(player, 2, 4, CollisionUtil.SPLIPPERY))) {
            double distanceSquared = MathUtil.getDistanceSquared(player.getX(), player.getZ(), packet.getX(),
                    packet.getZ());
            double distance = Math.sqrt(distanceSquared);
            if (distance < 100) { // Todo wtf
                player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new).distance.addAndGet(distance);
            }
        }
        return ActionResult.PASS;
    }

    public static class SpeedCheckData {
        AtomicDouble distance = new AtomicDouble(0.0);
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        if (player.shouldBypassAnticheat()) return;
        if (player.getWorld().getTime() % 40 == 0) {
            SpeedCheckData data = player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new);
            double distance = data.distance.getAndSet(0.0);
            double magicNumber = distance / (1 + (player.getSpeed() * 1.2)); // TODO Lmao what is this
            if (magicNumber > 13) {
                if (flag(player, FlagSeverity.MINOR, "Speed Check " + magicNumber))
                    player.rollback();
            }
        }
    }
}
