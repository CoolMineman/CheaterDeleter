package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BoxUtil;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;

//TODO: Enforce fall damage ourselves
public class VerifyOnGroundCheck extends CDModule implements MovementPacketCallback {
    public VerifyOnGroundCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (player.shouldBypassAnticheat()) return ActionResult.PASS;
        if (packet.isChangePosition()) {
            if (packet.isOnGround()) {
                Box playerBox = BoxUtil.getBoxForPosition(player, packet.getX(), packet.getY(), packet.getZ()).expand(0.6); //Fences
                // Not having a smaller feetbox shouldn't matter unless head is in a block lol
                if (assertOrFlag(CollisionUtil.isTouching(player, playerBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(playerBox)), player, FlagSeverity.MINOR, "Spoofed onGround true")) player.rollback();
            } else {
                Box playerBox = BoxUtil.getBoxForPosition(player, packet.getX(), packet.getY(), packet.getZ()).expand(-0.1);
                Box feetBox = new Box(playerBox.minX, playerBox.minY, playerBox.minZ, playerBox.minX, playerBox.maxY, playerBox.minZ);
                //TODO: Why do fences break this?
                if (!BlockCollisionUtil.isNearby(player, 3, 3, BlockCollisionUtil.FENCE_LIKE)) assertOrFlag(!CollisionUtil.isTouching(player, feetBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(feetBox)), player, FlagSeverity.MINOR, "Spoofed onGround false");
            }
        } else if (packet.isOnGround() && !player.isOnGround()) {
            Box playerBox = BoxUtil.getBoxForPosition(player, player.getX(), player.getY(), player.getZ()).expand(0.4); //Should be liberal enough
            // Not having a smaller feetbox shouldn't matter unless head is in a block lol
            assertOrFlag(CollisionUtil.isTouching(player, playerBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(playerBox)), player, FlagSeverity.MAJOR, "Spoofed onGround true (Check 2)");
        }
        return ActionResult.PASS;
    }
}
