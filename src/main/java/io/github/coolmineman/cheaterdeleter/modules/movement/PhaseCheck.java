package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerLastTeleportData;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

//TODO Teleports Request Confirms

public class PhaseCheck extends CDModule implements MovementPacketCallback {
    private static final double INTERP = 0.7;

    public PhaseCheck() {
        super("phase_check");
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (!enabledFor(player)) return ActionResult.PASS;
        if (packet.isChangePosition()) {
            World world = player.getWorld();
            if (world.getTime() - player.getPistonMovementTick() < 20 && Math.abs(player.getY() - packet.getY()) < 1.1 && MathUtil.getDistanceSquared(player.getX(), player.getZ(), packet.getX(), packet.getZ()) < 1.5) {
                //Avoid stupid piston edge case when a player is pushed into an unobstructed 1m tall block in certain cases 
                //mojank sets the stepheight to 1 server side causing desync occasionally
                //TODO maybe modify Entity.move to not cause desync with pistons
                //maybe just set the stepheight to a sane value and slightly change vanilla piston behavior? does anyone care about this?
                return ActionResult.PASS;
            }
            Box box = player.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(-0.1);
            if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(player.getStepHeight(), box, player.getY(), packet.getY()))), player, FlagSeverity.MAJOR, "Failed Phase Check1")) return ActionResult.FAIL;
            PlayerLastTeleportData playerLastTeleportData = player.getTracked(Trackers.PLAYER_LAST_TELEPORT_TRACKER);
            if (System.currentTimeMillis() - playerLastTeleportData.lastTeleport < 1000 && MathUtil.getDistanceSquared(playerLastTeleportData.lastTeleportX, playerLastTeleportData.lastTeleportY, playerLastTeleportData.lastTeleportZ, packet.getX(), packet.getY(), packet.getZ()) < 0.1) return ActionResult.PASS; 
            double currentX = player.getX();
            double currentY = player.getY();
            double currentZ = player.getZ();
            double targetX = packet.getX();
            boolean targetXPositive = targetX > player.getX();
            boolean hitTargetX = false;
            double targetY = packet.getY();
            boolean targetYPositive = targetY > player.getY();
            boolean hitTargetY = false;
            double targetZ = packet.getZ();
            boolean targetZPositive = targetZ > player.getZ();
            boolean hitTargetZ = false;
            while (!(hitTargetX && hitTargetY && hitTargetZ)) {
                box = player.getBoxForPosition(currentX, currentY, currentZ).expand(-0.1);
                if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(player.getStepHeight(), box, player.getY(), packet.getY()))), player, FlagSeverity.MAJOR, "Failed Phase Check2")) return ActionResult.FAIL;
                if (!hitTargetX) {
                    if (targetXPositive) {
                        if (currentX + INTERP < targetX) {
                            currentX += INTERP;
                        } else {
                            hitTargetX = true;
                        }
                    } else {
                        if (currentX - INTERP > targetX) {
                            currentX -= INTERP;
                        } else {
                            hitTargetX = true;
                        }
                    }
                }
                if (!hitTargetY) {
                    if (targetYPositive) {
                        if (currentY + INTERP < targetY) {
                            currentY += INTERP;
                        } else {
                            hitTargetY = true;
                        }
                    } else {
                        if (currentY - INTERP > targetY) {
                            currentY -= INTERP;
                        } else {
                            hitTargetY = true;
                        }
                    }
                }
                if (!hitTargetZ) {
                    if (targetZPositive) {
                        if (currentZ + INTERP < targetZ) {
                            currentZ += INTERP;
                        } else {
                            hitTargetZ = true;
                        }
                    } else {
                        if (currentZ - INTERP > targetZ) {
                            currentZ -= INTERP;
                        } else {
                            hitTargetZ = true;
                        }
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public long getFlagCoolDownMs() {
        return 0;
    }

    @Override
    public boolean flag(CDPlayer player, FlagSeverity severity, String message) {
        if (super.flag(player, severity, message)) player.rollback();
        return false;
    }
}
