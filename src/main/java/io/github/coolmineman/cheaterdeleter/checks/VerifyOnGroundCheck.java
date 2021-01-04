package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BoxUtil;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;

//TODO: Enforce fall damage ourselves
public class VerifyOnGroundCheck extends Check implements MovementPacketCallback {
    public VerifyOnGroundCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (packet.isChangePosition()) {
            if (packet.isOnGround()) {
                Box playerBox = BoxUtil.getBoxForPosition(player, packet.getX(), packet.getY(), packet.getZ()).expand(0.4);
                // Not having a smaller feetbox shouldn't matter unless head is in a block lol
                assertOrFlag(CollisionUtil.isTouching(playerBox, player.getWorld(), CollisionUtil.touchingTopPredicate(playerBox)), player, FlagSeverity.MINOR, "Spoofed onGround true");
            } else {
                Box playerBox = BoxUtil.getBoxForPosition(player, packet.getX(), packet.getY(), packet.getZ()).expand(-0.1);
                Box feetBox = new Box(playerBox.minX, playerBox.minY, playerBox.minZ, playerBox.minX, playerBox.maxY, playerBox.minZ);
                assertOrFlag(!CollisionUtil.isTouching(feetBox, player.getWorld(), CollisionUtil.touchingTopPredicate(feetBox)), player, FlagSeverity.MINOR, "Spoofed onGround false");
            }
        } else if (packet.isOnGround() && !player.isOnGround()) {
            Box playerBox = BoxUtil.getBoxForPosition(player, player.getX(), player.getY(), player.getZ()).expand(0.4); //Should be liberal enough
            // Not having a smaller feetbox shouldn't matter unless head is in a block lol
            assertOrFlag(CollisionUtil.isTouching(playerBox, player.getWorld(), CollisionUtil.touchingTopPredicate(playerBox)), player, FlagSeverity.MAJOR, "Spoofed onGround true (Check 2)");
        }
        return ActionResult.PASS;
    }
}
