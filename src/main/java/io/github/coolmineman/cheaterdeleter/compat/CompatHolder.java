package io.github.coolmineman.cheaterdeleter.compat;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.loader.api.FabricLoader;

public final class CompatHolder<T extends Compat> {
    @Nullable
    public final T compat;
    public final String modid;
    public final String modName;
    public final Class<T> clazz;

    public CompatHolder(Supplier<T> supplier, String modid, String modName, Class<T> clazz) {
        this.modid = modid;
        this.modName = modName;
        this.clazz = clazz;
        if (FabricLoader.getInstance().isModLoaded(modid)) {
            CompatManager.LOGGER.info("Loading Compatability For {}", modid);
            this.compat = supplier.get();
            CompatManager.LOGGER.info("Succesfully Loaded Compatability For {}", modName);
        } else {
            this.compat = null;
        }
    }
}
