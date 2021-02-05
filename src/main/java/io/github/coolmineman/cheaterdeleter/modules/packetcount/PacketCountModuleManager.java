package io.github.coolmineman.cheaterdeleter.modules.packetcount;

import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class PacketCountModuleManager {
    private PacketCountModuleManager() { }

    public static void init() {
        ModuleManager.registerModule(new PacketLimiterCheck());
        ModuleManager.registerModule(new EntityTimerCheck());
    }
}
