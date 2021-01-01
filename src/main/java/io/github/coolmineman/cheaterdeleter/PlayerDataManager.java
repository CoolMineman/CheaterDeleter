package io.github.coolmineman.cheaterdeleter;

import java.util.function.Supplier;

import io.github.coolmineman.cheaterdeleter.duck.DataStorage;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerDataManager {
    private PlayerDataManager() { }

    public static <T> void put(ServerPlayerEntity player, Class<T> clazz, T data) {
        ((DataStorage)player).getStoredData().put(clazz, data);
    }

    public static <T> T get(ServerPlayerEntity player, Class<T> clazz) {
        return clazz.cast(((DataStorage)player).getStoredData().get(clazz));
    }

    public static <T> T getOrCreate(ServerPlayerEntity player, Class<T> clazz, Supplier<T> supplier) {
        T result = get(player, clazz);
        if (result == null) {
            result = supplier.get();
            put(player, clazz, result);
        }
        return result;
    }
}
