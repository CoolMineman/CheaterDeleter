package io.github.coolmineman.cheaterdeleter.modules.debug;

import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class DebugModuleManager {
    private DebugModuleManager() { }

    public static void init() {
        ModuleManager.registerModule(new PacketClassSpammerModule());
    }
}
