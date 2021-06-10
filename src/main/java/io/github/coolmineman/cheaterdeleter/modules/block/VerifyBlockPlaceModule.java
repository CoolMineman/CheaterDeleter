package io.github.coolmineman.cheaterdeleter.modules.block;

import io.github.coolmineman.cheaterdeleter.events.PlayerInteractBlockCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;

public class VerifyBlockPlaceModule extends CDModule implements PlayerInteractBlockCallback {
    public VerifyBlockPlaceModule() {
        super("verify_block_place");
    }

    @Override
    public boolean onPlayerInteractBlock(CDPlayer player, PlayerInteractBlockC2SPacket packet) {
        BlockHitResult hr = packet.getBlockHitResult();
        return !enabledFor(player) || !player.getWorld().getBlockState(hr.getBlockPos()).getOutlineShape(player.getWorld(), hr.getBlockPos()).isEmpty();
    }

}
