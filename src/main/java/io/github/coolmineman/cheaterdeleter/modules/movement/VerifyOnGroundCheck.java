package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import net.minecraft.util.math.Box;

//TODO: Enforce fall damage ourselves
//TODO: Edge case where player lands on a block that used to exist (common with pistons)
public class VerifyOnGroundCheck extends CDModule implements PlayerMovementListener {
    public VerifyOnGroundCheck() {
        super("verify_on_ground_check");
        PlayerMovementListener.EVENT.register(this);
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || cause.isTeleport() || player.isSpectator()) return;
        if (packet.isChangePosition()) {
            if (packet.isOnGround()) {
                Box playerBox = player.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(0.6); //Fences
                // Not having a smaller feetbox shouldn't matter unless head is in a block lol
                if (assertOrFlag(CollisionUtil.isTouching(player, playerBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(playerBox)), player, FlagSeverity.MINOR, "Spoofed onGround true")) player.rollback();
            }
            //This false flags way too much
            // else {
            //     Box playerBox = player.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(-0.1);
            //     Box feetBox = new Box(playerBox.minX, playerBox.minY, playerBox.minZ, playerBox.minX, playerBox.maxY, playerBox.minZ);
            //     //TODO: Why do fences break this?
            //     if (!BlockCollisionUtil.isNearby(player, 3, 3, BlockCollisionUtil.FENCE_LIKE)) assertOrFlag(!CollisionUtil.isTouching(player, feetBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(feetBox)), player, FlagSeverity.MINOR, "Spoofed onGround false");
            // }
        } else if (packet.isOnGround() && !player.isOnGround()) {
            Box playerBox = player.getBoxForPosition(player.getX(), player.getY(), player.getZ()).expand(0.4); //Should be liberal enough
            // Not having a smaller feetbox shouldn't matter unless head is in a block lol
            assertOrFlag(CollisionUtil.isTouching(player, playerBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(playerBox)), player, FlagSeverity.MAJOR, "Spoofed onGround true (Check 2)");
        }
    }
}
