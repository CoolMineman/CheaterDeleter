package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PhaseCheck extends CDModule implements PlayerMovementListener {
    private static final double INTERP = 0.7;

    public PhaseCheck() {
        super("phase_check");
        PlayerMovementListener.EVENT.register(this);
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || cause.isTeleport() || player.isSpectator()) return;
        if (packet.isChangePosition() && !cause.isTeleport()) {
            World world = player.getWorld();
            if (world.getTime() - player.getPistonMovementTick() < 20 && Math.abs(player.getPacketY() - packet.getY()) < 1.1 && MathUtil.getDistanceSquared(player.getPacketX(), player.getPacketZ(), packet.getX(), packet.getZ()) < 1.5) {
                //Avoid stupid piston edge case when a player is pushed into an unobstructed 1m tall block in certain cases 
                //mojank sets the stepheight to 1 server side causing desync occasionally
                //TODO maybe modify Entity.move to not cause desync with pistons
                //maybe just set the stepheight to a sane value and slightly change vanilla piston behavior? does anyone care about this?
                return;
            }
            Box box = player.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(-0.1);
            if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(player.getStepHeight(), box, player.getPacketY(), packet.getY()))), player, FlagSeverity.MAJOR, "Failed Phase Check1")) return;
            double currentX = player.getPacketX();
            double currentY = player.getPacketY();
            double currentZ = player.getPacketZ();
            double targetX = packet.getX();
            boolean targetXPositive = targetX > player.getPacketX();
            boolean hitTargetX = false;
            double targetY = packet.getY();
            boolean targetYPositive = targetY > player.getPacketY();
            boolean hitTargetY = false;
            double targetZ = packet.getZ();
            boolean targetZPositive = targetZ > player.getPacketZ();
            boolean hitTargetZ = false;
            while (!(hitTargetX && hitTargetY && hitTargetZ)) {
                box = player.getBoxForPosition(currentX, currentY, currentZ).expand(-0.1);
                if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(player.getStepHeight(), box, player.getPacketY(), packet.getY()))), player, FlagSeverity.MAJOR, "Failed Phase Check2")) return;
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
    }

    @Override
    public long getFlagCoolDownMs() {
        return 0;
    }

    @Override
    public boolean flag(CDPlayer player, FlagSeverity severity, String message) {
        super.flag(player, severity, message);
        player.rollback();
        return false;
    }
}
