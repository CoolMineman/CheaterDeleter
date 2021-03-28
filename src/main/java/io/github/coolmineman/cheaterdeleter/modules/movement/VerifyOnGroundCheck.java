package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import net.minecraft.util.math.Box;

//TODO: Enforce fall damage ourselves
public class VerifyOnGroundCheck extends CDModule implements PlayerMovementListener {
    public VerifyOnGroundCheck() {
        super("verify_on_ground_check");
        PlayerMovementListener.EVENT.register(this);
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || cause.isTeleport() || player.isSpectator() || player.getWorld().getTime() - player.getPistonMovementTick() < 100) return;
        if (BlockCollisionUtil.isNearby(player, 5, 5, BlockCollisionUtil.BOUNCY)) return;
        if (packet.isChangePosition()) {
            if (packet.isOnGround()) {
                Box playerBox = player.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(0.6); //Fences
                // Not having a smaller feetbox shouldn't matter unless head is in a block lol
                if (assertOrFlag(CollisionUtil.isTouching(player, playerBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(playerBox)), player, FlagSeverity.MINOR, "Spoofed onGround true")) player.rollback();
            }
        } else if (packet.isOnGround() && !player.isOnGround()) {
            Box playerBox = player.getBoxForPosition(player.getPacketX(), player.getPacketY(), player.getPacketZ()).expand(0.4); //Should be liberal enough
            // Not having a smaller feetbox shouldn't matter unless head is in a block lol
            assertOrFlag(CollisionUtil.isTouching(player, playerBox, player.getWorld(), CollisionUtil.touchingRigidTopPredicates(playerBox)), player, FlagSeverity.MAJOR, "Spoofed onGround true (Check 2)");
        }
    }
}
