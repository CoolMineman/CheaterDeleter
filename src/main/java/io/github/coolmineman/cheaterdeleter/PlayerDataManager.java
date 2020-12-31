package io.github.coolmineman.cheaterdeleter;

import io.github.coolmineman.cheaterdeleter.duck.DataStorage;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerDataManager {
    private PlayerDataManager() { }

    public static <T> void put(ServerPlayerEntity playerEntity, Class<T> clazz, T data) {
        ((DataStorage)playerEntity).getStoredData().put(clazz, data);
    }

    public static <T> T get(ServerPlayerEntity playerEntity, Class<T> clazz) {
        return clazz.cast(((DataStorage)playerEntity).getStoredData().get(clazz));
    }
}
