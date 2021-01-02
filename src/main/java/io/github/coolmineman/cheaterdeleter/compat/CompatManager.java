package io.github.coolmineman.cheaterdeleter.compat;

import java.util.HashMap;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatManager {
    public static final Logger LOGGER = LogManager.getLogger("CheaterDeleter-CompatManager");

    private CompatManager() { }

    private static final HashMap<Class<? extends Compat>, CompatHolder<? extends Compat>> COMPATS = new HashMap<>();

    public static void init() {
        compat(StepHeightEntityAttributeCompat::new, "step-height-entity-attribute", "Step Height Entity Attribute Library", StepHeightEntityAttributeCompat.class);
    }

    public static <T extends Compat> void compat(Supplier<T> supplier, String modid, String modName, Class<T> clazz) {
        CompatHolder<T> compatHolder = new CompatHolder<>(supplier, modid, modName, clazz);
        COMPATS.put(clazz, compatHolder);
    }

    public static <T extends Compat> CompatHolder<T> getCompatHolder(Class<T> clazz) {
        return (CompatHolder<T>) COMPATS.get(clazz);
    }

    public static int getCompatCount() {
        return COMPATS.size();
    }
}
