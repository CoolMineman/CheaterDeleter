package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.cheaterdeleter.events.SetBlockStateListener;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {
    @Shadow
    @Final
    private World world;

    @Inject(method = "setBlockState", at = @At("RETURN"))
    public void onSetBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cb) {
        if (!world.isClient && cb.getReturnValue() != null) SetBlockStateListener.EVENT.invoker().onSetBlockState((ServerWorld)world, pos, cb.getReturnValue());
    }
}
