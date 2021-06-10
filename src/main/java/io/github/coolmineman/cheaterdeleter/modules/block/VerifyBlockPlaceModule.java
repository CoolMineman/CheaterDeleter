package io.github.coolmineman.cheaterdeleter.modules.block;

import io.github.coolmineman.cheaterdeleter.events.PlayerInteractBlockCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class VerifyBlockPlaceModule extends CDModule implements PlayerInteractBlockCallback {
    public VerifyBlockPlaceModule() {
        super("verify_block_place");
        PlayerInteractBlockCallback.EVENT.register(this);
    }

    @Override
    public boolean onPlayerInteractBlock(CDPlayer player, PlayerInteractBlockC2SPacket packet) {
        BlockHitResult hr = packet.getBlockHitResult();
        BlockPos pos = hr.getBlockPos();
        if (hr.isInsideBlock()) pos = pos.offset(hr.getSide());
        return !player.getWorld().getBlockState(pos).getOutlineShape(player.getWorld(), pos).isEmpty();
    }

}
