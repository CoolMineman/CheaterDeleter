package io.github.coolmineman.cheaterdeleter.modules.combat;

import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class CombatModuleManager {
    private CombatModuleManager() { }

    public static void init() {
        ModuleManager.registerModule(new VerifyAttackAngle());
    }
    
}
