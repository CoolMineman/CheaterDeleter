package io.github.coolmineman.cheaterdeleter.modules.block;

import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class BlockModuleManager {
    private BlockModuleManager() { }

    public static void init() {
        ModuleManager.registerModule(new VerifyBlockPlaceModule());
    }
}
