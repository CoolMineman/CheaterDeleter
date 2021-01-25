package io.github.coolmineman.cheaterdeleter.config;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public class IntConfigValue implements ConfigValue {
    private String key;
    private int value;
    private String comment;

    public IntConfigValue(String key, int defaultValue) {
        this(key, defaultValue, null);
    }

    public IntConfigValue(String key, int defaultValue, String comment) {
        this.key = key;
        this.value = defaultValue;
        this.comment = comment;
    }

    public void read(Map<String, NestedTextNode> map) {
        NestedTextNode node = map.get(key);
        if (node != null && node.isLeaf()) {
            value = Integer.valueOf(node.asLeafString());
        }
    }

    public void write(Map<String, NestedTextNode> map) {
        NestedTextNode thisNode = NestedTextNode.of(Integer.toString(value));
        if (comment != null) thisNode.setComment(comment);
        map.put(key, thisNode);
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public int get() {
        return value;
    }
}
