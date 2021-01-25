package io.github.coolmineman.cheaterdeleter.config;

import java.util.Map;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public interface ConfigValue {
    public void read(Map<String, NestedTextNode> map);
    public void write(Map<String, NestedTextNode> map);
}
