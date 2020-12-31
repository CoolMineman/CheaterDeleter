package io.github.coolmineman.cheaterdeleter.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.coolmineman.cheaterdeleter.duck.DataStorage;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements DataStorage {
    @Unique
    HashMap<Class<?>, Object> storedData = new HashMap<>();

    @Override
    public Map<Class<?>, Object> getStoredData() {
        return storedData;
    }
}
