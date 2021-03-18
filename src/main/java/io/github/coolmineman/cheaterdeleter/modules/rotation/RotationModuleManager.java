package io.github.coolmineman.cheaterdeleter.modules.rotation;

import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class RotationModuleManager {
    private RotationModuleManager() { }

    public static void init() {
        ModuleManager.registerModule(new CyberNuke());
        ModuleManager.registerModule(new LockDetector());
    }
}
