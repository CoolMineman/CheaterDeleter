package io.github.coolmineman.cheaterdeleter.modules.debug;

import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class DebugModuleManager {
    private DebugModuleManager() { }

    public static void init() {
        if (GlobalConfig.debugMode >= 3) {
            ModuleManager.registerCheck(new PacketClassSpammerModule());
        }
    }
}
