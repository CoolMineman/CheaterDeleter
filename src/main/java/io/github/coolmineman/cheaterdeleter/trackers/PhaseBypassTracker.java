package io.github.coolmineman.cheaterdeleter.trackers;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.LongPredicate;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerSpawnListener;
import io.github.coolmineman.cheaterdeleter.events.PlayerStartRidingListener;
import io.github.coolmineman.cheaterdeleter.events.SetBlockStateListener;
import io.github.coolmineman.cheaterdeleter.events.TeleportConfirmListener;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.PhaseBypassData;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PhaseBypassTracker extends Tracker<PhaseBypassData> implements SetBlockStateListener, PlayerEndTickCallback, PlayerSpawnListener, PlayerStartRidingListener, TeleportConfirmListener {

    /**
     * True if not bypassed
     */
    public BiPredicate<World, BlockPos> isNotBypassed(CDPlayer player) {
        return (world, blockpos) -> get(player).bypassPos.indexOf(blockpos.asLong()) == -1;
    }

    protected PhaseBypassTracker() {
        super(PhaseBypassData.class);
        SetBlockStateListener.EVENT.register(this);
        PlayerEndTickCallback.EVENT.register(this);
        PlayerStartRidingListener.EVENT.register(this);
        TeleportConfirmListener.EVENT.register(this);
    }

    @Override
    public @NotNull PhaseBypassData get(CDEntity entity) {
        return entity.getOrCreateData(PhaseBypassData.class, PhaseBypassData::new);
    }

    private static int getMaxBypassDistanceSquared(CDPlayer player) {
        return player.getVehicleCd() == null ? 64 : 25;
    }

    @Override
    public void onSetBlockState(ServerWorld world, BlockPos pos, BlockState state) {
        List<ServerPlayerEntity> players = world.getPlayers();
        for (ServerPlayerEntity player1 : players) {
            CDPlayer player = CDPlayer.of(player1);
            if (pos.getSquaredDistance(player.getX(), player.getY(), player.getZ(), true) < getMaxBypassDistanceSquared(player)) {
                PhaseBypassData data = get(player);
                data.bypassPos.add(pos.asLong());
            }
        }
    }

    private static LongPredicate distanceFilter(CDPlayer player) {
        return posLong -> {
            int x = BlockPos.unpackLongX(posLong);
            int y = BlockPos.unpackLongY(posLong);
            int z = BlockPos.unpackLongZ(posLong);
            return MathUtil.getDistanceSquared(x, y, z, player.getX(), player.getY(), player.getZ()) >= getMaxBypassDistanceSquared(player);
        };
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        PhaseBypassData data = get(player);
        if (System.currentTimeMillis() - data.lastUpdated > 1000) {
            //Do some clean up
            data.bypassPos.removeIf(distanceFilter(player));
        }
    }

    @Override
    public void onSpawn(CDPlayer player) {
        PhaseBypassData data = get(player);
        data.lastUpdated = System.currentTimeMillis();
        BlockPos.stream(player.getBox().expand(-0.1)).forEach(pos -> data.bypassPos.add(pos.asLong()));
    }

    @Override
    public void onStartRiding(CDPlayer player, CDEntity vehicle) {
        PhaseBypassData data = get(player);
        data.lastUpdated = System.currentTimeMillis();
        BlockPos.stream(vehicle.getBox().expand(-0.1)).forEach(pos -> data.bypassPos.add(pos.asLong()));
    }

	@Override
	public void onTeleportConfirm(CDPlayer player, TeleportConfirmC2SPacket teleportConfirmC2SPacket, PlayerMoveC2SPacketView playerMoveC2SPacketView) {
		PhaseBypassData data = get(player);
        data.lastUpdated = System.currentTimeMillis();
        BlockPos.stream(player.getBoxForPosition(playerMoveC2SPacketView.getX(), playerMoveC2SPacketView.getY(), playerMoveC2SPacketView.getZ()).expand(-0.1)).forEach(pos -> data.bypassPos.add(pos.asLong()));
	}

}
