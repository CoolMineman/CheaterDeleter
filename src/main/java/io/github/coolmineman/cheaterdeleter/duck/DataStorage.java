package io.github.coolmineman.cheaterdeleter.duck;

import java.util.Map;

public interface DataStorage {
    public Map<Class<?>, Object> getStoredData();
}
