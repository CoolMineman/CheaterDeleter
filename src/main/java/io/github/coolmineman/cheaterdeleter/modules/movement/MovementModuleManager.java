package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.movement.entity.BoatFlyCheck;
import io.github.coolmineman.cheaterdeleter.modules.movement.entity.EntityVerticalCheck;

public class MovementModuleManager {
    private MovementModuleManager() { }

    public static void init() {
        ModuleManager.registerCheck(new VerifyOnGroundCheck());
        ModuleManager.registerCheck(new VerticalCheck());
        ModuleManager.registerCheck(new GlideCheck());
        ModuleManager.registerCheck(new OnGroundVerticalCheck());
        ModuleManager.registerCheck(new PhaseCheck());
        ModuleManager.registerCheck(new SpeedCheck());
        ModuleManager.registerCheck(new InventoryMoveCheck());
        //Entity
        ModuleManager.registerCheck(new BoatFlyCheck());
        ModuleManager.registerCheck(new EntityVerticalCheck());
    }
}
