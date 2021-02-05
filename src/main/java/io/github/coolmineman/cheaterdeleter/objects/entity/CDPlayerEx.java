package io.github.coolmineman.cheaterdeleter.objects.entity;

import io.github.coolmineman.cheaterdeleter.events.ClickSlotC2SPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.OutgoingTeleportListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.util.ActionResult;

/* Package Private */ class CDPlayerEx {
    static {
        OutgoingTeleportListener.EVENT.register((player, packet) -> player.tickRollback(packet.getX(), packet.getY(), packet.getZ(), true));

        ClickSlotC2SPacketCallback.EVENT.register((player, packet) -> {
            if (packet.getSyncId() == 0 && player.asMcPlayer().currentScreenHandler == player.asMcPlayer().playerScreenHandler) {
                player.setHasCurrentPlayerScreenHandler(true);
            }
            return ActionResult.PASS;
        });
    }

    public CDPlayerEx(CDPlayer player) {
        this.lastGoodX = player.getX();
        this.lastGoodY = player.getY();
        this.lastGoodZ = player.getZ();

        this.lastPacketX = player.getX();
        this.lastPacketY = player.getY();
        this.lastPacketZ = player.getZ();
    }

    public Object2LongOpenHashMap<CDModule> lastFlagsMap = new Object2LongOpenHashMap<>();

    public double flags = 0.0;
    public long lastFlag = 0;

    public double lastGoodX;
    public double lastGoodY;
    public double lastGoodZ;
    public boolean hasLastGood = false;

    public volatile double lastPacketX;
    public volatile double lastPacketY;
    public volatile double lastPacketZ;

    public boolean hasCurrentPlayerScreenHandler = false;
}
