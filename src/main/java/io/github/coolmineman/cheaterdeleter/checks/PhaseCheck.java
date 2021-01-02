package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

//TODO Teleports
//TODO Moving Blocks (Give Phase Bypass for that block)
//TODO setBlock event (related to above)

public class PhaseCheck extends Check implements MovementPacketCallback {
    private static final double INTERP = 0.7;

    public PhaseCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (packet.isChangePosition()) {
            World world = player.getWorld();
            Box box = CollisionUtil.getBoxForPosition(player, packet.getX(), packet.getY(), packet.getZ()).expand(-0.1);
            assertOrFlag(!CollisionUtil.isTouching(box, world, CollisionUtil.touchingNonSteppablePredicate(player.getStepHeight(), box, player.getY(), packet.getY())), player);
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
                box = CollisionUtil.getBoxForPosition(player, currentX, currentY, currentZ).expand(-0.1);
                assertOrFlag2(!CollisionUtil.isTouching(box, world, CollisionUtil.touchingNonSteppablePredicate(player.getStepHeight(), box, player.getY(), packet.getY())), player);
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

    private void assertOrFlag(boolean condition, CDPlayer player) {
        if (!condition) flag(player, "Failed Phase Check1");
    }

    private void assertOrFlag2(boolean condition, CDPlayer player) {
        if (!condition) flag(player, "Failed Phase Check2");
    }
    
}
