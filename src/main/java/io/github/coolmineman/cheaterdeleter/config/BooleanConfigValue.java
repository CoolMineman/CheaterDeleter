package io.github.coolmineman.cheaterdeleter.config;

import java.util.Map;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public final class BooleanConfigValue implements ConfigValue {
    private String key;
    private boolean value;

    public BooleanConfigValue(String key, boolean defaultValue) {
        this.key = key;
        this.value = defaultValue;
    }

    public void read(Map<String, NestedTextNode> map) {
        NestedTextNode node = map.get(key);
        if (node != null && node.isLeaf()) {
            value = Boolean.valueOf(node.asLeafString());
        }
    }

    public void write(Map<String, NestedTextNode> map) {
        map.put(key, NestedTextNode.of(Boolean.toString(value)));
    }

    public boolean get() {
        return value;
    }
}
