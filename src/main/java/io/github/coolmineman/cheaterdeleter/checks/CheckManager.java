package io.github.coolmineman.cheaterdeleter.checks;

import java.util.ArrayList;

public class CheckManager {
    private CheckManager() { }

    private static final ArrayList<Check> CHECKS = new ArrayList<>();

    public static void init() {
        registerCheck(new PacketLimiterCheck());
        registerCheck(new VerifyOnGroundCheck());
        registerCheck(new TimerCheck());
        registerCheck(new VerticalCheck());
        registerCheck(new GlideCheck());
        registerCheck(new OnGroundVerticalCheck());
        registerCheck(new PhaseCheck());
    }

    public static void registerCheck(Check check) {
        CHECKS.add(check);
    }

    public static int getCheckCount() {
        return CHECKS.size();
    }

}
